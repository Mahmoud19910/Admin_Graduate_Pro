package com.sarmed.my_admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sarmed.my_admin.Adapters.MessageAdapter;
import com.sarmed.my_admin.Adapters.UsersChatAdapter;
import com.sarmed.my_admin.FireBase_Service.Firebase_Function;
import com.sarmed.my_admin.Models.MessagesModles;
import com.sarmed.my_admin.Models.Users_Chat;
import com.sarmed.my_admin.databinding.ActivityChatBinding;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private MessageAdapter messageAdapter;
    private String senderRoom , reciverRoom , photoUrl;
    private DatabaseReference databaseReferenceSender , databaseReferenceReciver;
    private Users_Chat  usersChat;
    private MessagesModles messagesModles;
    Uri imageUri;
    private final int PICK_IMAGE_REQUEST = 22;
    private static final String CHANNEL_ID = "MyNotificationChannel";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

       boolean internetCheck =  checkInternetConnection(this);



        if(!internetCheck){
          binding.chatrecycle.setVisibility(View.INVISIBLE);
          binding.sendmsg.setClickable(false);
           Toast.makeText(this, "لايوجد اتصال بالانترنت !! ", Toast.LENGTH_LONG).show();
       }

        // جلب بيانات الشخص المراد ارسال له رسالة
         UsersChatAdapter.TransferToActivit(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                usersChat = (Users_Chat) o;
            }
        });



        binding.nameptv.setText(usersChat.getName());
        if(usersChat.getPhotoUrl() == null || usersChat.getPhotoUrl().isEmpty()){
            binding.profileImage.setImageDrawable(getDrawable(R.drawable.user));
        }else {
            Glide.with(ChatActivity.this).load(usersChat.getPhotoUrl()).into(binding.profileImage);
        }

        if(usersChat.isSession()==true){
            binding.block.setImageDrawable(getDrawable(R.drawable.online));
            binding.onlinetv.setText("متصل");
        }else {
            binding.block.setImageDrawable(getDrawable(R.drawable.ofline));
            binding.onlinetv.setText("غير متصل");
        }



        senderRoom = loadUid()+usersChat.getId();
        reciverRoom = usersChat.getId()+loadUid();


        //Adapter
        messageAdapter = new MessageAdapter(ChatActivity.this , loadUid());
        binding.chatrecycle.setAdapter(messageAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ChatActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setReverseLayout(true); // Set reverse layout
        binding.chatrecycle.setLayoutManager(layoutManager);
        binding.chatrecycle.scrollToPosition(messageAdapter.getItemCount() - 1);


        databaseReferenceSender = FirebaseDatabase.getInstance().getReference("Chat Messages").child(senderRoom);
        databaseReferenceReciver = FirebaseDatabase.getInstance().getReference("Chat Messages").child(reciverRoom);

        databaseReferenceSender.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageAdapter.clearAdapter();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    messagesModles = dataSnapshot.getValue(MessagesModles.class);
                    messageAdapter.addMessage(messagesModles);
                    binding.chatrecycle.scrollToPosition(0);

                    if(loadUid().equals(messagesModles.getReciverId())){
                        createNotificationChannel(messagesModles.getSenderName() , messagesModles.getMessageText());
                    }
                    if(usersChat.getId().equals(messagesModles.getReciverId())){
                        saveReciverId(messagesModles.getReciverId());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        Firebase_Function.getAllUsers_ChatFromRealTime(this, new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {

                Users_Chat usersCh = (Users_Chat) o;

                    if(usersCh.getId().equals(loadReciverId())){
                        if(usersCh.isSession()){
                            binding.block.setImageDrawable(getDrawable(R.drawable.online));
                            binding.onlinetv.setText("متصل");

                        }else {
                            binding.block.setImageDrawable(getDrawable(R.drawable.ofline));
                            binding.onlinetv.setText("غير متصل");

                        }
                    }




            }
        });



        binding.sendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String messageText =  binding.messageEdit.getText().toString();
               binding.messageEdit.setText("");
               binding.chatrecycle.scrollToPosition(messageAdapter.getItemCount()-1);
                if(messageText.trim().length()>0){
                    if(imageUri==null){
                        sendMessaging(messageText , "");
                    }else {
                        uploadImage(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                sendMessaging(messageText , o.toString());
                                binding.uploadImage.setVisibility(View.INVISIBLE);
                            }
                        });
                    }

                }
            }
        });

        binding.attachbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });


    }

    public String loadUid(){
        SharedPreferences sharedPreferences = getSharedPreferences("uid" , MODE_PRIVATE);
        return sharedPreferences.getString("id" , "");
    }

    public String loadName(){
        SharedPreferences sharedPreferences = getSharedPreferences("myname" , MODE_PRIVATE);
        return sharedPreferences.getString("name" , "");
    }

    public String loadPhoto(){
        SharedPreferences sharedPreferences = getSharedPreferences("senderPhoto" , MODE_PRIVATE);
        return sharedPreferences.getString("photo" , "");
    }

    // Get Time
    public String getTimeAtTheMoment(){
        // Get current time
        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();

        // Format the time using SimpleDateFormat
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        return  dateFormat.format(currentTime);
    }

    private void sendMessaging(String message , String photoUrl){
       String messId = UUID.randomUUID().toString();

        String messageId = databaseReferenceSender.push().getKey();
        MessagesModles mesg = new MessagesModles(messId , message , photoUrl , loadName() , usersChat.getId() , loadUid() , loadPhoto() , getTimeAtTheMoment() , messageId  );
           messageAdapter.addMessage(mesg);
           databaseReferenceSender
                   .child(messageId)
                   .setValue(mesg);

           databaseReferenceReciver
                   .child(messageId)
                   .setValue(mesg);

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            // Get the URI of the selected image from the intent data
            imageUri = data.getData();
            try {
                // Setting image on image view using Bitmap

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                binding.uploadImage.setImageBitmap(bitmap);
                binding.uploadImage.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }


    private void uploadImage(OnSuccessListener listener) {
        if (imageUri != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            // Defining the child of storageReference
            FirebaseStorage  storage = FirebaseStorage.getInstance();

            StorageReference storageReference = storage.getReference();
            StorageReference ref = storageReference.child("Chat Images/" + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image

            // Upload the image to Firebase Storage and add a success listener to get the download URL
            UploadTask uploadTask = ref.putFile(imageUri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                ref.getDownloadUrl().addOnSuccessListener(uri -> {

                    // Image uploaded successfully
                    // Dismiss dialog
                    progressDialog.dismiss();
                    Toast.makeText(ChatActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();

                 String   downloadUrl = uri.toString();
                 listener.onSuccess(downloadUrl);

                    imageUri = null;

                });
            }).addOnFailureListener(exception -> {
                // Handle any errors
                // ...
                // Error, Image not uploaded
                progressDialog.dismiss();
                Toast.makeText(ChatActivity.this, "Failed " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    // Progress Listener for loading
                    // percentage on the dialog box

                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());progressDialog.setMessage("Uploaded " + (int) progress + "%");
                }
            });
        }
    }
    // Check Internet Connection
    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    // Select Image method
    private void SelectImage() {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }

    public void saveReciverId(String reciveId){
        SharedPreferences sharedPreferences =    getSharedPreferences("reciveId" , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id" , reciveId);
        editor.apply();
    }

    // جلب رقم المعرف للمستخد
    private String loadReciverId() {
        SharedPreferences sharedPreferences = getSharedPreferences("reciveId", Context.MODE_PRIVATE);
        return sharedPreferences.getString("id", "");
    }

    private void createNotificationChannel(String nameUser , String messages) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Mahmoud Nassar");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Create an intent to open the app when the notification is clicked
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create the notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ChatActivity.this, CHANNEL_ID)
                .setContentTitle(nameUser)
                .setContentText(messages)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ofline)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true); // Enable auto-cancel to remove the notification when clicked
//        // Set the notification sound
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder.setSound(soundUri);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(ChatActivity.this);
        notificationManagerCompat.notify(10 , notificationBuilder.build());
    }

}