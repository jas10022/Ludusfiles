package com.example.conversationsdk.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.conversationsdk.ChatActionCallback;
import com.example.conversationsdk.R;
import com.example.conversationsdk.fragment.BinderListFragment;
import com.moxtra.binder.sdk.OpenChatEventListener;
import com.moxtra.sdk.MXAccountManager;
import com.moxtra.sdk.MXChatManager;
import com.moxtra.sdk.MXChatCustomizer;
import com.moxtra.sdk.MXException;
import com.moxtra.sdk.MXNotificationManager;
import com.moxtra.sdk.MXSDKConfig;


public class Tablet_MainActivity extends FragmentActivity implements ChatActionCallback, View.OnClickListener {

    private static final String TAG = Tablet_MainActivity.class.getSimpleName();
    private FrameLayout leftFragmentLayout;
    private FrameLayout rightFragmentLayout;
    private View entireLayout;
    private Button createBinderBtn, createOneOnOneChatBtn;
    private Button chatSettingsBtn;
    private boolean isExpand = false;
    private BinderListFragment leftFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tablet_main);

        entireLayout = findViewById(R.id.left_entire_layout);
        leftFragmentLayout = (FrameLayout) findViewById(R.id.left_fragment);
        rightFragmentLayout = (FrameLayout) findViewById(R.id.right_fragment);
        createBinderBtn = (Button) findViewById(R.id.create_chat_btn);
        createOneOnOneChatBtn = (Button) findViewById(R.id.create_individual_chat_btn);
        chatSettingsBtn = (Button)findViewById(R.id.chat_settings_btn);

        createBinderBtn.setOnClickListener(this);
        createOneOnOneChatBtn.setOnClickListener(this);
        chatSettingsBtn.setOnClickListener(this);

        FragmentManager fm = getSupportFragmentManager();

        if(savedInstanceState == null)
            leftFragment = (BinderListFragment)Fragment.instantiate(this, BinderListFragment.class.getName());
        else {
            leftFragment = (BinderListFragment) fm.findFragmentById(R.id.left_fragment);
            isExpand = savedInstanceState.getBoolean("ExpandState");

            if(!isExpand){
                entireLayout.setVisibility(View.VISIBLE);
            }else{
                entireLayout.setVisibility(View.GONE);
            }
        }
        leftFragment.setOnOpenChatActionCallback(this);

        if(fm.findFragmentById(R.id.left_fragment) == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.left_fragment, leftFragment);
            ft.commit();
        }

        MXChatCustomizer.setOpenChatEventListener(new OpenChatEventListener() {
            @Override
            public void intentToOpenBinder(String binderId, Bundle bundle) {
                //if(MXChatBrandingManager.isForceTablet())
                {
                    try {
                        MXChatManager.getInstance().openChatForFragment(binderId, bundle, new MXChatManager.OnOpenChatForFragmentListener() {
                            @Override
                            public void onOpenChatForFragmentSuccess(String binderID, Fragment fragment) {
                                addChatFragment(fragment);
                                if(leftFragment != null)
                                    leftFragment.setCurrentSession(binderID);
                            }

                            @Override
                            public void onOpenChatForFragmentFailed(int i, String s) {

                            }
                        });
                    } catch (MXException.AccountManagerIsNotValid accountManagerIsNotValid) {
                        accountManagerIsNotValid.printStackTrace();
                    }
                }
            }
        });
        if(!LoginActivity.USE_GCM)
            MXChatManager.getInstance().setRemoteNotificationType(MXChatManager.PushNotificationType.LONG_CONNECTION);
        try {
            MXChatCustomizer.setForceTablet(true);
        } catch (MXException.OpenChatEventListenerMissed openChatEventListenerMissed) {
            openChatEventListenerMissed.printStackTrace();
        }
        MXChatCustomizer.setOnChatExpandButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Tablet_MainActivity.this, "fragment expand button clicked!", Toast.LENGTH_SHORT).show();
                if(isExpand){
                    isExpand = false;
                    entireLayout.setVisibility(View.VISIBLE);
                }else{
                    isExpand = true;
                    entireLayout.setVisibility(View.GONE);
                }
            }
        });

        if(getIntent() != null)
            MXNotificationManager.processMXNotification(this,getIntent());
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.right_fragment);
        if(fragment != null && MXChatManager.getInstance().onBackPressed(fragment))
            return;
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getSupportFragmentManager().findFragmentById(R.id.right_fragment) == null && leftFragment != null){
            leftFragment.openFirstChat();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("ExpandState", isExpand);
    }

    private void createIndividualChat() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Tablet_MainActivity.this);
        builder.setTitle(R.string.create_individual_chat);
        LayoutInflater inflater = Tablet_MainActivity.this.getLayoutInflater();
        final View contentView = inflater.inflate(R.layout.dialog_invite_to_chat, null);
        final EditText et = (EditText) contentView.findViewById(R.id.et_unique_id);
        et.setHint(R.string.invite_to_chat);
        builder.setView(contentView)
                .setPositiveButton(R.string.Done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText etUniqueID = (EditText) contentView.findViewById(R.id.et_unique_id);
                        try {
                            MXChatManager.getInstance().createIndividualChatForFragment(etUniqueID.getText().toString(), new MXChatManager.OnCreateChatForFragmentListener() {
                                @Override
                                public void onCreateChatForFragmentSuccess(String s, Fragment fragment) {
                                    addChatFragment(fragment);
                                }

                                @Override
                                public void onCreateChatForFragmentFailed(int i, String s) {
                                    Log.e(TAG, "onCreateChatForFragmentFailed: " + i + ", " + s);
                                    Toast.makeText(Tablet_MainActivity.this, "Can't create individual chat: " + s, Toast.LENGTH_LONG).show();
                                }
                            });
                        } catch (MXException.AccountManagerIsNotValid accountManagerIsNotValid) {
                            Log.e(TAG, "createIndividualChatForFragment failed.", accountManagerIsNotValid);
                            accountManagerIsNotValid.printStackTrace();
                        } catch (MXException.OpenChatEventListenerMissed openChatEventListenerMissed) {
                            Log.e(TAG, "createIndividualChatForFragment failed.", openChatEventListenerMissed);
                        }
                    }
                })
                .setNegativeButton(R.string.Cancel, null);

        builder.create().show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.create_chat_btn:
                AlertDialog.Builder builder = new AlertDialog.Builder(Tablet_MainActivity.this);
                builder.setTitle(R.string.create_chat);
                LayoutInflater inflater = Tablet_MainActivity.this.getLayoutInflater();
                final View contentView = inflater.inflate(R.layout.dialog_invite_to_chat, null);
                final EditText et = (EditText) contentView.findViewById(R.id.et_unique_id);
                et.setHint(R.string.create_chat);
                builder.setView(contentView)
                        .setPositiveButton(R.string.Done, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                String topic = et.getText().toString();
                                createChatAction(topic);
                            }
                        })
                        .setNegativeButton(R.string.Cancel, null);

                builder.create().show();
                break;
            case R.id.create_individual_chat_btn:
                createIndividualChat();
                break;
            case R.id.chat_settings_btn:
                Intent intent = new Intent(Tablet_MainActivity.this, ChatManagerSettingsActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void openChatAction(String binderId) {
        try {
            MXChatManager.getInstance().openChatForFragment(binderId, null, new MXChatManager.OnOpenChatForFragmentListener() {
                @Override
                public void onOpenChatForFragmentSuccess(String s, Fragment fragment) {
                    addChatFragment(fragment);
                }

                @Override
                public void onOpenChatForFragmentFailed(int i, String s) {

                }
            });
        } catch (MXException.AccountManagerIsNotValid accountManagerIsNotValid) {
            accountManagerIsNotValid.printStackTrace();
        }
    }

    @Override
    public void createChatAction(String topic) {
        try {
            MXChatManager.getInstance().createChatForFragment(topic, new MXChatManager.OnCreateChatForFragmentListener() {
                @Override
                public void onCreateChatForFragmentSuccess(String s, Fragment fragment) {
                    addChatFragment(fragment);
                }

                @Override
                public void onCreateChatForFragmentFailed(int i, String s) {

                }
            });
        } catch (MXException.OpenChatEventListenerMissed openChatEventListenerMissed) {
            openChatEventListenerMissed.printStackTrace();
        } catch (MXException.AccountManagerIsNotValid accountManagerIsNotValid) {
            accountManagerIsNotValid.printStackTrace();
        }
    }

    public void unlinkButtonPressed(View v) {
        MXAccountManager mAccountMgr = null;
        mAccountMgr = MXAccountManager.getInstance();

        if (mAccountMgr == null)
            return;

        if (mAccountMgr != null && mAccountMgr.isLinked()){

            mAccountMgr.unlinkAccount(new MXAccountManager.MXAccountUnlinkListener() {
                @Override
                public void onUnlinkAccountDone(MXSDKConfig.MXUserInfo user) {
                    Toast.makeText(getApplicationContext(), getString(R.string.unlink_success), Toast.LENGTH_LONG).show();
                    MXChatManager.getInstance().unlink();

                    //clear force tablet flag. Or if set this demo with tablet mode,
                    // after logout and switch to phone mode, this flag is exist so that can not open binder in phone mode
                    // this section only takes effort in this demo.
                    try {
                        MXChatCustomizer.setForceTablet(false);
                    } catch (MXException.OpenChatEventListenerMissed openChatEventListenerMissed) {
                        openChatEventListenerMissed.printStackTrace();
                    }
                    // jump to login activity
                    Intent intent = new Intent(Tablet_MainActivity.this, LoginActivity.class);
                    Tablet_MainActivity.this.startActivity(intent);
                    Tablet_MainActivity.this.finish();
                }
            });
        }
    }

    @Override
    public void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent");
        MXNotificationManager.processMXNotification(this,intent);
    }

    private void addChatFragment(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (getSupportFragmentManager().findFragmentById(R.id.right_fragment) == null)
            ft.add(R.id.right_fragment, fragment);
        else
            ft.replace(R.id.right_fragment, fragment);
        ft.commitAllowingStateLoss();
    }

    public void hideRightFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.right_fragment);
        if (fragment != null) {
            ft.detach(fragment);
            ft.commit();
        }
    }
}
