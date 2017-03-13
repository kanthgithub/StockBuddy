package com.example.jaipr.stockbuddy;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import Bean.User;

/**
 * Created by jaipr on 25-02-2017.
 */


public class UserProfileFragment extends Fragment {


    private TextView userName_header;
    private TextView userEmail_header;
    private TextView userName;
    private TextView userAge;
    private TextView userGender;
    private TextView userLocation;

    private User user;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_user_profile, container, false);

        //load user Profile
        loadUserProfile(view);

        return view;
    }

    private void loadUserProfile(View view) {
        userName_header=(TextView) view.findViewById(R.id.header_userName);
        userEmail_header=(TextView) view.findViewById(R.id.header_userEmail);
        userName=(TextView) view.findViewById(R.id.text_user_name);
        userAge=(TextView) view.findViewById(R.id.text_user_age);
        userGender=(TextView) view.findViewById(R.id.text_user_gender);
        userLocation=(TextView) view.findViewById(R.id.text_user_location);

        user=new User();
        SharedPreferences sharedPreferences=getContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String str_userNameHeader=sharedPreferences.getString("FirstName",null)+" "+sharedPreferences.getString("LastName",null);
        String str_userEmail=sharedPreferences.getString("Email",null);
        String str_userName="Name : "+str_userNameHeader;
        String str_userAge="Age : "+user.getAge();
        String str_userGender="Gender : "+user.getGender();
        String str_userLocation="Location : "+user.getLocation();

        userName_header.setText(str_userNameHeader);
        userEmail_header.setText(str_userEmail);
        userName.setText(str_userName);
        userAge.setText(str_userAge);
        userGender.setText(str_userGender);
        userLocation.setText(str_userLocation);
    }

}
