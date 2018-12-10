package com.link.dheyaa.textme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
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
import java.util.List;


public class FriendsFragment extends android.support.v4.app.Fragment {

    @Nullable
    ListView listView ;
    private FirebaseAuth mAuth;
    private DatabaseReference DBref;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.friends_tab, container, false);
         listView = (ListView) root.findViewById(R.id.friends_list);

        mAuth = FirebaseAuth.getInstance();
        DBref = FirebaseDatabase.getInstance().getReference("Users");


        final ArrayList<Friend> friends = new ArrayList<>();

     //   DBref.child(mAuth.getCurrentUser().getUid()).addValueEventListener(userEventListener);


        friends.add(new Friend("peter" , "peter@peter.peter"));
        friends.add(new Friend("malcolm" , "malcolm@malcolm.malcolm"));
        friends.add(new Friend("ali" , "ali@ali.ali"));
        friends.add(new Friend("mike" , "mike@mike.mike"));

        FriendAdapter adapter = new FriendAdapter(friends , getContext());

       listView.setAdapter(adapter);
       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               listView.setClickable(false);
               Intent Message = new Intent(getActivity(), MessagingPage.class);
               Message.putExtra("Friend_name", friends.get(i).getName());
               startActivity(Message);
               listView.setClickable(true);

           }
       });
        return root;
     }

   /*
   *
   *  ValueEventListener userEventListener = new  ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            User user =  dataSnapshot.getValue(User.class);
            Collection<String> friendsIds =  user.getFriends().values();
            //friendsIds
            DBref.


        }
        @Override
        public void onCancelled(DatabaseError error) {
        }
    };
   *
   * */


}
