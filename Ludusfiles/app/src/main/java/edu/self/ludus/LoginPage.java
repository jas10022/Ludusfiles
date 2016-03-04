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
    public static String mPassword;
    private String mUsername;
    private Firebase myFirebaseRef;
    private String ProfileType;
    public static boolean loginScene2;
    public static String id;
    private Firebase user;
    public static String city;
    public static String name;
    public static String phoneNumber;
    public static String sport;
    public static String email;
    public static String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        Firebase.setAndroidContext(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loginScene2 = true;
        if (SignUp.loginScene == true){
            loginScene2 = false;
        }else if(SignUp.loginScene == false){
            loginScene2 = true;
        }

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

                        user = new Firebase("https://mytennis.firebaseio.com/" + mPassword);

                        id = authData.getUid().toString();

                        user.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ProfileType = dataSnapshot.child("ProfileType").getValue().toString();

                                username = dataSnapshot.child("Username").getValue().toString();

                                Log.d("LoginPage", ProfileType);

                                city = dataSnapshot.child("City").getValue().toString();
                                name = dataSnapshot.child("Name").getValue().toString();
                                phoneNumber = dataSnapshot.child("PhoneNumber").getValue().toString();
                                sport = dataSnapshot.child("Sport").getValue().toString();
                                email = dataSnapshot.child("Email").getValue().toString();
                                username = dataSnapshot.child("Username").getValue().toString();


                                if (ProfileType.equals("Student")) {
                                    Intent i = new Intent(LoginPage.this, StudentHomePage.class);
                                    i.putExtra("Username", username);
                                    i.putExtra("ID",id);
                                    i.putExtra("Email", email);
                                    i.putExtra("Name", name);
                                    i.putExtra("PhoneNumber",phoneNumber);
                                    i.putExtra("Sport", sport);
                                    i.putExtra("City", city);
                                    i.putExtra("Password",mPassword);
                                    startActivity(i);
                                } else if (ProfileType.equals("Coach")) {
                                    Intent a = new Intent(LoginPage.this, CoachProfile.class);
                                    a.putExtra("Username", username);
                                    a.putExtra("ID", id);
                                    a.putExtra("Email", email);
                                    a.putExtra("Name", name);
                                    a.putExtra("PhoneNumber", phoneNumber);
                                    a.putExtra("Sport", sport);
                                    a.putExtra("City", city);
                                    a.putExtra("Password",mPassword);
                                    startActivity(a);
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Log.d("LoginPage", "not");
                    }
                });

            }

        });
    }
}
