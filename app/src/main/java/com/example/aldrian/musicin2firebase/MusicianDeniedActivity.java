package com.example.aldrian.musicin2firebase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.aldrian.musicin2firebase.SessionManager.SessionManager;
import com.example.aldrian.musicin2firebase.model.Job;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

/**
 * Created by Tommy on 12/12/17.
 */

public class MusicianDeniedActivity extends AppCompatActivity {
    private DatabaseReference mDb;
    private Job job;
    private SessionManager sessionManager;
    private String TAG="MusicianAccActDebug";
    public static String SERIALIZED_JOB ="serializedJob";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musician_inbox_deny);
        mDb = FirebaseDatabase.getInstance().getReference();
        sessionManager = new SessionManager(getApplicationContext());
    }
    public void onBackPressed() {
        DatabaseReference jobs = mDb.child("jobs");
        job = new Gson().fromJson(getIntent().getStringExtra(SERIALIZED_JOB),Job.class);
        String key = job.getId();
        job.setStatus(Job.SEEN_BY_MUSICIAN);
        job.setId(null);

        jobs.child(key).setValue(job).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG,"success appeal job");
            }
        });
        super.onBackPressed();
    }
}
