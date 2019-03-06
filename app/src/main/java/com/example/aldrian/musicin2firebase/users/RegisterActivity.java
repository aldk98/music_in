package com.example.aldrian.musicin2firebase.users;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amitshekhar.DebugDB;
import com.example.aldrian.musicin2firebase.DatabaseHelper;
import com.example.aldrian.musicin2firebase.R;
import com.example.aldrian.musicin2firebase.SessionManager.SessionManager;
import com.example.aldrian.musicin2firebase.model.User;
import com.example.aldrian.musicin2firebase.musician.MusicianActivity;
import com.example.aldrian.musicin2firebase.owner.OwnerActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "RegisterActDebug";
    private final AppCompatActivity activity = RegisterActivity.this;
    private NestedScrollView nestedScrollView;
    private EditText textInputEditTextName;
    private EditText textInputEditTextEmail;
    private EditText textInputEditTextPhone;
    private EditText textInputEditTextPassword;
    private EditText textInputEditTextConfirmPassword;
    private Button appCompatButtonRegister;
    private AppCompatTextView appCompatTextViewLoginLink;
    private InputValidation inputValidation;

    private String role = "empty";
    private SessionManager sessionManager;
    private DatabaseReference mDatabase;
    private Boolean userExists = false;
    private ImageView role_musician,role_owner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        //getSupportActionBar().hide();
        Log.d(TAG, DebugDB.getAddressLog());
        initViews();
        initListeners();
        initObjects();
    }

    private void initViews() {

        nestedScrollView = findViewById(R.id.nestedScrollView);
        textInputEditTextName = findViewById(R.id.textInputEditTextName);
        textInputEditTextEmail = findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPhone = findViewById(R.id.textInputEditTextPhone);
        textInputEditTextPassword = findViewById(R.id.textInputEditTextPassword);
        //textInputEditTextConfirmPassword = findViewById(R.id.textInputEditTextConfirmPassword);
        appCompatButtonRegister = findViewById(R.id.appCompatButtonRegister);
        appCompatTextViewLoginLink = findViewById(R.id.appCompatTextViewLoginLink);
        role_musician = findViewById(R.id.role_musician);
        role_owner = findViewById(R.id.role_owner);
    }

    private void initListeners() {
        appCompatButtonRegister.setOnClickListener(this);
        appCompatTextViewLoginLink.setOnClickListener(this);
        role_musician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                role_owner.setImageResource(R.drawable.btn_owner);
                role_musician.setImageResource(R.drawable.btn_musician_hover);
                role = "musician";
            }
        });

        role_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                role_owner.setImageResource(R.drawable.btn_owner_hover);
                role_musician.setImageResource(R.drawable.btn_musician);
                role = "owner";
            }
        });
    }

    private void initObjects() {
        inputValidation = new InputValidation(activity);
        sessionManager = new SessionManager(getApplicationContext());
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonRegister:
                validateInputAndRegistration();
                break;
            case R.id.appCompatTextViewLoginLink:
                finish();
                break;
        }
    }

    private void validateInputAndRegistration() {

        if(role == "empty"){
            Snackbar snack = Snackbar.make(nestedScrollView, getString(R.string.error_message_role), Snackbar.LENGTH_LONG);
            View view = snack.getView();
            TextView tv = view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextName, getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPhone, getString(R.string.error_message_phone))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, getString(R.string.error_message_password))) {
            return;
        }
        appCompatButtonRegister.setVisibility(View.GONE);
        appCompatTextViewLoginLink.setVisibility(View.GONE);
        checkExistingUser();
    }

    private void checkExistingUser() {
        DatabaseReference users = mDatabase.child("users");
        Query userQuery = users.orderByChild("email").equalTo(textInputEditTextEmail.getText().toString());

        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        User user = snapshot.getValue(User.class);
                        user.setId(snapshot.getKey());
                        userExists = true;
                        Log.d(TAG,""+user.getId());
                    }
                }else{
                    Log.d(TAG,"User not found");
                    userExists = false;
                }
                processRegistration();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                appCompatButtonRegister.setVisibility(View.VISIBLE);
                appCompatTextViewLoginLink.setVisibility(View.VISIBLE);
                Log.d(TAG,databaseError.getMessage());
            }
        });
    }
    private void registerToFirebase(){

        DatabaseReference users = mDatabase.child("users");
        final String userId= users.push().getKey();

        final User newUser = new User(
                role,
                textInputEditTextName.getText().toString(),
                textInputEditTextEmail.getText().toString(),
                textInputEditTextPhone.getText().toString(),
                textInputEditTextPassword.getText().toString()
        );
        users.child(userId).setValue(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                newUser.setId(userId);
                redirectUser(newUser);
                appCompatButtonRegister.setVisibility(View.VISIBLE);
                appCompatTextViewLoginLink.setVisibility(View.VISIBLE);
            }
        });
    }

    private void processRegistration() {
        if(userExists){
            Snackbar snack = Snackbar.make(nestedScrollView, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG);
            View view = snack.getView();
            TextView tv = view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();

        } else {
            registerToFirebase();
            Snackbar snackbar = Snackbar.make(nestedScrollView,"You are registered now",Snackbar.LENGTH_LONG);
            View view= snackbar.getView();
            TextView tv = view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snackbar.show();
        }
    }

    private void redirectUser(User user) {
        if(user.getRole().equals("musician")){
            final Intent intent = new Intent(activity, MusicianActivity.class);
            sessionManager.createLoginSession(user);
            startActivity(intent);
            finish();
        }else{
            final Intent intent = new Intent(activity, OwnerActivity.class);
            sessionManager.createLoginSession(user);
            startActivity(intent);
            finish();
        }
    }
}
