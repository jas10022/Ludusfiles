package edu.self.ludus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;

public class LoginPage extends AppCompatActivity {

    private EditText mPasswordInput;
    private EditText mUsernameInput;
    private String mPassword;
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPasswordInput = (EditText) findViewById(R.id.password_input);
        mUsernameInput = (EditText) findViewById(R.id.email_input);
        final Button loginButton = (Button) findViewById(R.id.login);
        Button forgotPasswordButton = (Button) findViewById(R.id.forgot_password);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPassword = mPasswordInput.getText().toString();
                mUsername = mUsernameInput.getText().toString();

                ParseUser.logInInBackground(mUsername, mPassword, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null ){
                            Log.d("LoginPage","it was a succes");
                            Log.d("LoginPage","person equals " + user.get("person").toString());
                            if(user.get("person").toString().equals("student")){
                                Intent i = new Intent(LoginPage.this,StudentHomePage.class);
                                startActivity(i);
                            }
                            if (user.get("person").toString().equals("coach")){
                                Intent i = new Intent(LoginPage.this, CoachProfile.class);
                                startActivity(i);
                            }
                        }else{
                            Log.d("LoginPage","error is "+e);
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginPage.this);
                                builder.setMessage(R.string.account_error)
                                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                return;
                                            }
                                        });
                                // Create the AlertDialog object and return it
                                builder.show();

                        }
                    }
                });

            }

        });
    }
}
