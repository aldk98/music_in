package com.example.aldrian.musicin2firebase.users;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aldrian.musicin2firebase.DatabaseHelper;
import com.example.aldrian.musicin2firebase.R;
import com.example.aldrian.musicin2firebase.SessionManager.SessionManager;
import com.example.aldrian.musicin2firebase.model.User;
import com.example.aldrian.musicin2firebase.musician.MusicianActivity;
import com.example.aldrian.musicin2firebase.owner.OwnerActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //    private TextInputLayout textInputLayoutEmail;
    //    private TextInputLayout textInputLayoutPassword;
    //private NestedScrollView nestedScrollView;

    private final AppCompatActivity activity = LoginActivity.this;
    private LinearLayout linearLayout;
    private EditText textInputEditTextEmail;
    private EditText textInputEditTextPassword;
    private Button appCompatButtonLogin;
    private AppCompatTextView textViewLinkRegister;
    private InputValidation inputValidation;
    private SessionManager sessionManager;
    private final String TAG= "LoginActDebug";
    private DatabaseReference mDatabase;
    private Boolean userExists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        //getSupportActionBar().hide();
        initViews();
        initListeners();
        initObjects();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void initViews() {
        linearLayout = findViewById(R.id.linearLayout);
        textInputEditTextEmail = findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = findViewById(R.id.textInputEditTextPassword);
        appCompatButtonLogin = findViewById(R.id.appCompatButtonLogin);
        textViewLinkRegister = findViewById(R.id.btnRegister);
    }

    private void initListeners() {
        appCompatButtonLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);
    }

    private void initObjects() {
        inputValidation = new InputValidation(activity);
        sessionManager=new SessionManager(getApplicationContext());
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonLogin:
                validateInputAndLogin();
                break;
            case R.id.btnRegister:
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
        }
    }

    private void validateInputAndLogin() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, getString(R.string.error_message_email))) {
            return;
        }
        toogleButtonsVisibility();
        checkExistingUser();
    }
    private void checkExistingUser() {
        DatabaseReference users = mDatabase.child("users");
        Query userQuery = users.orderByChild("email").equalTo(textInputEditTextEmail.getText().toString());

        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = new User();
                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        user = snapshot.getValue(User.class);
                        user.setId(snapshot.getKey());
                        userExists = true;
                        Log.d(TAG,""+user.getId());
                    }
                }else{
                    Log.d(TAG,"User not found");
                    userExists = false;
                }
                processLogin(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                toogleButtonsVisibility();
                Log.d(TAG,databaseError.getMessage());
            }
        });
    }

    private void toogleButtonsVisibility() {
        if(appCompatButtonLogin.getVisibility()==View.VISIBLE){
            appCompatButtonLogin.setVisibility(View.GONE);
            textViewLinkRegister.setVisibility(View.GONE);
        }else{
            appCompatButtonLogin.setVisibility(View.VISIBLE);
            textViewLinkRegister.setVisibility(View.VISIBLE);
        }

    }

    private void processLogin(User user) {
        if(!userExists || !textInputEditTextPassword.getText().toString().equals(user.getPassword())){
            Snackbar snackbar = Snackbar.make(linearLayout,getString(R.string.error_valid_email_password),Snackbar.LENGTH_LONG);
            View view= snackbar.getView();
            TextView tv = view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snackbar.show();
            toogleButtonsVisibility();
        } else {
            redirectUser(user);
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

