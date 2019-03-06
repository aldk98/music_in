package com.example.aldrian.musicin2firebase.fragments;


import android.os.AsyncTask;
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
import android.widget.Toast;

import com.example.aldrian.musicin2firebase.R;
import com.example.aldrian.musicin2firebase.SessionManager.SessionManager;
import com.example.aldrian.musicin2firebase.adapters.CurrentRecyclerAdapter;
import com.example.aldrian.musicin2firebase.model.Job;
import com.example.aldrian.musicin2firebase.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CurrentFragment extends Fragment {



    private ArrayList<Job> jobList;
    private User currentUser;
    private SessionManager sessionManager;
    private String TAG = "InboxFragDebugTAG";
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mDb;
    private CurrentRecyclerAdapter currentRecyclerAdapter;
    private RecyclerView recyclerView;


    public CurrentFragment() {
        // Required empty public constructor
    }

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
    private void setupJobsFromFirebase() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        final DatabaseReference jobs = mDb.child("jobs");
        jobs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                jobList.clear();
                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()) {
                    Job job = snapshot.getValue(Job.class);
                    if (job.getStatus().equals(Job.ACCEPTED) &&
                            (currentUser.getId().equals(job.getOwner_id()) ||
                            currentUser.getId().equals(job.getMusician_id()))) {
                        job.setId(snapshot.getKey());
                        jobList.add(job);
                    }
                }
                Log.d("Adapter JobList", currentRecyclerAdapter.getItemCount() + "");
                currentRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initViews() {
        recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(currentRecyclerAdapter);

    }

    private void initObjects() {
        mDb = FirebaseDatabase.getInstance().getReference();
        sessionManager=new SessionManager(getContext());
        currentUser = sessionManager.getCurrentUser();
        jobList = new ArrayList<>();
        sessionManager=new SessionManager(getContext());
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

        currentRecyclerAdapter =new CurrentRecyclerAdapter(jobList, new CurrentRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Job job, int position) {
                Toast.makeText(getActivity(), "This Job is "+job.getStatus(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
