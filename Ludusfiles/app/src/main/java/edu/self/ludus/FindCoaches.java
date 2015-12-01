package edu.self.ludus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Objects;

public class FindCoaches extends AppCompatActivity {

    private EditText mFindCoaches;
    private String mStudentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_coaches);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ParseUser user = ParseUser.getCurrentUser();

        TextView studentId = (TextView)findViewById(R.id.student_id);
        mFindCoaches = (EditText)findViewById(R.id.add_coach);
        Button add = (Button)findViewById(R.id.add);

        if(user.getObjectId() != null) {
            mStudentId = user.getObjectId().toString();
        }

        studentId.setText(mStudentId);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FindCoaches = mFindCoaches.getText().toString();

                ParseQuery<ParseUser> query = ParseUser.getQuery();

                query.whereEqualTo("objectId", FindCoaches);
                query.getFirstInBackground(new GetCallback<ParseUser>() {
                    @Override
                    public void done(ParseUser object, ParseException e) {
                        if (e == null) {


                            AlertDialog.Builder builder = new AlertDialog.Builder(FindCoaches.this);
                            builder.setMessage(R.string.connection_succes)
                                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            return;
                                        }
                                    });
                            // Create the AlertDialog object and return it
                            builder.show();
                        } else {
                            Log.d("FindCoaches", "this is the error " + e);

                            AlertDialog.Builder builder = new AlertDialog.Builder(FindCoaches.this);
                            builder.setMessage(R.string.connection_error)
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
