package com.example.jaipr.stockbuddy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private String stringFirstName;
    private String stringLastName;
    private String stringEmail;
    private String stringPassword;
    private String stringConfirmPassword;

    private TextInputLayout textInputLayoutFirstName;
    private TextInputLayout textInputLayoutLastName;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConPassword;

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_signup1);

        /*stop automatically appear android keyboard when activity start*/
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        TextView stockTitle = (TextView) findViewById(R.id.title);
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Ubuntu-L.ttf");
        stockTitle.setTypeface(myCustomFont);

    }

    /*redirect to second signup layout*/
    public void SignupNext(View view) {
        editTextFirstName = (EditText) findViewById(R.id.input_firstName);
        editTextLastName = (EditText) findViewById(R.id.input_lastName);

        stringFirstName = editTextFirstName.getText().toString();
        stringLastName = editTextLastName.getText().toString();

        textInputLayoutFirstName = (TextInputLayout) findViewById(R.id.input_layout_first_name);
        textInputLayoutLastName = (TextInputLayout) findViewById(R.id.input_layout_last_name);

        textInputLayoutFirstName.setError(null);
        textInputLayoutLastName.setError(null);

        if (isRequire(stringFirstName)) {
            textInputLayoutFirstName.setError("Enter first name");
        }
        if (isRequire(stringLastName)) {
            textInputLayoutLastName.setError("Enter last name");
        } else {
            setContentView(R.layout.fragment_signup2);

            TextView stockTitle = (TextView) findViewById(R.id.title);
            Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Ubuntu-L.ttf");
            stockTitle.setTypeface(myCustomFont);
        }
    }

    /*perform the register operation*/
    public void SignUp(View view) {
        editTextEmail = (EditText) findViewById(R.id.input_email);
        editTextPassword = (EditText) findViewById(R.id.input_password);
        editTextConfirmPassword = (EditText) findViewById(R.id.input_confirm_password);

        stringEmail = editTextEmail.getText().toString();
        stringPassword = editTextPassword.getText().toString();
        stringConfirmPassword = editTextConfirmPassword.getText().toString();

        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        textInputLayoutConPassword = (TextInputLayout) findViewById(R.id.input_layout_confirm_password);

        textInputLayoutEmail.setError(null);
        textInputLayoutPassword.setError(null);
        textInputLayoutConPassword.setError(null);

        boolean isValid = true;

        if (!validateEmail(stringEmail)) {
            textInputLayoutEmail.setError("Enter valid email");
            isValid = false;
        }

        if (isRequire(stringPassword)) {
            textInputLayoutPassword.setError("Enter password");
            isValid = false;
        }

        if (isRequire(stringConfirmPassword)) {
            textInputLayoutConPassword.setError("Enter password");
            isValid = false;
        }

        if (!stringConfirmPassword.trim().equals(stringPassword.trim())) {
            textInputLayoutConPassword.setError("Password must be same");
            isValid = false;
        }

        if (isValid) {
            setSharedPreferences();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

    }

    /*redirect back to login actvity*/
    public void backToLogin(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    public void setSharedPreferences() {
        try {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("FirstName", StringUtils.capitalize(stringFirstName).trim());
            editor.putString("LastName", StringUtils.capitalize(stringLastName).trim());
            editor.putString("Email", stringEmail.toLowerCase().trim());
            editor.putString("Password", stringPassword.trim());

            editor.commit();
        } catch (Exception e) {

        }
    }

    public boolean isRequire(String s) {
        return s.equals("") || s == null;
    }

}
