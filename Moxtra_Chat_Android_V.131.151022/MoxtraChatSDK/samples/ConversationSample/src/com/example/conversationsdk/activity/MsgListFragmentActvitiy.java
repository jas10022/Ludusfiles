package com.example.conversationsdk.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.example.conversationsdk.R;
import com.example.conversationsdk.fragment.MessageListFragment;


public class MsgListFragmentActvitiy extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_list_fragment_actvitiy);
        MessageListFragment fragment = new MessageListFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_holder, fragment);
        ft.commit();
    }

 }
