package edu.self.ludus;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;

public class StudentView extends AppCompatActivity {

    public static final String EXTRA = "CVA_Contact";
    private static final String TAG = "ContactViewActivity";

    private int mColor;
    private Students mStudents;
    private int mPosition;
    private TextView mContactName;
    private FieldsAdapter mAdapter;
    private ImageView mEditIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.contact_view_toolbar);
        setSupportActionBar(toolbar);

            Display display = getWindowManager().getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);

            int width = point.x;
            int height = point.y;
            RelativeLayout headerSection = (RelativeLayout) findViewById(R.id.contact_View_header);


            headerSection.setLayoutParams(new LinearLayout.LayoutParams(width, (int) (width * (9.0 / 16.0))));


            mPosition = getIntent().getIntExtra(EXTRA, 0);
            mStudents = StudentList.getInstance().get(mPosition);
            mContactName = (TextView)findViewById(R.id.contact_view_name);
            ImageView editIcon = (ImageView)findViewById(R.id.edit_icon);

            editIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(StudentView.this, StudentEditActivity.class);
                    i.putExtra(StudentEditActivity.EXTRA ,  mPosition);
                    startActivity(i);
                }
            });

            toolbar.setOnMenuItemClickListener(new android.support.v7.widget.Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    Log.d("StudentView","it was clicked " + id);
                    if (id == R.id.edit_icon) {
                        Intent i = new Intent(StudentView.this, StudentEditActivity.class);
                        i.putExtra(StudentEditActivity.EXTRA ,  mPosition);
                        startActivity(i);
                        return true;
                    }else {
                        return false;
                    }
                }
            });
            toolbar.inflateMenu(R.menu.menu_student_feedback);

            ListView listView = (ListView)findViewById(R.id.contact_view_fields);
            mAdapter = new FieldsAdapter(mStudents.sports, mStudents.skillLevel);
            listView.setAdapter(mAdapter);

            updateUI();
        }

    private void updateUI(){
        mContactName.setText(mStudents.getName());
        mAdapter.notifyDataSetChanged();
    }

    private class FieldsAdapter extends BaseAdapter {
        ArrayList<String> emails;
        ArrayList<String> phoneNumbers;

        FieldsAdapter(ArrayList<String> emails, ArrayList<String> phoneNumbers){

            this.phoneNumbers = phoneNumbers;
            this.emails = emails;

        }

        @Override
        public int getCount() {

            return  phoneNumbers.size() + emails.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = StudentView.this.getLayoutInflater().inflate(R.layout.student_view_rows , parent, false);
            }

            String value = (String)getItem(position);

            TextView contactValue = (TextView)convertView.findViewById(R.id.contact_view_row_value);
            contactValue.setText(value);

            ImageView iv = (ImageView)convertView.findViewById(R.id.field_icon);
            if (isFirst(position)) {
                if (isEmail(position)) {
                    iv.setImageResource(R.drawable.ic_action_name);
                } else {
                    iv.setImageResource(R.drawable.ic_call);
                }
            }
            iv.setColorFilter(mColor);
            return convertView;
        }

        private boolean isFirst(int position){
            if (position == 0 || position == phoneNumbers.size()){
                return true;
            }else{
                return false;
            }
        }

        @Override
        public long getItemId(int position) {

            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (isEmail(position)){
                return emails.get(position - phoneNumbers.size());
            }else{
                return phoneNumbers.get(position);
            }
        }

        private boolean isEmail(int position){
            if (position > phoneNumbers.size()-1){

                return true;
            }else{
                return false;
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_student_view, menu);
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
