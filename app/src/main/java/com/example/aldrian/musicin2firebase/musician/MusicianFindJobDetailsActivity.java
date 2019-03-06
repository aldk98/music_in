package com.example.aldrian.musicin2firebase.musician;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.aldrian.musicin2firebase.R;
import com.example.aldrian.musicin2firebase.SessionManager.SessionManager;
import com.example.aldrian.musicin2firebase.model.Job;
import com.example.aldrian.musicin2firebase.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class MusicianFindJobDetailsActivity extends AppCompatActivity {
    DatabaseReference mDatabase;
    SessionManager sessionManager;
    public static String SERIALIZED_JOB = "serializedJob";
    private TextView tv_job_businessName;
    private TextView tv_job_address;
    private TextView tv_job_time;
    private TextView tv_job_paying_range;
    private TextView tv_job_date;
    private TextView tv_job_genre;
    private Button btn_appeal;
    private Job job;
    private String TAG = "JobDetailDebugTAG";
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musician_findjob_details);
        initObjects();
        initViews();
        setTexts();
        initListeners();
    }

    private void initListeners() {
        btn_appeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference jobs = mDatabase.child("jobs");
                String key = job.getId();
                job.setStatus(Job.ON_APPEAL);
                job.setId(null);
                job.setMusician_id(user.getId());
                jobs.child(key).setValue(job).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG,"success appeal job");
                    }
                });
                btn_appeal.setVisibility(View.GONE);
            }
        });
    }

    private void setTexts() {
        tv_job_businessName.setText(job.getBusinessName());
        tv_job_address.setText(job.getAddress());
        tv_job_time.setText(job.getTime());
        tv_job_paying_range.setText(job.getPayRange());
        tv_job_date.setText(job.getDate());
        tv_job_genre.setText(job.getGenre());
    }

    private void initViews() {
        btn_appeal = findViewById(R.id.btn_appeal);
        tv_job_businessName = findViewById(R.id.tv_job_businessName);
        tv_job_address = findViewById(R.id.tv_job_address);
        tv_job_time = findViewById(R.id.tv_job_time);
        tv_job_paying_range = findViewById(R.id.tv_job_paying_range);
        tv_job_date = findViewById(R.id.tv_job_date);
        tv_job_genre = findViewById(R.id.tv_job_genre);
    }

    private void initObjects() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        job = new Gson().fromJson(getIntent().getStringExtra(SERIALIZED_JOB),Job.class);
        Log.d("JOB_ITEM",getIntent().getStringExtra(SERIALIZED_JOB));
        sessionManager=new SessionManager(getApplicationContext());
        user = sessionManager.getCurrentUser();
        if(job.getStatus().equals(Job.ON_APPEAL)){
            btn_appeal.setVisibility(View.GONE);
        }
    }
}

//        tv_job_businessName.setText(job.getBusinessName());
//        tv_job_address.setText(job.getAddress());
//        tv_job_time.setText(job.getTime());
//        tv_job_paying_range.setText(job.getPayRange());
//        tv_job_date.setText(job.getDate());
//        tv_job_genre.setText(job.getGenre());
