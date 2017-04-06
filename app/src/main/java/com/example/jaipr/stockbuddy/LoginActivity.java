package com.example.jaipr.stockbuddy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
import org.json.JSONException;
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

    private boolean isRegisterUser = false;

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

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

    }

//    public  void Login(View view)
//    {
//        new AsynkLogin().execute();
//    }

    public void Login(View view)
    {
        progressBar.setVisibility(View.VISIBLE);
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
                new AsynkLogin().execute();
            }
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Connection not available", Toast.LENGTH_LONG).show();
        }
    }

    public void doLogin() {
        if (isRegisterUser) {
            setSharedPreferences();
            setStock();
            Intent i = new Intent(getApplicationContext(), FlashActivity.class);
            startActivity(i);
        } else {
            textInputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
            textInputLayoutEmail.setError("Enter valid email id");
            textInputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
            textInputLayoutPassword.setError("Enter valid password");
            Toast.makeText(getApplicationContext(), "Wrong credential", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean Require(String text) {
        return !(text == null || text.equals(""));
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

            String[] symbols = new String[]{"AAPL", "GOOGL", "INTC", "FB", "TSLA", "NFLX", "YHOO", "AMZN", "MSFT"};

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

    class AsynkLogin extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                String JSONStr = PostData();
                jsonObject = new JSONObject(JSONStr);
                String status = jsonObject.get("success").toString();
                isRegisterUser = status.equals("1");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            doLogin();
        }
    }
}
