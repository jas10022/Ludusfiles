package edu.self.ludus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class CoachProfile extends AppCompatActivity {

    private Firebase user;    private Intent intentextra;
    public static String city;
    public static String name;
    public static String phoneNumber;
    public static String sport;
    public static String email;
    public static String username;
    public static String id;
    public static String password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_coach_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = new Firebase("https://mytennis.firebaseio.com/" + LoginPage.mPassword);

        intentextra = getIntent();

        username = intentextra.getStringExtra("Username");

        city = intentextra.getStringExtra("City");
        name = intentextra.getStringExtra("Name");
        phoneNumber = intentextra.getStringExtra("PhoneNumber");
        sport = intentextra.getStringExtra("Sport");
        email = intentextra.getStringExtra("Email");
        id = intentextra.getStringExtra("ID");
        password = intentextra.getStringExtra("Password");

        Button studentsButton = (Button)findViewById(R.id.student_button);
        Button profileButton = (Button)findViewById(R.id.profile_button);
        Button calenderButton = (Button)findViewById(R.id.calender_button);
        Button connectButton = (Button)findViewById(R.id.connect_button);
        Button learnButton = (Button)findViewById(R.id.learn_button);
        TextView welcome_user = (TextView)findViewById(R.id.title);


        welcome_user.setText("Welcome " + username);

        studentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CoachProfile.this , Coaches_Students.class);

                startActivity(i);
            }
        });
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CoachProfile.this,CoachProfileInfo.class);
                i.putExtra("City",city);
                i.putExtra("Name",name);
                i.putExtra("PhoneNumber",phoneNumber);
                i.putExtra("Sport",sport);
                i.putExtra("Email", email);
                i.putExtra("Username",username);
                startActivity(i);
            }
        });
        calenderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        learnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}
