package com.link.dheyaa.textme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class FriendsFragment extends android.support.v4.app.Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.friends_tab, container, false);
        ListView listView = (ListView) root.findViewById(R.id.friends_list);


        ArrayList<Friend> friends = new ArrayList<>();

        friends.add(new Friend("peter" , "peter@peter.peter"));
        friends.add(new Friend("malcolm" , "malcolm@malcolm.malcolm"));
        friends.add(new Friend("ali" , "ali@ali.ali"));
        friends.add(new Friend("mike" , "mike@mike.mike"));

        FriendAdapter adapter = new FriendAdapter(friends , getContext());

       listView.setAdapter(adapter);
       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               Intent Message = new Intent(getActivity(), MessagingPage.class);
               startActivity(Message);
           }
       });
        return root;
     }


}
