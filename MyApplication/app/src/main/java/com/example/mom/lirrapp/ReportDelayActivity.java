package com.example.mom.lirrapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ReportDelayActivity extends AppCompatActivity {
    EditText  mLocation, mTime, mEmail, mComplaints, mWhatType;
    TextView mWhere;
    Button mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_delay);

        mWhere= (TextView)findViewById(R.id.where);
        mWhatType= (EditText)findViewById(R.id.what);
        mTime= (EditText)findViewById(R.id.time);
        mComplaints= (EditText)findViewById(R.id.complaints);
        mEmail= (EditText)findViewById(R.id.EditTextEmail);

        mSubmit=(Button)findViewById(R.id.sendResponse);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ReportDelayActivity.this, "Send Alert", Toast.LENGTH_SHORT).show();
            }
        });
        String where= mWhere.getText().toString();
        String time= mTime.getText().toString();
        String email=mEmail.getText().toString();
        String complaints= mComplaints.getText().toString();
        String type= mWhatType.getText().toString();

    }
}
