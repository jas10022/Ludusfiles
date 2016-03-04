package edu.self.ludus;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class StudentHomePage extends ActionBarActivity {

    Button mProfileButton;
    Button mSkillsButton;
    Button mFeedbackButton;
    Button mImportantDatesButton;
    Button mWorkoutsButton;
    Button mGoalsButton;
    private Firebase myFirebaseRef;
    private Intent intentextra;
    private String username;
    private String city;
    private String name;
    private String phoneNumber;
    private String sport;
    private String email;
    private String id;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Firebase.setAndroidContext(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home_page);

        myFirebaseRef = new Firebase("https://mytennis.firebaseio.com/");

        intentextra = getIntent();

        username = intentextra.getStringExtra("Username");

        city = intentextra.getStringExtra("City");
        name = intentextra.getStringExtra("Name");
        phoneNumber = intentextra.getStringExtra("PhoneNumber");
        sport = intentextra.getStringExtra("Sport");
        email = intentextra.getStringExtra("Email");
        id = intentextra.getStringExtra("ID");
        password = intentextra.getStringExtra("Password");

        mProfileButton = (Button)findViewById(R.id.profile_button);
        mSkillsButton = (Button)findViewById(R.id.skills_button);
        mFeedbackButton = (Button)findViewById(R.id.feedback_button);
        mImportantDatesButton = (Button) findViewById(R.id.important_dates_button);
        mWorkoutsButton = (Button) findViewById(R.id.workouts_button);
        mGoalsButton = (Button)findViewById(R.id.goals_button);
        Button findCoaches = (Button)findViewById(R.id.find_coaches);
        TextView welcome_user = (TextView)findViewById(R.id.title);

        welcome_user.setText("Welcome " + username);


        mProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StudentHomePage.this, StudentProfile.class);
                i.putExtra("Username",username);
                i.putExtra("City", city);
                i.putExtra("Name", name);
                i.putExtra("PhoneNumber", phoneNumber);
                i.putExtra("Sport", sport);
                i.putExtra("Email", email);
                startActivity(i);
            }
        });

        mSkillsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StudentHomePage.this, StudentSkills.class);
                startActivity(i);
            }
        });

        mFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StudentHomePage.this, StudentFeedback.class);
                startActivity(i);
            }
        });

        mImportantDatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StudentHomePage.this, StudentImportantDates.class);
                startActivity(i);
            }
        });

        mWorkoutsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StudentHomePage.this, StudentWorkout.class);
                startActivity(i);
            }
        });

        mGoalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StudentHomePage.this, StudentGoals.class);
                startActivity(i);
            }
        });
        findCoaches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StudentHomePage.this,FindCoaches.class);
                i.putExtra("ID", id);
                i.putExtra("Password", password);
                startActivity(i);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_student_home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
