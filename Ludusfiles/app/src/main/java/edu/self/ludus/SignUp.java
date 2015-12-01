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
import android.widget.RadioButton;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class SignUp extends AppCompatActivity {

    private EditText mUsernameInput;
    private EditText mPasswordInput;
    private EditText mEmailInput;
    private RadioButton mCoachSignup;
    private RadioButton mStudentSignup;
    private TextView mLoginText;
    private EditText mNameInput;
    private EditText mPhoneNumberInput;
    private EditText mSportInput;
    private EditText mCityInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button submitButton = (Button)findViewById(R.id.mSubmitButton);
        mUsernameInput = (EditText)findViewById(R.id.username_input);
        mPasswordInput = (EditText)findViewById(R.id.mPasswordInput);
        mEmailInput = (EditText)findViewById(R.id.email_input);
        mNameInput = (EditText)findViewById(R.id.name_input);
        mPhoneNumberInput = (EditText)findViewById(R.id.phone_number_input);
        mCityInput = (EditText)findViewById(R.id.city);
        mSportInput = (EditText)findViewById(R.id.sport_input);
        mCoachSignup = (RadioButton)findViewById(R.id.coach_signup);
        mStudentSignup = (RadioButton)findViewById(R.id.student_signup);
        mLoginText = (TextView)findViewById(R.id.login);

          submitButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  String Username = mUsernameInput.getText().toString();
                  String Password = mPasswordInput.getText().toString();
                  String Email = mEmailInput.getText().toString();
                  String Name = mNameInput.getText().toString();
                  String PhoneNumber = mPhoneNumberInput.getText().toString();
                  String Sport = mSportInput.getText().toString();
                  String City = mCityInput.getText().toString();

                  ParseUser user = new ParseUser();
                  if (Username.isEmpty()||Password.isEmpty()||Email.isEmpty()||Name.isEmpty()||PhoneNumber.isEmpty()||Sport.isEmpty()|City.isEmpty()){
                      AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                      builder.setMessage(R.string.sign_in_error)
                              .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                  public void onClick(DialogInterface dialog, int id) {
                                      return;
                                  }
                              });
                      // Create the AlertDialog object and return it
                      builder.show();
                  }else {
                      user.setUsername(Username);
                      user.setPassword(Password);
                      user.setEmail(Email);
                      user.put("Name", Name);
                      user.put("PhoneNumber", PhoneNumber);
                      user.put("Sport", Sport);
                      user.put("City", City);
                  }

                  if (mCoachSignup.isChecked() || mStudentSignup.isChecked()) {
                      if (mCoachSignup.isChecked()) {
                          user.put("person", "coach");
                      }
                      if (mStudentSignup.isChecked()) {
                          user.put("person", "student");
                      } 
                  }else {
                      AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                      builder.setMessage(R.string.choose_person)
                              .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                  public void onClick(DialogInterface dialog, int id) {
                                      return;
                                  }
                              });
                      // Create the AlertDialog object and return it
                      builder.show();
                  }

                  user.signUpInBackground(new SignUpCallback() {
                      @Override
                      public void done(ParseException e) {
                          if (e==null){
                              if (mCoachSignup.isChecked()){
                                  Intent i = new Intent(SignUp.this,CoachProfile.class);
                                  startActivity(i);
                          }
                              if (mStudentSignup.isChecked()){
                                  Intent i = new Intent(SignUp.this,StudentHomePage.class);
                                  startActivity(i);
                              }
                          }else{
                              Log.d("SignUp","error is "+ e);
                          }
                      }
                  });

              }
          });
        mLoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUp.this, LoginPage.class);
                startActivity(i);
            }
        });
    }
}
