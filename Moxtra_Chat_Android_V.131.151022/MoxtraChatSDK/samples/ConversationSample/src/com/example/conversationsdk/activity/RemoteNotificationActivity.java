package com.example.conversationsdk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.conversationsdk.PreferenceUtils;
import com.example.conversationsdk.R;
import com.moxtra.sdk.MXAccountManager;

/**
 * Created by jeffery on 6/9/15.
 */
public class RemoteNotificationActivity extends FragmentActivity {

    private final String TAG = RemoteNotificationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        super.setContentView(R.layout.activity_notification);

        if (MXAccountManager.getInstance() != null && MXAccountManager.getInstance().isLinked()) {
            int isTablet = PreferenceUtils.getInstance(this).getInt(LoginActivity.PERFERENCE_UI_IS_TABLET, -1);
            Log.d(TAG, "onCreate isTablet=" + isTablet);
            showMainActivity(isTablet == 1,getIntent());
        }
    }

    private void showMainActivity(boolean isTablet,Intent i){
        Log.d(TAG, "showMainActivity");
        Intent intent = new Intent(this, isTablet?Tablet_MainActivity.class:Phone_MainActivity.class);
        if(i != null) {
            intent.putExtras(i);
            Log.d(TAG,"showMainActivity extras="+i.getExtras());
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }
}
