package edu.self.ludus;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.ArrayList;

public class Coaches_Students extends AppCompatActivity {

    public static final String EXTRA = "CVA_Contact";
    public StudentList mStudents;
    public StudentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_coaches__students);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mStudents = StudentList.getInstance();

        for (int i = 0; i <30; i++){
            Students contact1 = new Students();
            contact1.setName("Jas Bakshi");
            contact1.sports = new ArrayList<String>();
            contact1.sports.add("Tennis");
            contact1.skillLevel = new ArrayList<String>();
            contact1.skillLevel.add("5/10");
            mStudents.add(contact1);
        }
        ListView listView = (ListView)findViewById(R.id.student_list_view);
        mAdapter = new StudentAdapter(mStudents);
        listView.setAdapter(mAdapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            int previousFirstItem = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > previousFirstItem) {
                    getSupportActionBar().hide();
                } else if (firstVisibleItem < previousFirstItem) {
                    getSupportActionBar().show();
                }
                previousFirstItem = firstVisibleItem;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(Coaches_Students.this, StudentView.class);
                i.putExtra(StudentView.EXTRA,position);
                startActivity(i);
                        
            }
        });

    }

private class StudentAdapter extends ArrayAdapter<Students>{
StudentAdapter(ArrayList<Students> contacts){
    super(Coaches_Students.this, R.layout.rows, R.id.student_title,contacts);
}
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = super.getView(position, convertView, parent);

        Students contact = getItem(position);

        TextView nameTextView = (TextView)convertView.findViewById(R.id.student_title);
        nameTextView.setText(contact.getName());
        return convertView;
    }
}

    @Override
    protected void onResume() {
        super.onResume();

        mAdapter.notifyDataSetChanged();
    }
}
