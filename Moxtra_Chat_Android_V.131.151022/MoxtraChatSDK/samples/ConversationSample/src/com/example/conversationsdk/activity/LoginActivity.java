package com.example.conversationsdk.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.conversationsdk.PreferenceUtils;
import com.example.conversationsdk.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.moxtra.sdk.MXAccountManager;
import com.moxtra.sdk.MXChatManager;
import com.moxtra.sdk.MXSDKConfig;
import com.moxtra.sdk.MXSDKException;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Breeze on 14/12/18.
 */
public class LoginActivity extends FragmentActivity {
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    String SENDER_ID = "your_senderID";

    public static final String PERFERENCE_UI_IS_TABLET = "perference_ui_is_tablet";
    public static final String PERFERENCE_MXACCOUNTMANAGER_INITIAL_TYPE = "perference_mxaccountmgr_initial_type";
    public static final int ASYNC_MODE = 0;
    public static final int SYNC_MODE = 1;

    private static final String TAG = "LoginActivity";
    private MXAccountManager mAccountMgr;
    protected ProgressDialog pb;
    private GoogleCloudMessaging gcm;
    private AtomicInteger msgId = new AtomicInteger();
    private Context context;
    private RadioGroup radioGroup;

    private String regid = null;

    public static boolean USE_GCM = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        super.setContentView(R.layout.activity_login);
        context = this;
        pb = new ProgressDialog(this);
        pb.setMessage("Waiting for initialization...");
        pb.setCancelable(false);
        radioGroup = (RadioGroup)findViewById(R.id.mxaccount_initilize_radio_group);
        int mode = PreferenceUtils.getInstance(this).getInt(PERFERENCE_MXACCOUNTMANAGER_INITIAL_TYPE, SYNC_MODE);
        if(mode == SYNC_MODE){
            ((RadioButton)findViewById(R.id.mxaccount_initilize_sync_mode)).setChecked(true);
        }else{
            ((RadioButton)findViewById(R.id.mxaccount_initilize_async_mode)).setChecked(true);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.mxaccount_initilize_async_mode:
                        PreferenceUtils.getInstance(getApplicationContext()).saveInt(PERFERENCE_MXACCOUNTMANAGER_INITIAL_TYPE, ASYNC_MODE);
                        break;
                    case R.id.mxaccount_initilize_sync_mode:
                        PreferenceUtils.getInstance(getApplicationContext()).saveInt(PERFERENCE_MXACCOUNTMANAGER_INITIAL_TYPE, SYNC_MODE);
                        break;
                }
            }
        });

//        initializeAccountManager();
//        Log.d(TAG, "The registrationId = " + getRegistrationId(this));
        mAccountMgr = MXAccountManager.getInstance();
        if(!mAccountMgr.isInitialized()){
            pb.show();
            try {
                mAccountMgr.initialize(new MXAccountManager.MXAccountInitializeListener() {
                    @Override
                    public void onInitializeAccountDone(boolean b) {
                        pb.dismiss();
                        decideUIStyle();
                    }
                });
            } catch (MXSDKException.InvalidParameter invalidParameter) {
                invalidParameter.printStackTrace();
            }
        }
        else
            decideUIStyle();

        // Check device for Play Services APK. If check succeeds, proceed with GCM registration.
        if (USE_GCM) {
            if (checkPlayServices()) {
                gcm = GoogleCloudMessaging.getInstance(this);
                regid = getRegistrationId(this);

                if (regid.isEmpty()) {
                    registerInBackground();
                }
            } else {
                Log.i(TAG, "No valid Google Play Services APK found.");
            }
        }
    }

    private void decideUIStyle() {
        if (mAccountMgr.isLinked()) {
            int isTablet = PreferenceUtils.getInstance(this).getInt(PERFERENCE_UI_IS_TABLET,-1);
            Log.d(TAG, "onCreate isTablet=" + isTablet);
            if(isTablet == -1) {
                showDialog();
            }else{
                showMainActivity(isTablet == 1,getIntent());
            }
        }
    }


    //added by Jeffery
    @Override
    public void onNewIntent(Intent i ){
        super.onNewIntent(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        // Check device for Play Services APK.
//        checkPlayServices();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }

    private void initializeAccountManager() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pb.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    mAccountMgr = MXAccountManager.createInstance(getApplicationContext());
                } catch (MXSDKException.InvalidParameter invalidParameter) {
                    invalidParameter.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (pb != null)
                    pb.dismiss();
                if (mAccountMgr.isLinked()) {
                   showDialog();
                    return;
                }
            }
        }.execute();
    }

    public void loginButtonPressed(View view) {
        EditText etUniqueId = (EditText) super.findViewById(R.id.et_unique_id);
        final MXSDKConfig.MXUserInfo user = new MXSDKConfig.MXUserInfo(etUniqueId.getText().toString(), MXSDKConfig.MXUserIdentityType.IdentityUniqueId);
        mAccountMgr.setupUser(user, null, null, regid, new MXAccountManager.MXAccountLinkListener() {
            @Override
            public void onLinkAccountDone(boolean bSuccess) {
                Log.d(TAG, "onLinkAccountDone(), logged-in user: " + mAccountMgr.getUserInfo());
                if (bSuccess) {
                    showDialog();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.login_failed), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void ssoLoginButtonPressed(View v) {
        final MXSDKConfig.MXUserInfo user = new MXSDKConfig.MXUserInfo("your_access_token", MXSDKConfig.MXUserIdentityType.IdentityTypeSSOAccessToken);
        mAccountMgr.setupUser(user, null, null, new MXAccountManager.MXAccountLinkListener() {
            @Override
            public void onLinkAccountDone(boolean bSuccess) {
                if (bSuccess) {
                    showDialog();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.login_failed), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void joinMeetWithoutLogin(View v){
        EditText etMeetID = (EditText) findViewById(R.id.et_meet_id);
        String meetId = etMeetID.getText().toString();
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
	
	    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGcmPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(LoginActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }
    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app. Not needed for this demo since the device sends upstream messages
     * to a server that echoes back the message using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() {
        // Your implementation here.
        MXAccountManager.getInstance().updateDeviceToken(regid);
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pb.setMessage("Register with GCM server");
                pb.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                pb.dismiss();
            }
        }.execute(null, null, null);
    }

    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    private void showDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Login Result")
                .setMessage("Login success, please choose UI mode")
                .setPositiveButton("Tablet", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PreferenceUtils.getInstance(getApplicationContext()).saveInt(PERFERENCE_UI_IS_TABLET, 1);
                        showMainActivity(true,getIntent());
                    }
                }).setNegativeButton("Phone", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PreferenceUtils.getInstance(getApplicationContext()).saveInt(PERFERENCE_UI_IS_TABLET, 0);
                showMainActivity(false,getIntent());
            }
        });
        builder.create().show();
    }

    private void showMainActivity(boolean isTablet,Intent i){
        Log.d(TAG,"showMainActivity");
        Intent intent = new Intent(LoginActivity.this, isTablet?Tablet_MainActivity.class:Phone_MainActivity.class);
        if(i != null)
            intent.putExtras(i);
        startActivity(intent);
        finish();
    }

}
