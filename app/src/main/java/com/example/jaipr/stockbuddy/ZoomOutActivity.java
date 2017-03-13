package com.example.jaipr.stockbuddy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Bean.StockJSON;

/**
 * Created by jaipr on 11-03-2017.
 */

public class ZoomOutActivity extends Activity implements Animation.AnimationListener {

    private TextView txtMessage;
    private ImageView imageView;
    private static int TIME_OUT=500;

    // Animation
    Animation animZoomOut;
    Animation animMove;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoomout);

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
}