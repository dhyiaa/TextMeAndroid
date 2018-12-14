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
import android.widget.TextView;

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
    ArrayList<User> friends = new ArrayList<User>();


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.friends_tab, container, false);
        listView = (ListView) root.findViewById(R.id.friends_list);

        TextView noFriends=root.findViewById(R.id.nofriends);
        mAuth = FirebaseAuth.getInstance();
        DBref = FirebaseDatabase.getInstance().getReference("Users");
        DBref.child(mAuth.getCurrentUser().getUid()).child("friends").orderByValue().equalTo(true).addValueEventListener(userEventListener);

        this.adapter = new FriendAdapter( new ArrayList(), getContext());
        listView.setAdapter(this.adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listView.setClickable(false);
                Intent Message = new Intent(getActivity(), MessagingPage.class);
                  Message.putExtra("Friend_name", friends.get(i).getUsername());
                startActivity(Message);
                listView.setClickable(true);
            }
        });

        root.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);

        return root;
    }

    ValueEventListener userEventListener = new ValueEventListener() {
        //ArrayList<User> friends = new ArrayList<User>();

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            HashMap<String, Boolean> friendIds = (HashMap<String, Boolean>) dataSnapshot.getValue();
            if (friendIds != null) {
                Iterator it = friendIds.entrySet().iterator();
                while (it.hasNext()) {
                    final Map.Entry pair = (Map.Entry) it.next();

                    DBref.child(pair.getKey().toString()).orderByKey().addValueEventListener(new ValueEventListener() {
                            String userId = pair.getKey().toString();

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                user.setId(userId);
                                user.setFriends(null);

                                adapter.removeOld(user, friends);
                                friends.add(user);

                                adapter.setFriends(friends);

                               // adapter.add(user);

                                System.out.println(">>>>><<<<<<<<----ok ok ok   "+friends.toString());
                                adapter.clear();
                                adapter.remove();

                                Sorting.quickSortByAlphabet(friends);
                                adapter.addAll(friends);

                                adapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    it.remove();
                }

            }else{


            }
        }

        @Override
        public void onCancelled(DatabaseError error) {
        }
    };


}
