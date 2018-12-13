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
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class FriendsFragment extends android.support.v4.app.Fragment {

    @Nullable
    ListView listView;
    private FirebaseAuth mAuth;
    private DatabaseReference DBref;
    private FriendAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.friends_tab, container, false);
        listView = (ListView) root.findViewById(R.id.friends_list);

        mAuth = FirebaseAuth.getInstance();
        DBref = FirebaseDatabase.getInstance().getReference("Users");
        DBref.child(mAuth.getCurrentUser().getUid()).child("friends").addValueEventListener(userEventListener);


        ArrayList<User> dumFriends = new ArrayList();
        dumFriends.add(new User( "123",  "12345",   new HashMap<String, Boolean>()  ));

        adapter = new FriendAdapter(dumFriends, getContext());

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

    ValueEventListener userEventListener = new ValueEventListener() {
        ArrayList<User> friends = new ArrayList<User>();

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            HashMap<String, Boolean> friendIds = (HashMap<String, Boolean>) dataSnapshot.getValue();
            if (friendIds.values() != null) {

                adapter.clear();
                Iterator it = friendIds.entrySet().iterator();
                while (it.hasNext()) {
                    final Map.Entry pair = (Map.Entry) it.next();

                    if (pair.getValue().equals(true)) {
                            DBref.child(pair.getKey().toString()).addValueEventListener(new ValueEventListener() {
                            String userId = pair.getKey().toString();

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                //  adapter.remove();
                                user.setId(userId);
                              //  friends.add(user);

                               // adapter.removeOld(userId , friends);

                                adapter.update(userId  , user);

                                //adapter.add(user);

                                System.out.println(" ->>>>>friend user" + user.toString()+"  ->>>>>");

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    it.remove();
                }

            }
        }

        @Override
        public void onCancelled(DatabaseError error) {
        }
    };


}
