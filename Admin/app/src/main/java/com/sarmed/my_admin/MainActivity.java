package com.sarmed.my_admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sarmed.my_admin.FireBase_Service.Gmai_Auth;
import com.sarmed.my_admin.Interface.GmailDataListener;
import com.sarmed.my_admin.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements GmailDataListener {
ActivityMainBinding binding;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        updateSession(loadUid() , true);
        Gmai_Auth.createGoogleAuth(MainActivity.this);


//        Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/graduate-project-c9979.appspot.com/o/Chat%20Images%2F33892723-c25c-49bb-8248-af414b41937c?alt=media&token=ffab3ee0-900e-473c-9568-cb3cc67743d0")
//                        .into(new CustomTarget<Drawable>() {
//                            @Override
//                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                                VideoView videoView = new VideoView(MainActivity.this);
//                                videoView.setVideoURI(Uri.parse("https://firebasestorage.googleapis.com/v0/b/graduate-project-c9979.appspot.com/o/Chat%20Images%2F33892723-c25c-49bb-8248-af414b41937c?alt=media&token=ffab3ee0-900e-473c-9568-cb3cc67743d0"));
//
//                                FrameLayout frameLayout = findViewById(R.id.frameLayout);
//                                frameLayout.addView(videoView);
//                                videoView.start();
//                            }
//
//                            @Override
//                            public void onLoadCleared(@Nullable Drawable placeholder) {
//
//                            }
//                        });


        binding.AdminAddBtnFruitsSections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Admin_Add.class));
            }
        });
        binding.AdminAddBtnDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext() , Admin_doctor_add.class));
            }
        });
       binding.AdminExercisesAdd.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(getBaseContext() , Admin_exercises_add.class));
           }
       });

       binding.adminShose.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(getBaseContext() , Show_Data.class));
           }
       });

       binding.signInButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Gmai_Auth.onClickGoogleBut(MainActivity.this);
           }
       });

       binding.logout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Gmai_Auth.onSignOut(MainActivity.this);
           }
       });

       binding.lineraChat.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(MainActivity.this , UsersList_Activity.class));
           }
       });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Gmai_Auth.onResultGoolgleAuth(MainActivity.this , requestCode , data);
    }

    @Override
    public void getGmailData(String id, String email, String name, String photoUrl) {
        if(id!=null && email!=null && name!=null ){
            binding.linearLayout.setVisibility(View.VISIBLE);
            binding.signInButton.setVisibility(View.INVISIBLE);
            saveUid(id);
            saveName(name);

        }
    }

    public void saveUid(String id){
        SharedPreferences sharedPreferences = getSharedPreferences("uid" , MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id" , id);
        editor.apply();
    }

    public void saveName(String name){
        SharedPreferences sharedPreferences = getSharedPreferences("myname" , MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name" , name);
        editor.apply();
    }




    public String loadUid(){
        SharedPreferences sharedPreferences = getSharedPreferences("uid" , MODE_PRIVATE);
      return sharedPreferences.getString("id" , "");
    }

    @Override
    public void onBackPressed() {
createExitDialod();
    }

    @Override
    protected void onDestroy() {
        updateSession(loadUid() , false);
    }



    // ميثود الخروج من التطبيق
    private void createExitDialod() {
        Dialog dialog = new Dialog(MainActivity.this);
        int layout = R.layout.exit_dialog;
        View view = LayoutInflater.from(MainActivity.this).inflate(layout, null);
        ((View) view).findViewById(R.id.okBt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSession(loadUid() , false);
                MainActivity.this.finishAffinity();
            }
        });
        view.findViewById(R.id.cancelBt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.create();
        dialog.show();

    }

    public  void updateSession(String id , boolean isSesion) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Admin");

        databaseReference.child(id).child("sesion").setValue(isSesion)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // Update successful
                        Toast.makeText(MainActivity.this, "Session updated successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Update failed
                        Toast.makeText(MainActivity.this, "Failed to update session: " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }




}