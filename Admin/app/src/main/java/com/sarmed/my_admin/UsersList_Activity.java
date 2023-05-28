package com.sarmed.my_admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.sarmed.my_admin.Adapters.UsersChatAdapter;
import com.sarmed.my_admin.FireBase_Service.Firebase_Function;
import com.sarmed.my_admin.Models.Users_Chat;
import com.sarmed.my_admin.databinding.ActivityUsersListBinding;

public class UsersList_Activity extends AppCompatActivity {

   private ActivityUsersListBinding binding;
   private UsersChatAdapter usersChatAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        boolean internetCheck =  checkInternetConnection(this);
        if(!internetCheck){
            binding.usersRecycler.setVisibility(View.INVISIBLE);
            binding.noInternet.setVisibility(View.VISIBLE);
            Toast.makeText(this, "لايوجد اتصال بالانترنت !! ", Toast.LENGTH_LONG).show();
        }else {
            binding.noInternet.setVisibility(View.INVISIBLE);
        }

        //Adapter
        usersChatAdapter = new UsersChatAdapter(UsersList_Activity.this);
        binding.usersRecycler.setAdapter(usersChatAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(UsersList_Activity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.usersRecycler.setLayoutManager(layoutManager);

        Firebase_Function.getAllUsers_ChatFromRealTime(this, usersChatAdapter, new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {

                Users_Chat usersChat = (Users_Chat) o;
                usersChatAdapter.addUsersChat(usersChat);
            }
        });
    }


    // Check Internet Connection
    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}