 package com.link.dheyaa.textme;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

 public class FriendAdapter extends ArrayAdapter<Friend>{

     private ArrayList<Friend> friends;
     Context mContext;

     public FriendAdapter(ArrayList<Friend> notes, Context context) {
         super(context, R.layout.friends_list_item, notes);
         this.friends = notes;
         this.mContext = context;
     }


     @Override
     public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
         View listItem = convertView;
         if(listItem == null)
             listItem = LayoutInflater.from(mContext).inflate(R.layout.friends_list_item,parent,false);

         Friend currentFriend = friends.get(position);

         TextView friendName = (TextView) listItem.findViewById(R.id.user_name);
         friendName.setText(currentFriend.getName());

         TextView friendEmail = (TextView) listItem.findViewById(R.id.user_email);
         friendEmail.setText(currentFriend.getEmail());

         return listItem;
     }
 }
