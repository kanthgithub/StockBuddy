package com.example.jaipr.stockbuddy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by jaipr on 25-02-2017.
 */

public class LoginActivity extends AppCompatActivity {

    Handler mHandler = new Handler();
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private ProgressBar progressBar;

    private String str_email;
    private String str_password;

    private JSONObject jsonObject;

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

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);

    }

    public void Login(View view)
    {
        if (isNetworkAvailable()) {
            editTextEmail = (EditText) findViewById(R.id.login_email);
            editTextPassword = (EditText) findViewById(R.id.login_password);

            str_email = editTextEmail.getText().toString();
            str_password = editTextPassword.getText().toString();

            boolean isValid = true;

            if (Require(str_email)) {
                textInputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
                textInputLayoutEmail.setError(null);
            } else {
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
                if (validateLogin()) {
                    progressBar.setProgress(100);
                    progressBar.setVisibility(View.INVISIBLE);
                    setSharedPreferences();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                } else {
                    progressBar.setProgress(100);
                    progressBar.setVisibility(View.INVISIBLE);
                    textInputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
                    textInputLayoutEmail.setError("Enter valid email id");
                    textInputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
                    textInputLayoutPassword.setError("Enter valid password");
                    Toast.makeText(getApplicationContext(), "Wrong credential", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "Connection not available", Toast.LENGTH_LONG).show();
        }
    }

    public boolean Require(String text) {
        return !(text == null || text.equals(""));
    }

    public boolean validateLogin()
    {
        try {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(20);
            String JSONStr = PostData();
            progressBar.setProgress(60);
            jsonObject = new JSONObject(JSONStr);
            String status = jsonObject.get("success").toString();
            return status.equals("1");
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
            progressBar.setProgress(80);
            SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("LoginData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();

            editor.putBoolean("isLogin", true);

            editor.commit();

            sharedPreferences = getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();

            editor.putString("FirstName", StringUtils.capitalize(jsonObject.get("firstName").toString()).trim());
            editor.putString("LastName", StringUtils.capitalize(jsonObject.get("lastName").toString()).trim());
            editor.putString("Email", jsonObject.get("email").toString().toLowerCase().trim());

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


    public String PostData() {
        String jsonResponse = "";
        String URL = "https://androidpugnatorcom.000webhostapp.com/Login.php?Email=" + str_email + "&Password=" + str_password;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(URL);

            HttpResponse httpResponse = httpClient.execute(httpPost);

            HttpEntity httpEntity = httpResponse.getEntity();
            jsonResponse = readResponse(httpResponse);
        } catch (Exception exception) {
        }
        return jsonResponse;
    }

    public String readResponse(HttpResponse res) {
        InputStream is = null;
        String return_text = "";
        try {
            is = res.getEntity().getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return_text = sb.toString();
        } catch (Exception e) {

        }
        return return_text;

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
