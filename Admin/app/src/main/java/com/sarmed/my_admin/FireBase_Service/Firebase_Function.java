package com.sarmed.my_admin.FireBase_Service;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sarmed.my_admin.Adapters.UsersChatAdapter;
import com.sarmed.my_admin.Admin_doctor_add;
import com.sarmed.my_admin.Models.Doctor;
import com.sarmed.my_admin.Models.Users_Chat;

public class Firebase_Function {

    //رفع على الفاير بيز ريال تايم
    public static void addToRealTime(Doctor doctor , Context context ,String uid){
        DatabaseReference databaseReference  = FirebaseDatabase.getInstance().getReference("Admin");
        databaseReference.child(uid).setValue(doctor).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage().toString()+"", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //جلب جميع الدكاترة من قاعدة البيانات
    public static void getAllUsers_ChatFromRealTime(Context context , UsersChatAdapter adapter , OnSuccessListener onSuccessListener){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UsersChat");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapter.clearAdapter();
                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    Users_Chat usersChat = dataSnapshot1.getValue(Users_Chat.class);
                    onSuccessListener.onSuccess(usersChat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(context, error.getMessage().toString()+"", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static void getAllUsers_ChatFromRealTime(Context context , OnSuccessListener onSuccessListener){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UsersChat");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    Users_Chat usersChat = dataSnapshot1.getValue(Users_Chat.class);
                    onSuccessListener.onSuccess(usersChat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(context, error.getMessage().toString()+"", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
