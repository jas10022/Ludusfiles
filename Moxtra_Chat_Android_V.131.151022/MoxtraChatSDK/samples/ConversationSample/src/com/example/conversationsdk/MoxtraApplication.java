package com.example.conversationsdk;

import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.example.conversationsdk.activity.LoginActivity;
import com.moxtra.binder.SDKConstant;
import com.moxtra.sdk.MXAccountManager;
import com.moxtra.sdk.MXSDKException;

/**
 * Created by Breeze on 14/10/24.
 */
public class MoxtraApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MoxtraApplication", "onCreate");

        CustomizeUISettings.setUISettings();
        //Must be initialize before any activity starts if use asynchronous mode to initialize MXAccountManager .
//        MXApplicationManager.getInstance().initialize(getApplicationContext());
        int mode = PreferenceUtils.getInstance(this).getInt(LoginActivity.PERFERENCE_MXACCOUNTMANAGER_INITIAL_TYPE, LoginActivity.SYNC_MODE);
        try {
            if (mode == LoginActivity.ASYNC_MODE) {
                MXAccountManager.createInstance(getApplicationContext(), "uNisxXhvxZI", "gAUoJMpSE38", true, null);
            } else {
                MXAccountManager.createInstance(getApplicationContext(), "uNisxXhvxZI", "gAUoJMpSE38", true);
            }
        } catch (MXSDKException.InvalidParameter invalidParameter) {
            invalidParameter.printStackTrace();
        }

        Log.d("MoxtraApplication", "onCreate end");
    }
}
