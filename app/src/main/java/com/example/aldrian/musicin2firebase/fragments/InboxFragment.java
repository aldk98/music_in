package com.example.aldrian.musicin2firebase.fragments;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aldrian.musicin2firebase.MusicianAcceptedActivity;
import com.example.aldrian.musicin2firebase.MusicianDeniedActivity;
import com.example.aldrian.musicin2firebase.OwnerInboxDetailsActivity;
import com.example.aldrian.musicin2firebase.R;
import com.example.aldrian.musicin2firebase.SessionManager.SessionManager;
import com.example.aldrian.musicin2firebase.adapters.InboxRecyclerAdapter;
import com.example.aldrian.musicin2firebase.model.Job;
import com.example.aldrian.musicin2firebase.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class InboxFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Job> jobList;
    private InboxRecyclerAdapter inboxRecyclerAdapter;
    private User currentUser;
    private SessionManager sessionManager;
    private String TAG = "InboxFragDebugTAG";
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mDb;
    public InboxFragment(){}

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

    private void setListenersOnUserRole() {
        if(currentUser.getRole().equals("owner")){
            inboxRecyclerAdapter = new InboxRecyclerAdapter(jobList, new InboxRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Job job, int position) {
                    String jobJson = new Gson().toJson(job);
                    Intent intent = new Intent(getActivity(), OwnerInboxDetailsActivity.class);
                    intent.putExtra(OwnerInboxDetailsActivity.SERIALIZED_JOB,jobJson);
                    startActivity(intent);
                }
            });

        }else {
            inboxRecyclerAdapter =new InboxRecyclerAdapter(jobList, new InboxRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Job job, int position) {
                    String jobJson = new Gson().toJson(job);

                    if(job.getStatus().equals("denied")){
                        Intent intent = new Intent(getActivity(), MusicianDeniedActivity.class);
                        intent.putExtra(MusicianAcceptedActivity.SERIALIZED_JOB,jobJson);
                        startActivity(intent);
                    }else if(job.getStatus().equals("accepted")) {
                        Intent intent = new Intent(getActivity(), MusicianAcceptedActivity.class);
                        intent.putExtra(MusicianDeniedActivity.SERIALIZED_JOB,jobJson);
                        startActivity(intent);
                    }
                }
            });
        }
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
                    if(currentUser.getRole().equals("owner")){
                        if(job.getStatus().equals(Job.ON_APPEAL)){
                            job.setId(snapshot.getKey());
                            jobList.add(job);
                        }
                    }else{
                        if(job.getMusician_id().equals(currentUser.getId()) && (job.getStatus().equals(Job.DENIED) ||
                                job.getStatus().equals(Job.ACCEPTED))){
                            job.setId(snapshot.getKey());
                            jobList.add(job);
                        }
                    }
                }
                Log.d("Adapter JobList",inboxRecyclerAdapter.getItemCount()+"");
                inboxRecyclerAdapter.notifyDataSetChanged();
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
        recyclerView.setAdapter(inboxRecyclerAdapter);
    }

    private void initObjects() {
        mDb = FirebaseDatabase.getInstance().getReference();
        sessionManager=new SessionManager(getContext());
        currentUser =sessionManager.getCurrentUser();
        jobList = new ArrayList<>();
        sessionManager=new SessionManager(getContext());
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        setListenersOnUserRole();
    }
}

