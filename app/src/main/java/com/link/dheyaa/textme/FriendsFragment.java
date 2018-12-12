package com.link.dheyaa.textme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


public class FriendsFragment extends android.support.v4.app.Fragment {

    @Nullable
    ListView listView ;
    private FirebaseAuth mAuth;
    private DatabaseReference DBref;
    private  FriendAdapter adapter;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.friends_tab, container, false);
        listView = (ListView) root.findViewById(R.id.friends_list);

        mAuth = FirebaseAuth.getInstance();
        DBref = FirebaseDatabase.getInstance().getReference("Users");
        DBref.child(mAuth.getCurrentUser().getUid()).addValueEventListener(userEventListener);


        adapter = new FriendAdapter( new ArrayList() , getContext());

       listView.setAdapter(adapter);
       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               listView.setClickable(false);
               Intent Message = new Intent(getActivity(), MessagingPage.class);
             //  Message.putExtra("Friend_name", friends.get(i).getName());
               startActivity(Message);
               listView.setClickable(true);
           }
       });

        root.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);

        return root;
     }

     ValueEventListener userEventListener = new  ValueEventListener() {
        ArrayList<User> friends = new ArrayList();
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            User user =  dataSnapshot.getValue(User.class);
            if(user.getFriends() == null){



            }else{
                 ArrayList friendsIds =  user.getFriends();
                 for(int i = 0 ; i < friendsIds.size() - 1 ; i++){
                     DBref.child(friendsIds.get(i + 1).toString()).addValueEventListener(new ValueEventListener() {
                         @Override
                         public void onDataChange(DataSnapshot dataSnapshot) {
                             User friend = dataSnapshot.getValue(User.class);
                             friends.add(friend);
                             System.out.println ( " -----------------friends [] ----->>>> " +dataSnapshot.getValue(User.class).toString());
                             adapter.add(new Friend(friend.getUsername() , friend.getEmail()));
                             adapter.notifyDataSetChanged();
                         }

                         @Override
                         public void onCancelled(DatabaseError databaseError) {

                         }
                     });

                 }
                //adapter.notifyDataSetChanged();

                System.out.println ( " ---------------------->>>> " +friendsIds.toString());
                System.out.println ( " ---------------------->>>> " +friends.toString());

            }
            System.out.println ( " ---------------------->>>> " +user.toString());

        }
        @Override
        public void onCancelled(DatabaseError error) {
        }
    };



}
