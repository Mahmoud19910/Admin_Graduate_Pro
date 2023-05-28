package com.sarmed.my_admin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sarmed.my_admin.ChatActivity;
import com.sarmed.my_admin.Models.Users_Chat;
import com.sarmed.my_admin.R;
import com.sarmed.my_admin.UsersChat_Listener;

import java.util.ArrayList;
import java.util.List;


public class UsersChatAdapter extends RecyclerView.Adapter<UsersChatAdapter.ViewHolderUsers> {

    private Context context;
    private List<Users_Chat> usersChatList;
    public  static Users_Chat usersCh;

    public UsersChatAdapter(Context context) {
        this.context = context;
        usersChatList = new ArrayList<>();
    }



    public void addUsersChat(Users_Chat usersChat){
        usersChatList.add(usersChat);
        notifyDataSetChanged();
    }

    public void clearAdapter(){
        usersChatList.clear();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolderUsers onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.doctor_item_design, parent, false);
        return new ViewHolderUsers(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderUsers holder, int position) {

        Users_Chat usersChat = usersChatList.get(position);

        Toast.makeText(context, usersChat.getPhotoUrl()+"url", Toast.LENGTH_SHORT).show();
        if(usersChat.getPhotoUrl()==null || usersChat.getPhotoUrl().isEmpty()){
            holder.usersPhoto.setImageDrawable(context.getDrawable(R.drawable.user));
        }else {
            Glide.with(context).load(usersChat.getPhotoUrl()).into(holder.usersPhoto);
        }

        holder.nameUsers.setText(usersChat.getName());
        holder.phone.setText(usersChat.getPhone());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usersCh = usersChat;
                context.startActivity(new Intent(context , ChatActivity.class));

            }
        });



    }

    @Override
    public int getItemCount() {
        return usersChatList.size();
    }

    public class ViewHolderUsers extends RecyclerView.ViewHolder {
        CircularImageView usersPhoto;
        TextView nameUsers, phone;

        public ViewHolderUsers(@NonNull View itemView) {
            super(itemView);
            usersPhoto = itemView.findViewById(R.id.usersPhoto);
            nameUsers = itemView.findViewById(R.id.nameUsers);
            phone = itemView.findViewById(R.id.mobile);
        }
    }

    public static void TransferToActivit(OnSuccessListener onSuccessListener){
         onSuccessListener.onSuccess(usersCh);
    }





}

