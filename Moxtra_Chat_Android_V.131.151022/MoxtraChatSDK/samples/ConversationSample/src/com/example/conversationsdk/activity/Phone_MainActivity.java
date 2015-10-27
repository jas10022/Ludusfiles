package com.example.conversationsdk.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.conversationsdk.R;
import com.example.conversationsdk.fragment.BinderListFragment;
import com.moxtra.binder.conversation.MXConversationActivity;
import com.moxtra.sdk.MXAccountManager;
import com.moxtra.sdk.MXChatCustomizer;
import com.moxtra.sdk.MXChatManager;
import com.moxtra.sdk.MXException;
import com.moxtra.sdk.MXException.AccountManagerIsNotValid;
import com.moxtra.sdk.MXGroupMeetMember;
import com.moxtra.sdk.MXNotificationManager;
import com.moxtra.sdk.MXSDKConfig;
import com.moxtra.sdk.MXSDKException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Phone_MainActivity extends FragmentActivity {
    private static final String TAG = "MainActivity";

    private static final int DIALOG_USER_PROFILE = 1;
    private static final int DIALOG_INVITE = 2;
    private static final int DIALOG_JOIN_MEET = 3;
    private static final int DIALOG_OPEN_CHAT = 4;
    private static final int DIALOG_OPEN_INDIVIDUAL_CHAT = 5;
    private static final int REQUEST_CREATE_GROUP_CHAT = 100;

    public static String mBinderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        if(getIntent() != null)
            MXNotificationManager.processMXNotification(this, getIntent());
    }

    private void init() {
        MXChatCustomizer.setOpenChatEventListener(null);
        if (!LoginActivity.USE_GCM) {
            MXChatManager.getInstance().setRemoteNotificationType(MXChatManager.PushNotificationType.LONG_CONNECTION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CREATE_GROUP_CHAT:
                if (resultCode == Activity.RESULT_OK) {
                    mBinderID = data.getExtras().getString("binderID");
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id, Bundle args) {
        switch (id) {
            case DIALOG_USER_PROFILE: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.update_profile);
                LayoutInflater inflater = super.getLayoutInflater();
                final View contentView = inflater.inflate(R.layout.dialog_user_profile, null);
                builder.setView(contentView)
                        .setPositiveButton(R.string.Done, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                EditText etFirstName = (EditText) contentView.findViewById(R.id.et_firstname);
                                EditText etLastName = (EditText) contentView.findViewById(R.id.et_lastname);
                                File file = new File(Environment.getExternalStorageDirectory(), "/Download/001.png");
                                MXSDKConfig.MXProfileInfo profile = new MXSDKConfig.MXProfileInfo(etFirstName.getText().toString(), etLastName.getText().toString(), file.getAbsolutePath());
                                MXAccountManager.getInstance().updateUserProfile(profile);
                            }
                        })
                        .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                return builder.create();
            }

            case DIALOG_INVITE: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.invite_to_chat);
                LayoutInflater inflater = super.getLayoutInflater();
                final View contentView = inflater.inflate(R.layout.dialog_invite_to_chat, null);
                builder.setView(contentView)
                        .setPositiveButton(R.string.Done, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                EditText etUniqueID = (EditText) contentView.findViewById(R.id.et_unique_id);
                                List<String> uniqueIds = new ArrayList<String>();
//                                Map<String, List<String>> orgUniqueIdMap = new HashMap<String, List<String>>();
//                                uniqueIds.add(etUniqueID.getText().toString());
//                                orgUniqueIdMap.put("Pg3dAcMahyYFJDE4HyfFZw6", uniqueIds);
                                //MXChatManager.getInstance(). inviteByUniqueIdsWithOrgId(mBinderID, orgUniqueIdMap,
                                MXChatManager.getInstance().inviteByUniqueIds(mBinderID, uniqueIds, new MXChatManager.OnInviteListener() {
                                    @Override
                                    public void onInviteSuccess() {

                                    }

                                    @Override
                                    public void onInviteFailed(int errorCode, String message) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                return builder.create();
            }

            case DIALOG_OPEN_INDIVIDUAL_CHAT: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.invite_to_chat);
                LayoutInflater inflater = super.getLayoutInflater();
                final View contentView = inflater.inflate(R.layout.dialog_invite_to_chat, null);
                builder.setView(contentView)
                        .setPositiveButton(R.string.Done, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                EditText etUniqueID = (EditText) contentView.findViewById(R.id.et_unique_id);
                                try {
                                    MXChatManager.getInstance().createIndividualChat(etUniqueID.getText().toString(), new MXChatManager.OnCreateChatListener() {
                                        @Override
                                        public void onCreateChatSuccess(String s) {
                                            Log.i(TAG, "Create one on one chat success with id: " + s);
                                        }

                                        @Override
                                        public void onCreateChatFailed(int i, String s) {
                                            Toast.makeText(Phone_MainActivity.this, "Can't create chat: " + s, Toast.LENGTH_LONG).show();
                                            Log.e(TAG, "Create one on one chat failed with error msg: " + s);
                                        }
                                    });
                                } catch (AccountManagerIsNotValid accountManagerIsNotValid) {
                                    accountManagerIsNotValid.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                return builder.create();
            }

            case DIALOG_JOIN_MEET: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.join_a_meet);
                LayoutInflater inflater = super.getLayoutInflater();
                final View contentView = inflater.inflate(R.layout.dialog_invite_to_chat, null);
                final EditText et = (EditText) contentView.findViewById(R.id.et_unique_id);
                et.setHint("Input Meet ID");
                builder.setView(contentView)
                        .setPositiveButton(R.string.Done, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                String meetId = et.getText().toString();
                                if (meetId.length() > 0)
                                    try {
                                        MXChatManager.getInstance().joinMeet(meetId, "WhoAmI", new MXChatManager.OnJoinMeetListener() {

                                            @Override
                                            public void onJoinMeetDone(String meetId, String meetUrl) {
                                                Toast.makeText(getApplicationContext(), "Join Meet Done, Meet Id:" + meetId + "/MeetUrl:" + meetUrl, Toast.LENGTH_LONG).show();
                                            }

                                            @Override
                                            public void onJoinMeetFailed() {
                                                Toast.makeText(getApplicationContext(), "Join Meet failed ...", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    } catch (MXSDKException.MeetIsInProgress e) {

                                    }
                                else
                                    Toast.makeText(getApplicationContext(), "Input Meet Id...", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                return builder.create();
            }

            case DIALOG_OPEN_CHAT: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.open_chat);
                LayoutInflater inflater = super.getLayoutInflater();
                final View contentView = inflater.inflate(R.layout.dialog_invite_to_chat, null);
                final EditText et = (EditText) contentView.findViewById(R.id.et_unique_id);
                et.setHint("Input Chat/Binder ID");
                builder.setView(contentView)
                        .setPositiveButton(R.string.Done, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                String binderId = et.getText().toString();
                                if (binderId.length() > 0)
                                    openChatWithBinderId(binderId);
                                else
                                    Toast.makeText(getApplicationContext(), "Input Chat/Binder ID...", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                return builder.create();
            }
        }
        return super.onCreateDialog(id, args);
    }

    public void startMeetButtonPressed(View v) {
        try {
            MXChatManager.getInstance().startMeet("No Topic...", null, null, new MXChatManager.OnStartMeetListener() {

                @Override
                public void onStartMeetDone(String meetId, String meetUrl) {
                    Toast.makeText(getApplicationContext(), "Start Meet Done, Meet Id:" + meetId + "/MeetUrl:" + meetUrl, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onStartMeetFailed(int errCode, String errMsg) {
                    Toast.makeText(getApplicationContext(), "Start Meet failed, ErrorCode:" + errCode + "/ErrorMessage:" + errMsg, Toast.LENGTH_LONG).show();
                }
            });
        } catch (MXSDKException.Unauthorized unauthorized) {
            unauthorized.printStackTrace();
        } catch (MXSDKException.MeetIsInProgress meetIsInProgress) {
            meetIsInProgress.printStackTrace();
        }
    }

    public void joinMeetButtonPressed(View v) {
        showDialog(DIALOG_JOIN_MEET);
    }

    public void inviteByUniqueButtonPressed(View v) {
        super.showDialog(DIALOG_INVITE);
    }

    public void setupButtonPressed(View v) {
        super.showDialog(DIALOG_USER_PROFILE);
    }

    public void createChatButtonPressed(View v) {
        if (TextUtils.isEmpty(mBinderID)) {
            Intent i = new Intent(this, InviteToGroupChatActivity.class);
            super.startActivityForResult(i, REQUEST_CREATE_GROUP_CHAT);
        } else {
            try {
                MXChatManager.getInstance().openChat(mBinderID, null);
            } catch (AccountManagerIsNotValid e) {
                e.printStackTrace();
            }
        }
    }

    public void createIndividualButtonPressed(View v) {
        super.showDialog(DIALOG_OPEN_INDIVIDUAL_CHAT);
    }

    public void deleteChatButtonPressed(View v) {
        MXChatManager.getInstance().deleteChat(mBinderID);
        mBinderID = null;
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
                    mBinderID = null;
                    Toast.makeText(getApplicationContext(), getString(R.string.unlink_success), Toast.LENGTH_LONG).show();
                    MXChatManager.getInstance().unlink();
                    // jump to login activity
                    Intent intent = new Intent(Phone_MainActivity.this, LoginActivity.class);
                    Phone_MainActivity.this.startActivity(intent);
                    Phone_MainActivity.this.finish();
                }
            });
        }
    }

    public void setupAccountManagerButtonPressed(View v) {
        init();
    }

    public void openChatButtonClicked(View v) {
        showDialog(DIALOG_OPEN_CHAT);
    }

    private void openChatWithBinderId(final String binderId) {
        try {
            MXChatManager.getInstance().openChat(binderId, new MXChatManager.OnOpenChatListener() {

                @Override
                public void onOpenChatSuccess() {
                    Toast.makeText(Phone_MainActivity.this, "open binder successfully!", Toast.LENGTH_SHORT).show();
                    mBinderID = binderId;
                }

                @Override
                public void onOpenChatFailed(int resultCode, String resultStr) {
                    switch (resultCode) {
                        case MXConversationActivity.OpenChatResultCallback.BINDER_ID_NOT_FOUND:
                            Toast.makeText(Phone_MainActivity.this, resultStr, Toast.LENGTH_SHORT).show();
                            break;
                        case MXConversationActivity.OpenChatResultCallback.BINDER_ID_NULL:
                            Toast.makeText(Phone_MainActivity.this, resultStr, Toast.LENGTH_SHORT).show();
                            break;
                        case MXConversationActivity.OpenChatResultCallback.USER_NOT_LOGIN:
                            Toast.makeText(Phone_MainActivity.this, resultStr, Toast.LENGTH_SHORT).show();
                            break;
                        case MXConversationActivity.OpenChatResultCallback.USER_OFFLINE:
                            Toast.makeText(Phone_MainActivity.this, resultStr, Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            if (resultStr != null)
                                Toast.makeText(Phone_MainActivity.this, resultStr, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        } catch (AccountManagerIsNotValid e) {
            e.printStackTrace();
        }
    }

    public void getAllMembersInBinder(View v) {
        if (!TextUtils.isEmpty(mBinderID)) {
            MXChatManager.getInstance().getChatMembers(mBinderID, new MXChatManager.OnGetChatMembersListener() {
                @Override
                public void onGetChatMembersDone(ArrayList<String> users) {
                    showUsers(users);
                }

                @Override
                public void onGetChatMembersFailed(int code, String msg) {
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "For test, there is no binder created...", Toast.LENGTH_SHORT).show();
        }

    }

    public void getMeetMembers(View v){
        MXChatManager.getInstance().getMeetMembers(new MXChatManager.OnGetMeetMembersListener(){

            @Override
            public void onGetMeetMembersDone(ArrayList<MXGroupMeetMember> members) {
                ArrayList<String> strings = new ArrayList<String>();
                for(MXGroupMeetMember member:members){
                    strings.add(member.getUniqueId() + "(" + member.getStatus().name() + ")");
                }
                showUsers(strings);
            }

            @Override
            public void onGetMeetMembersFailed(int i, String s) {

            }
        });
    }

    public void getAllChatSessions(View v) {
        Intent intent = new Intent(this, BinderListFragmentActvitiy.class);
        intent.putExtra(BinderListFragment.GET_BINDERS_TYPE, BinderListFragment.ALL_CHAT_BINDERS);
        startActivity(intent);
    }

    public void inviteToMeet(View v){
//        List<String> uniqueIds = new ArrayList<String>();
//        Map<String, List<String>> orgUniqueIdMap = new HashMap<String, List<String>>();
//        uniqueIds.add("kindle");
//        orgUniqueIdMap.put("Pg3dAcMahyYFJDE4HyfFZw6", uniqueIds);
//        MXChatManager.getInstance().inviteParticipants(orgUniqueIdMap, "invite 2 meet!");
    }

    public void chatSettings(View v){
        Intent intent = new Intent(this, ChatManagerSettingsActivity.class);
        startActivity(intent);
    }

    public static void openChat(final String binderId) {
        try {
            if (TextUtils.isEmpty(binderId))
                return;
            MXChatManager.getInstance().openChat(binderId, new MXChatManager.OnOpenChatListener() {
                @Override
                public void onOpenChatSuccess() {
                    mBinderID = binderId;
                }

                @Override
                public void onOpenChatFailed(int i, String s) {

                }
            });
        } catch (MXException.AccountManagerIsNotValid accountManagerIsNotValid1) {
            accountManagerIsNotValid1.printStackTrace();
        }
    }

    public static void joinMeet(String meetId) {
        try {
            if (TextUtils.isEmpty(meetId))
                return;

            MXChatManager.getInstance().joinMeet(meetId, "user", new MXChatManager.OnJoinMeetListener() {
                @Override
                public void onJoinMeetDone(String s, String s2) {

                }

                @Override
                public void onJoinMeetFailed() {

                }
            });
        } catch (MXSDKException.MeetIsInProgress meetIsInProgress) {
            meetIsInProgress.printStackTrace();
        }
    }

    @Override
    public void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent");
        MXNotificationManager.processMXNotification(this,intent);
    }

    public static void setCurrentBinderId(String binderId){
        mBinderID = binderId;
    }

    public static String getCurrentBinderId(){
       return mBinderID;
    }

    private void showUsers(ArrayList<String> users){
        String allUsers;
        if(users.size() > 1) {
            StringBuffer sb = new StringBuffer();
            for(String user:users){
                sb.append(user+"/");
            }
            allUsers = sb.toString();
        }
        else if(users.size() == 1)
            allUsers = users.get(0);
        else
            allUsers = "No Users in Binder!";

        AlertDialog.Builder builder = new AlertDialog.Builder(Phone_MainActivity.this);
        builder.setTitle("BinderMembers").setMessage(allUsers).setPositiveButton("OK",null);
        builder.show();
    }

    public void openMessagePage(View v){
        Intent intent = new Intent();
        intent.setClass(this, MsgListFragmentActvitiy.class);
        startActivity(intent);
    }
}
