package com.example.jaipr.stockbuddy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import Controller.StockAPI;

/**
 * Created by jaipr on 11-03-2017.
 */

public class FlashActivity extends Activity implements Animation.AnimationListener {

    private static int TIME_OUT=500;
    // Animation
    Animation animZoomOut;
    Animation animMove;
    Handler mHandler = new Handler();
    StockAPI stockAPI;
    private TextView txtMessage;
    private ImageView imageView;
    private JSONObject jsonObject;

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

        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (true) {
                    try {
                        Thread.sleep(5000);
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                if (isNetworkAvailable()) {
                                    setStock();
                                }
                            }
                        });
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }
        }).start();
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
                    startApp();
                    finish();
                }
            }, TIME_OUT);
        }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }

    public void startApp()
    {
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("LoginData", Context.MODE_PRIVATE);
        try {
            if(sharedPreferences.getBoolean("isLogin",false))
            {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
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
        String[] symbols = new String[]{"INTC", "FB", "TSLA", "NKE", "YHOO", "AMZN", "TCS", "MSFT"};
        stockAPI = new StockAPI();

        boolean[] _result = getSymbolStatus(symbols);
        jsonObject = stockAPI.getStock(symbols, _result);

        String jsonString = jsonObject.toString();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("StockSymbol", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("StockJSON", jsonString);
        editor.commit();
    }
}