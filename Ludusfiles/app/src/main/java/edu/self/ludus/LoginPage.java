package edu.self.ludus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class LoginPage extends AppCompatActivity {

    private EditText mPasswordInput;
    private EditText mEmailInput;
    private String mPassword;
    private String mUsername;
    private Firebase myFirebaseRef;
    private String ProfileType;
    public static String username;
    public static String city;
    public static String name;
    public static String phoneNumber;
    public static String sport;
    public static String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        Firebase.setAndroidContext(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myFirebaseRef = new Firebase("https://mytennis.firebaseio.com/");

        mPasswordInput = (EditText) findViewById(R.id.password_input);
        mEmailInput = (EditText) findViewById(R.id.email_input);
        final Button loginButton = (Button) findViewById(R.id.login);
        Button forgotPasswordButton = (Button) findViewById(R.id.forgot_password);




        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPassword = mPasswordInput.getText().toString();
                mUsername = mEmailInput.getText().toString();

                myFirebaseRef.authWithPassword(mUsername, mPassword, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Log.d("LoginPage", "worked");

                        Firebase user = myFirebaseRef;
                        myFirebaseRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ProfileType = dataSnapshot.child(mPassword).child("ProfileType").getValue().toString();
                                username = dataSnapshot.child(mPassword).child("Username").getValue().toString();
                                city = dataSnapshot.child(mPassword).child("Username").getValue().toString();
                                name = dataSnapshot.child(mPassword).child("Username").getValue().toString();
                                phoneNumber = dataSnapshot.child(mPassword).child("Username").getValue().toString();
                                sport = dataSnapshot.child(mPassword).child("Username").getValue().toString();
                                email = dataSnapshot.child(mPassword).child("Username").getValue().toString();

                                Log.d("LoginPage", ProfileType);

                                if (ProfileType.equals("Student")) {
                                    Intent i = new Intent(LoginPage.this, StudentHomePage.class);
                                    startActivity(i);
                                }else if (ProfileType.equals("Coach")) {
                                    Intent a = new Intent(LoginPage.this, CoachProfile.class);
                                    startActivity(a);
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });

                        String username = user.child(mPassword).getKey().toString();



                        Log.d("LoginPage", username);

                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Log.d("LoginPage","not");
                    }
                });

            }

        });
    }
}
