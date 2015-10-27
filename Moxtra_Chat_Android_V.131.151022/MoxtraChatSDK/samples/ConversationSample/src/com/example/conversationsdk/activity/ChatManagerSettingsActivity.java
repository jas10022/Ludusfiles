package com.example.conversationsdk.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.conversationsdk.R;
import com.moxtra.binder.SDKFeatureConfig;
import com.moxtra.binder.sdk.InviteToChatCallback;
import com.moxtra.binder.sdk.InviteToMeetCallback;
import com.moxtra.binder.sdk.ShareResourceCallback;
import com.moxtra.binder.sdk.UserProfileCallback;
import com.moxtra.sdk.MXChatCustomizer;
import com.moxtra.sdk.MXChatManager;
import com.moxtra.sdk.MXGroupChatSession;
import com.moxtra.sdk.MXGroupChatSessionCallback;


public class ChatManagerSettingsActivity extends Activity implements View.OnClickListener,CheckBox.OnCheckedChangeListener {
    private CheckBox addFileCallback_cb;
    private CheckBox inviteToChatCallback_cb;
    private CheckBox inviteToMeetCallback_cb;
    private CheckBox moreButtonCallback_cb;
    private CheckBox shareResourceCallback_cb;
    private CheckBox userProfileCallback_cb;
    private Button meetEndListenerSetbtn;
    private Button meetEndListenerClearbtn;
    private Button groupSessionChangessetbtn;
    private Button groupSessionChangesClearbtn;
    private Button emailCustomizationBtn;

    private static final String TAG = "ChatManagerSettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_manager_settings);
        addFileCallback_cb = (CheckBox) findViewById(R.id.add_file_callback_cb);
        inviteToChatCallback_cb = (CheckBox) findViewById(R.id.invite_to_chat_callback_cb);
        inviteToMeetCallback_cb = (CheckBox) findViewById(R.id.invite_to_meet_callback_cb);
        moreButtonCallback_cb = (CheckBox) findViewById(R.id.more_button_clicked_callback_cb);
        shareResourceCallback_cb = (CheckBox) findViewById(R.id.share_resource_callback_cb);
        userProfileCallback_cb = (CheckBox) findViewById(R.id.user_profile_callback_cb);
         meetEndListenerSetbtn = (Button)findViewById(R.id.meet_end_callback_set);
         meetEndListenerClearbtn= (Button)findViewById(R.id.meet_end_callback_clear) ;
        groupSessionChangessetbtn= (Button)findViewById(R.id.group_session_callback_set);
        groupSessionChangesClearbtn= (Button)findViewById(R.id.group_session_callback_clear);
        emailCustomizationBtn = (Button)findViewById(R.id.email_customization_btn);

        addFileCallback_cb.setChecked(SDKFeatureConfig.getInstance().getAddFileBtnClickedListener() != null);
        inviteToChatCallback_cb.setChecked(SDKFeatureConfig.getInstance().getInviteToChatCallback() != null);
        inviteToMeetCallback_cb.setChecked(SDKFeatureConfig.getInstance().getInviteToMeetCallback() != null);
        moreButtonCallback_cb.setChecked(SDKFeatureConfig.getInstance().getMoreButtonClickListener() != null);
        shareResourceCallback_cb.setChecked(SDKFeatureConfig.getInstance().getShareResourceCallback() != null);
        userProfileCallback_cb.setChecked(SDKFeatureConfig.getInstance().getUserProfileCallback() != null);

        addFileCallback_cb.setOnCheckedChangeListener(this);
        inviteToMeetCallback_cb.setOnCheckedChangeListener(this);
        inviteToChatCallback_cb.setOnCheckedChangeListener(this);
        moreButtonCallback_cb.setOnCheckedChangeListener(this);
        shareResourceCallback_cb.setOnCheckedChangeListener(this);
        userProfileCallback_cb.setOnCheckedChangeListener(this);

        meetEndListenerSetbtn.setOnClickListener(this);
        meetEndListenerClearbtn.setOnClickListener(this);

        if(MXChatCustomizer.isForceTablet()) {
            groupSessionChangessetbtn.setVisibility(View.GONE);
            groupSessionChangesClearbtn.setVisibility(View.GONE);
        }
        groupSessionChangessetbtn.setOnClickListener(this);
        groupSessionChangesClearbtn.setOnClickListener(this);

