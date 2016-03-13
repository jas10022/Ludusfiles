package edu.self.ludus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class FindCoaches extends AppCompatActivity {

    private EditText mFindCoaches;
    private String mStudentId;
    private String Coach;
    private Intent intentextra;
    private String password;
    public static Firebase coachUser;
    private Firebase myFireBase;
    private String findCoaches;
    public static String coachUsername;
    private Firebase studentUser;
    private String username;
    private String city;
    private String name;
    private String phoneNumber;
    private String sport;
    private String email;
    private Firebase studentUSerid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_find_coaches);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        intentextra = getIntent();
        mStudentId = intentextra.getStringExtra("ID").toString();
        password = intentextra.getStringExtra("Password").toString();
        username = intentextra.getStringExtra("Username").toString();
        ImageView back = (ImageView) findViewById(R.id.btnBack);

        final TextView studentId = (TextView)findViewById(R.id.student_id);
        mFindCoaches = (EditText)findViewById(R.id.add_coach);
        final Button add = (Button)findViewById(R.id.add);

        city = intentextra.getStringExtra("City");
        name = intentextra.getStringExtra("Name");
        phoneNumber = intentextra.getStringExtra("PhoneNumber");
        sport = intentextra.getStringExtra("Sport");
        email = intentextra.getStringExtra("Email");
        password = intentextra.getStringExtra("Password");

        studentId.setText(mStudentId);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FindCoaches.this, StudentHomePage.class);
                if (add.isActivated()) {
                    i.putExtra("CoachID", coachUser.toString());
                } else {
                    i.putExtra("CoachID", "");
                }
                i.putExtra("Password",password);
                startActivity(i);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findCoaches = mFindCoaches.getText().toString();

                studentUser = new Firebase("https://mytennis.firebaseio.com/" + password);
                studentUSerid = new Firebase("https://mytennis.firebaseio.com/" + mStudentId);

                coachUser = new Firebase("https://mytennis.firebaseio.com/" + findCoaches);


                coachUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Coach = dataSnapshot.child("Message").getValue().toString();
                        coachUsername = dataSnapshot.child("Username").getValue().toString();
                        Log.d("FindCoaches", Coach);

                        studentUSerid.child("Coach").setValue(coachUser.toString());
                        studentUser.child("Coach").setValue(coachUser.toString());

                        StudentUserArray studentUserArray = new StudentUserArray();

                        studentUserArray.studentUsersname = new ArrayList<String>();
                        studentUserArray.studentIDcreation = new ArrayList<String>();
                        studentUserArray.studentUsersname.add(username);
                        studentUserArray.studentIDcreation.add("https://mytennis.firebaseio.com/" + mStudentId);


                        coachUser.child("Students").setValue(studentUserArray.studentUsersname);
                        coachUser.child("StudentsID").setValue(studentUserArray.studentIDcreation);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Log.d("FindCoach",firebaseError.toString());
                    }
                });

            }
        });


    }

}
