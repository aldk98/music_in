package com.example.aldrian.musicin2firebase.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aldrian.musicin2firebase.R;
import com.example.aldrian.musicin2firebase.model.Job;
import com.example.aldrian.musicin2firebase.adapters.MusicianFindJobRecyclerAdapter;
import com.example.aldrian.musicin2firebase.musician.MusicianFindJobDetailsActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Tommy on 9/12/17.
 */

public class MusicianFindJobFragment extends Fragment {

    private ArrayList<Job> jobList;
    private MusicianFindJobRecyclerAdapter musicianFindJobRecyclerAdapter;
    private DatabaseReference mDatabase;
    private RecyclerView.LayoutManager mLayoutManager;
    private String TAG = "FindJobFragDebugTAG";


    public MusicianFindJobFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recylerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initObjects();
        initViews();
        setupJobsFromFirebase();
    }

    private void initObjects() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        jobList = new ArrayList<>();
        musicianFindJobRecyclerAdapter = new MusicianFindJobRecyclerAdapter(
                jobList,
                new MusicianFindJobRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Job job, int position) {
                        Intent intent = new Intent(getActivity(),MusicianFindJobDetailsActivity.class);
                        String jobJson = new Gson().toJson(job);
                        intent.putExtra(MusicianFindJobDetailsActivity.SERIALIZED_JOB,jobJson);
                        startActivity(intent);
                    }
                });
    }

    private void initViews() {
        RecyclerView recyclerViewJobs = Objects.requireNonNull(getView()).findViewById(R.id.recyclerView);
        recyclerViewJobs.setItemAnimator(new DefaultItemAnimator());
        recyclerViewJobs.setHasFixedSize(false);
        recyclerViewJobs.setAdapter(musicianFindJobRecyclerAdapter);
        recyclerViewJobs.setLayoutManager(mLayoutManager);
    }

    private void setupJobsFromFirebase() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        final DatabaseReference jobs = mDatabase.child("jobs");
        jobs.orderByChild("status").equalTo(Job.WAITING);
        jobs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                jobList.clear();
                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()) {
                    Log.d(TAG,snapshot.toString());
                    Job job = snapshot.getValue(Job.class);
                    if (job != null && job.getStatus().equals(Job.WAITING)) {
                        job.setId(snapshot.getKey());
                        jobList.add(job);
                    }

                }
                Log.d(TAG,"Available Jobs :"+musicianFindJobRecyclerAdapter.getItemCount());
                musicianFindJobRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}


