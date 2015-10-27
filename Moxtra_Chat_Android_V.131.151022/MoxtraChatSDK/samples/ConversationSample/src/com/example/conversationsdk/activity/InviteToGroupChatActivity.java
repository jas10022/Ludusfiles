package com.example.conversationsdk.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.conversationsdk.R;
import com.moxtra.sdk.MXChatManager;
import com.moxtra.sdk.MXException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Breeze on 14/11/24.
 */
public class InviteToGroupChatActivity extends FragmentActivity implements View.OnClickListener {

    private static final String TAG_INVITE_TO_CHAT = "inviteToChat";
    private static final String TAG_INVITE_TO_MEET = "inviteToMeet";
    private static final String TAG_INVITE_TO_NEW_CHAT = "inviteToNewChat";

    private static final String TAG = "InviteToGroupChatActivity";
    private EditText etUniqueId;
    private Button btnInvite;
    private String mBinderID;
    private TextView labelError;
    private EditText etTopic;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_invite_to_group_chat);
        pd = new ProgressDialog(this);
        pd.setMessage("Waiting for result...");
        etUniqueId = (EditText) super.findViewById(R.id.editText);
        btnInvite = (Button) super.findViewById(R.id.btn_invite);
        btnInvite.setOnClickListener(this);
        btnInvite.setTag(TAG_INVITE_TO_NEW_CHAT);
        etTopic = (EditText) super.findViewById(R.id.et_topic);
        etTopic.setVisibility(View.VISIBLE);
        labelError = (TextView) super.findViewById(R.id.label_error_msg);
        labelError.setVisibility(View.GONE);
        Intent i = super.getIntent();
        if (i != null) {
            if (i.hasExtra("inviteToChat")) {
                btnInvite.setTag(TAG_INVITE_TO_CHAT);
                mBinderID = i.getStringExtra("binderID");
                etTopic.setVisibility(View.GONE);
            } else if (i.hasExtra("inviteToMeet")) {
                btnInvite.setTag(TAG_INVITE_TO_MEET);
                etTopic.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        String allUniqueIds = etUniqueId.getText().toString();
        List<String> uniqueIds = new ArrayList<String>();
        if (!TextUtils.isEmpty(allUniqueIds)) {
            String[] arrUniqueId = allUniqueIds.split(";");
            uniqueIds.addAll(Arrays.asList(arrUniqueId));
        }
        String tag = (String) v.getTag();
        if (TAG_INVITE_TO_NEW_CHAT.equals(tag)) {
            String topic = etTopic.getText().toString();
            if (TextUtils.isEmpty(topic)) {
                Toast.makeText(getApplicationContext(), "input a topic!!!", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                MXChatManager.getInstance().createChat(topic, uniqueIds, new MXChatManager.OnCreateChatListener() {
                    @Override
                    public void onCreateChatSuccess(String binderID) {
                        Log.d(TAG, "onCreateChatSuccess(), binderId = " + binderID);
                        Intent data = new Intent();
                        data.putExtra("binderID", binderID);
                        InviteToGroupChatActivity.this.setResult(Activity.RESULT_OK, data);
                        InviteToGroupChatActivity.this.finish();
                    }

                    @Override
                    public void onCreateChatFailed(int errorCode, String message) {
                        Log.d(TAG, "onCreateChatFailed(), errorCode = " + errorCode + ", message = " + message);
                        Intent data = new Intent();
                        data.putExtra("errorCode", errorCode);
                        data.putExtra("message", message);
                        InviteToGroupChatActivity.this.setResult(Activity.RESULT_CANCELED, data);
                        InviteToGroupChatActivity.this.finish();
                    }
                });
            } catch (MXException.AccountManagerIsNotValid e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        } else if (TAG_INVITE_TO_CHAT.equals(tag)) {
            MXChatManager.getInstance(this).inviteByUniqueIds(mBinderID, uniqueIds, new MXChatManager.OnInviteListener() {
                @Override
                public void onInviteSuccess() {
                    Log.d(TAG, "onInviteSuccess()");
                    labelError.setVisibility(View.GONE);
                    InviteToGroupChatActivity.this.finish();
                }

                @Override
                public void onInviteFailed(int errorCode, String message) {
                    labelError.setText(String.format("ERROR!! errorCode = %d, message = %s.", errorCode, message));
                    labelError.setVisibility(View.VISIBLE);
                }
            });
        } else if (TAG_INVITE_TO_MEET.equals(tag)) {
            MXChatManager.getInstance().inviteParticipants(null, uniqueIds, null);
            InviteToGroupChatActivity.this.finish();
        }
    }
}
