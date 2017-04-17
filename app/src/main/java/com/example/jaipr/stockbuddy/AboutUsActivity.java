package com.example.jaipr.stockbuddy;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutUsActivity extends AppCompatActivity {

    private ImageView imageViewBack;
    private ImageView imageViewWeb;
    private ImageView imageViewMail;
    private ImageView imageViewShare;

    private TextView textViewAboutApp1;
    private TextView textViewAboutApp2;
    private TextView textViewAboutApp3;

    private String strAboupApp1="We propose a stock market recommender system that analyses stock data and suggests a ranked basket of stocks.";
    private String strAboupApp2="The objective of this recommender system is to support stock market traders, individual investors and fund managers in their decisions.";
    private String strAboupApp3="Recommender systems apply different techniques of data mining on data and find similarities among various data items using live prices, historical data and news articles.";

    private String strLinkedIn="https://www.linkedin.com/in/jaimin-prajapati-9b8a91122/";
    private String strGooglePlus="https://plus.google.com/107934279122380207641";
    private String strGithub="https://github.com/jaiprajapati3";
    private String strWordPress="https://androidpugnator.wordpress.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        imageViewBack=(ImageView)findViewById(R.id.back);
        imageViewBack.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        imageViewWeb =(ImageView)findViewById(R.id.earth);
        imageViewWeb.setColorFilter(Color.parseColor("#263238"),PorterDuff.Mode.SRC_IN);

        imageViewMail=(ImageView)findViewById(R.id.mail);
        imageViewMail.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        imageViewShare=(ImageView)findViewById(R.id.share);
        imageViewShare.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        textViewAboutApp1=(TextView)findViewById(R.id.about_app1);
        textViewAboutApp1.setText(strAboupApp1);

        textViewAboutApp2=(TextView)findViewById(R.id.about_app2);
        textViewAboutApp2.setText(strAboupApp2);

        textViewAboutApp3=(TextView)findViewById(R.id.about_app3);
        textViewAboutApp3.setText(strAboupApp3);
    }

    public void SendMail(View view)
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_VOICEMAIL)
                != PackageManager.PERMISSION_GRANTED) {}
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setType("plain/text");
        sendIntent.setData(Uri.parse("jaiprajapati3@gmail.com"));
        sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "jaiprajapati3@gmail.com" });
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Stock Buddy");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "hello. this is a message sent from my atock buudy app :-)");
        startActivity(sendIntent);
    }

    public void ShareApp(View view)
    {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Stock Buddy");
            String sAux = "\nLet me recommend you this application\n\n";
            sAux = sAux + "www.google.com \n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch(Exception e) {
            //e.toString();
        }
    }

    public void Connect(View view)
    {
        final Dialog dialog = new Dialog(AboutUsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.layout_dialog_screen);//setting the dialog xml layout

        dialog.findViewById(R.id.facebook).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        openFaceBookPage();
                        dialog.dismiss();
                    }
                });
        dialog.findViewById(R.id.google_plus).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        openURL(strGooglePlus);
                        dialog.dismiss();
                    }
                });
        dialog.findViewById(R.id.linkedin).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        openURL(strLinkedIn);
                        dialog.dismiss();
                    }
                });

        dialog.findViewById(R.id.wordpress).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        openURL(strWordPress);
                        dialog.dismiss();
                    }
                });

        dialog.findViewById(R.id.github).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                       openURL(strGithub);
                        dialog.dismiss();
                    }
                });

        dialog.show();
    }

    public void openFaceBookPage()
    {
        String facebookUrl = "https://www.facebook.com/jaimin.prajapati.33";
        try {
            int versionCode = getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) {
                Uri uri = Uri.parse("fb://facewebmodal/f?href=" + facebookUrl);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            } else {
                // open the Facebook app using the old method (fb://profile/id or fb://page/id)
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/336227679757310")));
            }
        } catch (PackageManager.NameNotFoundException e) {
            // Facebook is not installed. Open the browser
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
        }
    }

    public void openURL(String url)
    {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void BackToMain(View view)
    {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}

