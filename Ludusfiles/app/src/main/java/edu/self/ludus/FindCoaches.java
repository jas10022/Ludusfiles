package edu.self.ludus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

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

        final TextView studentId = (TextView)findViewById(R.id.student_id);
        mFindCoaches = (EditText)findViewById(R.id.add_coach);
        Button add = (Button)findViewById(R.id.add);



        studentId.setText(mStudentId);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findCoaches = mFindCoaches.getText().toString();

                studentUser = new Firebase("https://mytennis.firebaseio.com/" + password);

                coachUser = new Firebase("https://mytennis.firebaseio.com/" + findCoaches);


                coachUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Coach = dataSnapshot.child("Message").getValue().toString();
                        coachUsername = dataSnapshot.child("Username").getValue().toString();
                        Log.d("FindCoaches", Coach);

                        studentUser.child("Coach").setValue(findCoaches);

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }
        });


    }

}
