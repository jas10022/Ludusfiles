package edu.self.ludus;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;

public class CoachProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ParseUser currentUser = ParseUser.getCurrentUser();


        Button studentsButton = (Button)findViewById(R.id.student_button);
        Button profileButton = (Button)findViewById(R.id.profile_button);
        Button calenderButton = (Button)findViewById(R.id.calender_button);
        Button connectButton = (Button)findViewById(R.id.connect_button);
        Button learnButton = (Button)findViewById(R.id.learn_button);
        TextView welcome_user = (TextView)findViewById(R.id.title);

        String Username = currentUser.get("username").toString();

        welcome_user.setText("Welcome " + Username);

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
