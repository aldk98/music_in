package com.example.aldrian.musicin2firebase.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.aldrian.musicin2firebase.R;
import com.example.aldrian.musicin2firebase.SessionManager.SessionManager;
import com.example.aldrian.musicin2firebase.model.User;

/**
 * Created by Tommy on 12/12/17.
 */

public class ProfileFragment extends Fragment {
    SessionManager sessionManager;
    private User user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager=new SessionManager(getActivity());
        TextView tv_name = getView().findViewById(R.id.tv_name);
        TextView tv_email = getView().findViewById(R.id.tv_email);
        TextView tv_phone = getView().findViewById(R.id.tv_phone);
        Button btn_logout = getView().findViewById(R.id.btn_logout);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logoutUser();
            }
        });

        user = sessionManager.getCurrentUser();

       String name = user.getName();
        tv_name.setText(name);
        String email = user.getEmail();
        tv_email.setText(email);
        String phone = user.getPhone();
        tv_phone.setText(phone);
    }
}
