package edu.self.ludus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class CoachProfileInfo extends AppCompatActivity {

    private String mName;
    private String mSport;
    private String mLocation;
    private String mPhoneNumber;
    private String mEmail;
    private String mUserName;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_profile_info);

        intent = getIntent();

        TextView nameUser = (TextView)findViewById(R.id.user_name);
        TextView userSport = (TextView)findViewById(R.id.user_sport);
        TextView userLocation = (TextView)findViewById(R.id.location);
        TextView userPhoneNumber = (TextView)findViewById(R.id.user_phone_number);
        TextView userEmail = (TextView)findViewById(R.id.email);
        TextView userName = (TextView)findViewById(R.id.username);

        mName = intent.getStringExtra("Name");
        mSport = intent.getStringExtra("Sport");
        mLocation = intent.getStringExtra("City");
        mPhoneNumber = intent.getStringExtra("PhoneNumber");
        mEmail = intent.getStringExtra("Email");
        mUserName = intent.getStringExtra("Username");

        Log.d("StudentProfile", "name is " + mName);

        nameUser.setText(mName);
        userSport.setText(mSport);
        userLocation.setText(mLocation);
        userPhoneNumber.setText(mPhoneNumber);
        userEmail.setText(mEmail);
        userName.setText(mUserName);
    }
}
