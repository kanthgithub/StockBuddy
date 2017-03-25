package com.example.jaipr.stockbuddy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by jaipr on 25-02-2017.
 */

public class LoginActivity extends AppCompatActivity {

    Handler mHandler = new Handler();
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setStock();

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

        boolean isValid = true;

        if (Require(str_email))
        {
            textInputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
            textInputLayoutEmail.setError(null);
        }
        else {
            textInputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
            textInputLayoutEmail.setError("Enter email id");
            isValid = false;
        }

        if (Require(str_password)) {
            textInputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
            textInputLayoutPassword.setError(null);
        } else {
            textInputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
            textInputLayoutPassword.setError("Enter password");
            isValid = false;
        }

        if (isValid) {
            if (validateLogin(str_email, str_password)) {
                setSharedPreferences();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            } else {
                textInputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
                textInputLayoutEmail.setError("Enter valid email id");
                textInputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
                textInputLayoutPassword.setError("Enter valid password");
                Toast.makeText(getApplicationContext(), "Wrong credential", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean Require(String text) {
        return !(text == null || text.equals(""));
    }

    public boolean validateLogin(String email,String password)
    {
        try {
            SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("UserData",Context.MODE_PRIVATE);
            return email.toLowerCase().trim().equals(sharedPreferences.getString("Email", null)) && password.trim().equals(sharedPreferences.getString("Password", null));
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

    public void setStock() {
        try {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("StockSymbol", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            String[] symbols = new String[]{"INTC", "FB", "TSLA", "NKE", "YHOO", "AMZN", "TCS", "MSFT"};

            for (String symbol : symbols) {
                editor.putBoolean(symbol, true);
                editor.commit();
            }
        } catch (Exception e) {

        }
    }

}