        emailCustomizationBtn.setOnClickListener(this);
        emailCustomizationBtn.setVisibility(View.GONE);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        buttonView.setChecked(isChecked);
        switch (buttonView.getId()) {
            case R.id.add_file_callback_cb:
                MXChatManager.getInstance().setAddFileBtnClickedListener(isChecked ? new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), "Open your activity and upload file here~~~", Toast.LENGTH_SHORT).show();
                        showInfoDialog("Upload file action~~~");
                    }
                } : null);
                break;
            case R.id.invite_to_chat_callback_cb:
                MXChatManager.getInstance().setChatInviteCallback(isChecked ? new InviteToChatCallback() {
                    @Override
                    public void onInviteToChat(String binderID, Bundle extras) {
                        if (TextUtils.isEmpty(binderID)) {
                            // means that create binder
                        } else {
                            Intent i = new Intent(ChatManagerSettingsActivity.this, InviteToGroupChatActivity.class);
                            i.putExtra("inviteToChat", true);
                            i.putExtra("binderID", binderID);
                            ChatManagerSettingsActivity.this.startActivity(i);
                        }
                    }
                } : null);
                break;
            case R.id.invite_to_meet_callback_cb:
                MXChatManager.getInstance().setMeetInviteCallback(isChecked ? new InviteToMeetCallback() {
                    @Override
                    public void onInviteToMeet(String meetId, String url, Bundle extras) {
                        Intent i = new Intent(ChatManagerSettingsActivity.this, InviteToGroupChatActivity.class);
                        i.putExtra("inviteToMeet", true);
                        ChatManagerSettingsActivity.this.startActivity(i);
                    }
                } : null);
                break;
            case R.id.more_button_clicked_callback_cb:
                MXChatManager.getInstance().setMoreButtonClickListener(isChecked ? new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "onClick(), callback from Moxtra!!");
                        showInfoDialog("More Button Callback invoked!");
                    }
                } : null);
                break;
            case R.id.share_resource_callback_cb:
                MXChatManager.getInstance().setShareResourceCallbackListener(isChecked ? new ShareResourceCallback() {
                    @Override
                    public void shareResource(String url, String downloadUrl) {
                        Toast.makeText(getApplicationContext(), "url is:" + url, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "DownloadUrl is:" + downloadUrl, Toast.LENGTH_SHORT).show();
                        showInfoDialog("Download URL:" + downloadUrl );
                    }

                    @Override
                    public void shareResourceFailed(int errorCode, String errorMsg) {
                        Toast.makeText(getApplicationContext(), "ErrorCode :" + errorCode + "/ErrorMsg:" + errorMsg, Toast.LENGTH_SHORT).show();
                        showInfoDialog("Error code:" + errorCode + "/ErrorMsg: " + errorMsg );
                    }
                } : null);
                break;
            case R.id.user_profile_callback_cb:
                MXChatManager.getInstance().setUserProfileCallback(isChecked ? new UserProfileCallback() {
                    @Override
                    public void openUserProfile(String uniqueId, Bundle args) {
                        Log.d(TAG, "openUserProfile(), uniqueId = " + uniqueId + ", args = " + args);
                        showInfoDialog("openUserProfile(), uniqueId = " + uniqueId + ", args = " + args);
                    }
                } : null);
                break;
        }
    }

    private void showInfoDialog(String info){
        AlertDialog.Builder builder = new AlertDialog.Builder(MXChatManager.getInstance(this).getContext());
        builder.setMessage(info).setIcon(R.drawable.ic_launcher).setPositiveButton("OK", null).setTitle("Moxtra");
        builder.show();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.meet_end_callback_set:
                MXChatManager.getInstance().setOnMeetEndListener(new MXChatManager.OnEndMeetListener() {
                    @Override
                    public void onMeetEnded(String meetId) {
                        Toast.makeText(getApplicationContext(), "Meet ended:" + meetId, Toast.LENGTH_SHORT).show();
                        showInfoDialog("Meet ended:" + meetId);
                    }
                });
                break;
            case R.id.meet_end_callback_clear:
                MXChatManager.getInstance().setOnMeetEndListener(null);
                break;
            case R.id.group_session_callback_set:
                MXChatManager.getInstance().setGroupChatSessionCallback(new MXGroupChatSessionCallback() {
                    @Override
                    public void onGroupChatSessionCreated(MXGroupChatSession session) {
                        Log.d(TAG, "onGroupChatSessionCreated(), session = " + session);
                    }

                    @Override
                    public void onGroupChatSessionUpdated(MXGroupChatSession session) {
                        Log.d(TAG, "onGroupChatSessionUpdated(), session = " + session);
                    }

                    @Override
                    public void onGroupChatSessionDeleted(MXGroupChatSession session) {
                        Log.d(TAG, "onGroupChatSessionDeleted(), session = " + session);
                    }
                });
                break;
            case R.id.group_session_callback_clear:
                MXChatManager.getInstance().setGroupChatSessionCallback(null);
                break;

            //DO NOT use this feature, it may cause sending email to an exist binder failed.
//            case R.id.email_customization_btn:
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle(R.string.customize_email_host);
//                LayoutInflater inflater = super.getLayoutInflater();
//                final View contentView = inflater.inflate(R.layout.dialog_invite_to_chat, null);
//                final EditText etUniqueID = (EditText) contentView.findViewById(R.id.et_unique_id);
//                etUniqueID.setHint(R.string.customize_email_host_hint);
//                builder.setView(contentView)
//                        .setPositiveButton(R.string.Done, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int id) {
//                                String host = etUniqueID.getText().toString().toLowerCase().trim();
//                                if(TextUtils.isEmpty(host)){
//                                    Toast.makeText(ChatManagerSettingsActivity.this, "Host can Not be empty!", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                MXChatManager.getInstance().setBinderEmailHost(host);
//                            }
//                        });
//                builder.show();
//                break;
        }
    }
}
