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

import com.firebase.client.Firebase;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Objects;

public class FindCoaches extends AppCompatActivity {

    private EditText mFindCoaches;
    private String mStudentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_find_coaches);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        TextView studentId = (TextView)findViewById(R.id.student_id);
        mFindCoaches = (EditText)findViewById(R.id.add_coach);
        Button add = (Button)findViewById(R.id.add);

        studentId.setText(mStudentId);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FindCoaches = mFindCoaches.getText().toString();

            }
        });


    }

}
