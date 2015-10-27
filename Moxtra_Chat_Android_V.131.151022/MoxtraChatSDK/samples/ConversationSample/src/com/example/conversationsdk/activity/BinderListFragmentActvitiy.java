package com.example.conversationsdk.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.example.conversationsdk.ChatActionCallback;
import com.example.conversationsdk.R;
import com.example.conversationsdk.fragment.BinderListFragment;


public class BinderListFragmentActvitiy extends FragmentActivity implements ChatActionCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_list_fragment_actvitiy);

        Bundle bundle = new Bundle();
        bundle.putAll(getIntent().getExtras());
        BinderListFragment fragment = new BinderListFragment();
        fragment.setArguments(bundle);
        fragment.setOnOpenChatActionCallback(this);

       FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
       ft.add(R.id.fragment_holder,fragment);
       ft.commit();
    }

    @Override
    public void openChatAction(String binderId) {
        Phone_MainActivity.openChat(binderId);
    }

    @Override
    public void createChatAction(String binderId) {

    }
}
