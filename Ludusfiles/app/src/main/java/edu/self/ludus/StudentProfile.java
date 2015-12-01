package edu.self.ludus;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.ParseUser;

import org.w3c.dom.Text;


public class StudentProfile extends ActionBarActivity {

    private String mName;
    private String mSport;
    private String mLocation;
    private String mPhoneNumber;
    private String mEmail;
    private String mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        ParseUser user = ParseUser.getCurrentUser();

        TextView nameUser = (TextView)findViewById(R.id.user_name);
        TextView userSport = (TextView)findViewById(R.id.user_sport);
        TextView userLocation = (TextView)findViewById(R.id.location);
        TextView userPhoneNumber = (TextView)findViewById(R.id.user_phone_number);
        TextView userEmail = (TextView)findViewById(R.id.email);
        TextView userName = (TextView)findViewById(R.id.username);
        TextView v = (TextView)findViewById(R.id.coach);

        if (user.get("Name") != null) {
            mName = user.get("Name").toString();
        }
        if (user.get("Sport") != null) {
            mSport = user.get("Sport").toString();
        }
        if (user.get("City") != null) {
            mLocation = user.get("City").toString();
        }
        if (user.get("PhoneNumber") != null) {
            mPhoneNumber = user.get("PhoneNumber").toString();
        }
        if ( user.get("email") != null) {
            mEmail = user.get("email").toString();
        }
        if (user.get("username") !=null) {
            mUserName = user.get("username").toString();
        }

        nameUser.setText(mName);
        userSport.setText(mSport);
        userLocation.setText(mLocation);
        userPhoneNumber.setText(mPhoneNumber);
        userEmail.setText(mEmail);
        userName.setText(mUserName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_student_profile, menu);
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
