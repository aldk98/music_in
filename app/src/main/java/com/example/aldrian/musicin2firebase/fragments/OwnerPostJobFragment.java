package com.example.aldrian.musicin2firebase.fragments;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.aldrian.musicin2firebase.R;
import com.example.aldrian.musicin2firebase.SessionManager.SessionManager;
import com.example.aldrian.musicin2firebase.model.Job;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OwnerPostJobFragment extends Fragment {

    private SessionManager sessionManager;


    private EditText et_businessName;
    private EditText et_address;
    private EditText et_time;
    private EditText et_paying_range;
    private EditText et_date;
    private EditText et_genre;
    private Button btn_post;
    private View rootView;
    private DatabaseReference mDatabase;

    public OwnerPostJobFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_owner_post, container, false);

        initViews();
        return rootView;
    }

    private void initViews() {
        et_businessName = rootView.findViewById(R.id.et_businessName);
        et_address = rootView.findViewById(R.id.et_address);
        et_time = rootView.findViewById(R.id.et_time);
        et_paying_range = rootView.findViewById(R.id.et_paying_range);
        et_date = rootView.findViewById(R.id.et_date);
        et_genre = rootView.findViewById(R.id.et_genre);
        btn_post =  rootView.findViewById(R.id.btn_post);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initObjects();

        initListeners();
    }

    private void initObjects() {
        sessionManager=new SessionManager(getContext());
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void initListeners() {
        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInputAndInputJob();
            }
        });
    }

    private void validateInputAndInputJob() {
        if(et_businessName.getText().toString().equals("")){
            Snackbar snack = Snackbar.make(rootView,(R.string.error_message_businessName),Snackbar.LENGTH_LONG);
            View view = snack.getView();
            TextView tv = view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }else if(et_address.getText().toString().equals("")){
            Snackbar snack = Snackbar.make(rootView,getString(R.string.error_message_address),Snackbar.LENGTH_LONG);
            View view = snack.getView();
            TextView tv = view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }else if(et_time.getText().toString().equals("")){
            Snackbar snack = Snackbar.make(rootView,getString(R.string.error_message_time),Snackbar.LENGTH_LONG);
            View view = snack.getView();
            TextView tv = view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }else if(et_paying_range.getText().toString().equals("")){
            Snackbar snack = Snackbar.make(rootView,getString(R.string.error_message_paying_range),Snackbar.LENGTH_LONG);
            View view = snack.getView();
            TextView tv = view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }else if(et_date.getText().toString().equals("")){
            Snackbar snack = Snackbar.make(rootView,getString(R.string.error_message_date),Snackbar.LENGTH_LONG);
            View view = snack.getView();
            TextView tv = view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }else if(et_genre.getText().toString().equals("")){
            Snackbar snack = Snackbar.make(rootView,getString(R.string.error_message_genre),Snackbar.LENGTH_LONG);
            View view = snack.getView();
            TextView tv = view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }else{
            toggleButton();
            postJobToFirebase();

        }
        hideKeyboardFrom(this.getContext(),this.rootView);
    }

    private void emptyFields() {
        et_businessName.setText(null);
        et_address.setText(null);
        et_time.setText(null);
        et_paying_range.setText(null);
        et_date.setText(null);
        et_genre.setText(null);
    }

    private void postJobToFirebase(){

        DatabaseReference jobs = mDatabase.child("jobs");
        final String jobId= jobs.push().getKey();
        final Job newJob= new Job();

        newJob.setBusinessName(et_businessName.getText().toString().trim());
        newJob.setAddress(et_address.getText().toString().trim());
        newJob.setTime("\""+et_time.getText().toString().trim()+"\"");
        newJob.setPayRange("\""+et_paying_range.getText().toString().trim()+"\"");
        newJob.setDate(et_date.getText().toString().trim());
        newJob.setGenre(et_genre.getText().toString().trim());
        newJob.setOwner_id(sessionManager.getCurrentUser().getId());
        newJob.setStatus(Job.WAITING);

        jobs.child(jobId).setValue(newJob).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                newJob.setId(jobId);

                Snackbar snack = Snackbar.make(rootView,getString(R.string.success_message_add),Snackbar.LENGTH_LONG);
                View view = snack.getView();
                TextView tv = view.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(Color.WHITE);
                snack.show();
                emptyFields();
                toggleButton();
            }
        });
    }

    private void toggleButton() {
        if(btn_post.getVisibility()== View.GONE){
            btn_post.setVisibility(View.VISIBLE);
        }else{
            btn_post.setVisibility(View.GONE);
        }

    }
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
