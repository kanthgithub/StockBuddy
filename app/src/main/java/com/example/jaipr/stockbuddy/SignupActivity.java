package com.example.jaipr.stockbuddy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

    private ProgressBar progressBar;

    private JSONObject jsonObject;

    /*validate email*/
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

    /*redirect to second sign up layout*/
    public void SignupNext(View view) {
        editTextFirstName = (EditText) findViewById(R.id.input_firstName);
        editTextLastName = (EditText) findViewById(R.id.input_lastName);

        stringFirstName = editTextFirstName.getText().toString().trim();
        stringLastName = editTextLastName.getText().toString().trim();

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

            progressBar = (ProgressBar) findViewById(R.id.progressBar);

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

        stringEmail = editTextEmail.getText().toString().trim();
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
            if (isNetworkAvailable()) {
                new AsynkRegister().execute();
            } else {
                Toast.makeText(this, "Connection not available", Toast.LENGTH_LONG).show();
            }
        }

    }

    /*redirect back to login activity*/
    public void backToLogin(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    /*check required field*/
    public boolean isRequire(String s) {
        return s.equals("") || s == null;
    }

    public String PostData() {
        String jsonResponse = "";
        String URL = "https://androidpugnatorcom.000webhostapp.com/Register.php?Email=" + stringEmail + "&Password=" + stringPassword
                + "&FirstName=" + stringFirstName + "&LastName=" + stringLastName + "&Mobile=1234567890&IMEI=1234567890";
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

    /*check network availability*/
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void isRegisterSucessFully() {
        try {
            String status = jsonObject.get("success").toString();
            if (status.equals("1")) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "User already exist", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class AsynkRegister extends AsyncTask<Void, Void, Void> {
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            isRegisterSucessFully();
        }

        public void TermsAndCondition(View view) {

        }
    }
    
}
