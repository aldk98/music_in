package com.example.aldrian.musicin2firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.aldrian.musicin2firebase.SessionManager.SessionManager;
import com.example.aldrian.musicin2firebase.model.Job;
import com.example.aldrian.musicin2firebase.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class OwnerInboxDetailsActivity extends AppCompatActivity {

    private DatabaseReference mDb;
    private Job currentJob;
    private SessionManager sessionManager;
    private String TAG="OwnInbDetDebug";
    public static String SERIALIZED_JOB ="serializedJob";
    private User currentUser;
    private TextView tv_name,tv_email,tv_phone,tv_information;
    private Button btn_accept;
    private String currentJobKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_inbox_details);
        sessionManager=new SessionManager(getApplicationContext());

        initObjects();
        initViews();
        initListeners();
    }

    private void initObjects() {
        currentJob = new Gson().fromJson(getIntent().getStringExtra(SERIALIZED_JOB), Job.class);
        mDb = FirebaseDatabase.getInstance().getReference().child("jobs");
        sessionManager = new SessionManager(getApplicationContext());
        currentUser = sessionManager.getCurrentUser();
        currentJobKey = currentJob.getId();
        currentJob.setId(null);
    }

    private void initListeners() {

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentJob.setStatus(Job.ACCEPTED);
                mDb.child(currentJobKey).setValue(currentJob).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG,"success appeal job");
                        onBackPressed();
                    }
                });

            }

        });

        Button btn_deny = findViewById(R.id.btn_deny);
        btn_deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentJob.setStatus(Job.DENIED);
                mDb.child(currentJobKey).setValue(currentJob).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG,"success appeal job");
                        onBackPressed();
                    }
                });
            }
        });
    }

    private void initViews() {
        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_email);
        tv_phone = findViewById(R.id.tv_phone);
        btn_accept = findViewById(R.id.btn_accept);
        tv_information = findViewById(R.id.tv_information);
        tv_name.setText(currentUser.getName());
        tv_email.setText(currentUser.getEmail());
        tv_phone.setText(currentUser.getPhone());
        tv_information.setText("Has appealed on"+ currentJob.getBusinessName());
    }
}
