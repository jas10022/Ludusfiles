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

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;


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
    private Firebase myFirebaseRef;

    public static String Username;
    public static String Password;
    public static String Email;
    public static String PhoneNumber;
    public static String Sport;
    public static String City;
    public static String Name;
    public static boolean loginScene;
    public static Firebase user;
    private String id;
    private Firebase userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loginScene = true;
        if (LoginPage.loginScene2 == true){
            loginScene = false;
        }else if(LoginPage.loginScene2 == false){
            loginScene = true;
        }


        myFirebaseRef = new Firebase("https://mytennis.firebaseio.com/");

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

                  Username = mUsernameInput.getText().toString();
                  Password = mPasswordInput.getText().toString();
                  Email = mEmailInput.getText().toString();
                  PhoneNumber = mPhoneNumberInput.getText().toString();
                  Sport = mSportInput.getText().toString();
                  City = mCityInput.getText().toString();
                  Name = mNameInput.getText().toString();


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
                      myFirebaseRef.createUser(Email, Password, new Firebase.ValueResultHandler<Map<String, Object>>() {
                          @Override
                          public void onSuccess(Map<String, Object> stringObjectMap) {
                              Log.d("SignUp", "works");

                              user = myFirebaseRef.child(Password);

                              myFirebaseRef.authWithPassword(Email, Password, new Firebase.AuthResultHandler() {
                                  @Override
                                  public void onAuthenticated(AuthData authData) {

                                      id = authData.getUid().toString();

                                      user.child("Username").setValue(Username);
                                      user.child("Email").setValue(Email);
                                      user.child("PhoneNumber").setValue(PhoneNumber);
                                      user.child("Sport").setValue(Sport);
                                      user.child("City").setValue(City);
                                      user.child("Name").setValue(Name);
                                      user.child("Coach").setValue("");
                                      user.child("Student").setValue("");

                                      userid = myFirebaseRef.child(id);
                                      userid.child("Message").setValue("It worked");
                                      userid.child("Username").setValue(Username);
                                      userid.child("Email").setValue(Email);
                                      userid.child("PhoneNumber").setValue(PhoneNumber);
                                      userid.child("Sport").setValue(Sport);
                                      userid.child("City").setValue(City);
                                      userid.child("Name").setValue(Name);
                                      userid.child("Coach").setValue("");
                                      userid.child("Student");

                                      if (mCoachSignup.isChecked() && mStudentSignup.isChecked()) {
                                          AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                                          builder.setMessage(R.string.both_cant_be)
                                                  .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                                      public void onClick(DialogInterface dialog, int id) {
                                                          return;
                                                      }
                                                  });
                                          // Create the AlertDialog object and return it
                                          builder.show();
                                      } else {
                                          if (mCoachSignup.isChecked() || mStudentSignup.isChecked()) {
                                              if (mCoachSignup.isChecked()) {
                                                  Intent i = new Intent(SignUp.this, CoachProfile.class);
                                                  i.putExtra("Username", Username);
                                                  i.putExtra("ID",id);
                                                  i.putExtra("Email",Email);
                                                  i.putExtra("Name", Name);
                                                  i.putExtra("PhoneNumber",PhoneNumber);
                                                  i.putExtra("Sport", Sport);
                                                  i.putExtra("City", City);
                                                  i.putExtra("Password",Password);
                                                  i.putExtra("StudentID","");
                                                  startActivity(i);
                                                  user.child("ProfileType").setValue("Coach");
                                              } else if (mStudentSignup.isChecked()) {
                                                  Intent i = new Intent(SignUp.this, StudentHomePage.class);
                                                  i.putExtra("Username", Username);
                                                  i.putExtra("ID",id);
                                                  i.putExtra("Email",Email);
                                                  i.putExtra("Name", Name);
                                                  i.putExtra("PhoneNumber",PhoneNumber);
                                                  i.putExtra("Sport", Sport);
                                                  i.putExtra("City", City);
                                                  i.putExtra("Password",Password);
                                                  i.putExtra("CoachID","");
                                                  startActivity(i);
                                                  user.child("ProfileType").setValue("Student");
                                              }
                                          } else {
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
                                      }

                                  }

                                  @Override
                                  public void onAuthenticationError(FirebaseError firebaseError) {

                                  }
                              });
                          }

                          @Override
                          public void onError(FirebaseError firebaseError) {
                              Log.d("SignUp","not");
                              AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                                          builder.setMessage(R.string.sign_in_error_email_passwrod)
                                                  .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                              public void onClick(DialogInterface dialog, int id) {
                                                  return;
                                              }
                                      });
                              // Create the AlertDialog object and return it
                              builder.show();
                          }
                      });
                  }



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
