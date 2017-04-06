package com.example.jaipr.stockbuddy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import Controller.StockAPI;

/**
 * Created by jaipr on 11-03-2017.
 */

public class FlashActivity extends Activity implements Animation.AnimationListener {

    private static int TIME_OUT=500;
    StockAPI stockAPI;

    Animation animZoomOut;
    Animation animMove;

    private TextView txtMessage;
    private ImageView imageView;
    private JSONObject jsonObject;
    private ProgressBar progressBar;
    private String strPrediction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        TextView stockTitle=(TextView)findViewById(R.id.text_App_Title);
        Typeface myCustomFont=Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-L.ttf");
        stockTitle.setTypeface(myCustomFont);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startAnim();
               // finish();
            }
        }, 200);


        txtMessage = (TextView) findViewById(R.id.text_App_Title);
        imageView = (ImageView) findViewById(R.id.logo);
        imageView.setVisibility(View.INVISIBLE);

        // load the animation
        animZoomOut = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.zoom_out);
        animMove = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.move);


        // set animation listener
        animZoomOut.setAnimationListener(this);
        animMove.setAnimationListener(this);

    }

    public void startAnim()
    {
        txtMessage.setVisibility(View.VISIBLE);
        txtMessage.startAnimation(animZoomOut);
    }

    @Override
    public void onAnimationEnd(Animation animation) {

        if (animation == animZoomOut) {
            imageView.setVisibility(View.VISIBLE);
            imageView.startAnimation(animMove);
        }
        if(animation== animMove)
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SetPrediction();
                    new GetPredictionAsynk().execute();
                    //finish();
                }
            }, TIME_OUT);
        }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    public void startApp()
    {
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("LoginData", Context.MODE_PRIVATE);
        try {
            if(sharedPreferences.getBoolean("isLogin",false))
            {
                if (isNetworkAvailable()) {
                    setStock();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Connection not available", Toast.LENGTH_LONG).show();
                    this.finish();
                    System.exit(0);
                }
            }
            else {
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        }
        catch (Exception e)
        {

        }
    }

    public boolean[] getSymbolStatus(String[] symbols) {
        boolean[] _symbol = new boolean[symbols.length];
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("StockSymbol", Context.MODE_PRIVATE);
        for (int i = 0; i < symbols.length; i++) {
            _symbol[i] = sharedPreferences.getBoolean(symbols[i], false);
        }
        return _symbol;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void setStock() {
        String[] symbols = new String[]{"AAPL", "GOOGL", "INTC", "FB", "TSLA", "NFLX", "YHOO", "AMZN", "MSFT"};
        stockAPI = new StockAPI();

        boolean[] _result = getSymbolStatus(symbols);
        jsonObject = stockAPI.getStock(symbols, _result);

        String jsonString = jsonObject.toString();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("StockSymbol", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("StockJSON", jsonString);
        editor.commit();
    }

    public String PostData() {
        String jsonResponse = "";
        String URL = "https://stockbull.herokuapp.com/stock/api";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpPost = new HttpGet(URL);

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

    public void SetPrediction() {
        strPrediction = PostData();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("StockPrediction", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("Prediction", strPrediction);
        editor.commit();

    }

    class StartApp extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            startApp();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
        }
    }

    class GetPredictionAsynk extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            SetPrediction();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new StartApp().execute();
        }
    }

}