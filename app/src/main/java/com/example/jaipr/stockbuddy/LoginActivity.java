package com.example.jaipr.stockbuddy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import Bean.User;

/**
 * Created by jaipr on 25-02-2017.
 */

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView stockTitle=(TextView)findViewById(R.id.title);
        Typeface myCustomFont=Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-L.ttf");
        stockTitle.setTypeface(myCustomFont);

        editTextEmail=(EditText)findViewById(R.id.login_email);
        editTextEmail.setFocusableInTouchMode(true);
        editTextEmail.setFocusable(true);
        editTextEmail.requestFocus();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    public void Login(View view)
    {
        editTextEmail =(EditText)findViewById(R.id.login_email);
        editTextPassword =(EditText)findViewById(R.id.login_password);

        String str_email= editTextEmail.getText().toString();
        String str_password= editTextPassword.getText().toString();

        user=new User();

        if(validateLogin(str_email,str_password))
        {
            setSharedPreferences();

            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
        }
        else {
            editTextEmail.setBackground(getDrawable(R.drawable.edittextshapeerror));
            editTextPassword.setBackground(getDrawable(R.drawable.edittextshapeerror));
            //Toast.makeText(getApplicationContext(),"Wrong credential",Toast.LENGTH_SHORT).show();

            showToast("Wrong credential");
        }
    }

    public boolean validateLogin(String email,String password)
    {
        try {
            SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("UserData",Context.MODE_PRIVATE);
            if(email.toLowerCase().trim().equals(sharedPreferences.getString("Email",null)) && password.trim().equals(sharedPreferences.getString("Password",null)))
            {
                return true;
            }
            else {
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public void Register(View view)
    {
        Intent i = new Intent(getApplicationContext(),SignupActivity.class);
        startActivity(i);
    }

    public void setSharedPreferences()
    {
        try {
            SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("LoginData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();

            editor.putBoolean("isLogin", true);

            editor.commit();
        }
        catch (Exception e)
        {

        }
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }

    public void showToast(String str)
    {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(str);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

}
