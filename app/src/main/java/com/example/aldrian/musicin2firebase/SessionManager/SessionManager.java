package com.example.aldrian.musicin2firebase.SessionManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.aldrian.musicin2firebase.DatabaseHelper;
import com.example.aldrian.musicin2firebase.model.User;
import com.example.aldrian.musicin2firebase.users.LoginActivity;
import com.google.gson.Gson;

/**
 * Created by Tommy on 12/12/17.
 */

public class SessionManager {
    SharedPreferences login_pref;
    SharedPreferences.Editor editor;
    Context _context;
    private static final String PREF_NAME="MySharedPreference";
    private static final String IS_LOGIN="LoggedIn";
    private static final String USER_KEY="User";


    public SessionManager(Context context) {
        this._context = context;
        login_pref=context.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);
    }

    public void createLoginSession(User user){
        editor=login_pref.edit();
        editor.clear();
        editor.apply();
        editor.putBoolean(IS_LOGIN, true);
        Gson gson = new Gson();
        String serializedUser = gson.toJson(user);
        editor.putString(USER_KEY,serializedUser);
        editor.apply();
    }
    public void checkLogin(Class Destination){
        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
        else if(this.isLoggedIn()) {
            Intent i = new Intent(_context,Destination);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }

    public boolean checkLogin(){
        if(this.isLoggedIn()){
            return true;
        }else{
            return false;
        }
    }
    public void logoutUser(){
        editor=login_pref.edit();
        editor.clear();
        editor.apply();

        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    private boolean isLoggedIn(){
        return login_pref.getBoolean(IS_LOGIN,false);
    }

    public User getCurrentUser(){
        if(isLoggedIn()) {
            Gson gson=new Gson();
            String serializedUser = login_pref.getString(USER_KEY,null);
            User user = gson.fromJson(serializedUser,User.class);
            return user;
        }
        else {
            return null;
        }
    }
}
