package com.example.jaipr.stockbuddy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GraphActivity extends AppCompatActivity {

    List<LinearLayout> listLinearLayout;
    List<String> trendList;
    private Toolbar toolbar;
    private TextView textViewSymbol;
    private TextView textViewPrice;
    private TextView txtPridictPrice;
    private String symbol;
    private String price;
    private JSONObject jsonObject;
    private JSONObject predictjsonObject;
    private JSONArray jsonArray;
    private LinearLayout layoutDay1;
    private LinearLayout layoutDay2;
    private LinearLayout layoutDay3;
    private LinearLayout layoutDay4;
    private LinearLayout layoutDay5;
    private RelativeLayout.LayoutParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        this.setTitle("Prediction");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        Bundle bundle = getIntent().getExtras();
        symbol = bundle.getString("Symbol");
        price = bundle.getString("Price");

        textViewSymbol = (TextView) findViewById(R.id.txtSymbol);
        textViewPrice = (TextView) findViewById(R.id.txtPrice);
        txtPridictPrice = (TextView) findViewById(R.id.txtPridictPrice);

        textViewSymbol.setText(symbol);
        textViewPrice.setText("$ " + price);

        SharedPreferences sharedPrefereunces1 = this.getApplicationContext().getSharedPreferences("StockPrediction", Context.MODE_PRIVATE);
        try {
            jsonObject = new JSONObject(sharedPrefereunces1.getString("Prediction", null).toString());
            jsonArray = jsonObject.getJSONArray("data");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject _temp = null;
            try {
                _temp = jsonArray.getJSONObject(i);
                if (_temp.get("Symbol").toString().equals(symbol)) {
                    predictjsonObject = _temp;
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        layoutDay1 = (LinearLayout) findViewById(R.id.barDay1);
        layoutDay2 = (LinearLayout) findViewById(R.id.barDay2);
        layoutDay3 = (LinearLayout) findViewById(R.id.barDay3);
        layoutDay4 = (LinearLayout) findViewById(R.id.barDay4);
        layoutDay5 = (LinearLayout) findViewById(R.id.barDay5);

        try {
            txtPridictPrice.setText("$ " + predictjsonObject.get("Prediction").toString());
            String strtrend = predictjsonObject.get("Trend").toString();
            trendList = Arrays.asList(strtrend.split(","));
            listLinearLayout = new ArrayList<>();
            listLinearLayout.add(layoutDay1);
            listLinearLayout.add(layoutDay2);
            listLinearLayout.add(layoutDay3);
            listLinearLayout.add(layoutDay4);
            listLinearLayout.add(layoutDay5);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*deploy garph*/

        setGraph(Integer.parseInt(trendList.get(0)), listLinearLayout.get(0));
        setGraph(Integer.parseInt(trendList.get(1)), listLinearLayout.get(1));
        setGraph(Integer.parseInt(trendList.get(2)), listLinearLayout.get(2));
        setGraph(Integer.parseInt(trendList.get(3)), listLinearLayout.get(3));
        setGraph(Integer.parseInt(trendList.get(4)), listLinearLayout.get(4));
    }

    public void setGraph(int prediction, LinearLayout layout) {
        final float scale = this.getResources().getDisplayMetrics().density;

        if (prediction == 0) {
            params = (RelativeLayout.LayoutParams) layout.getLayoutParams();
            params.height = (int) (110 * scale + 0.5f);
            layout.setLayoutParams(params);
            layout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        } else if (prediction == 1) {
            params = (RelativeLayout.LayoutParams) layout.getLayoutParams();
            params.height = (int) (165 * scale + 0.5f);
            layout.setLayoutParams(params);
            layout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else {
            params = (RelativeLayout.LayoutParams) layout.getLayoutParams();
            params.height = (int) (55 * scale + 0.5f);
            layout.setLayoutParams(params);
            layout.setBackgroundColor(getResources().getColor(R.color.colorRed));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main_menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.aboutUs:
                intent = new Intent(this, AboutUsActivity.class);
                this.startActivity(intent);
                break;
            case R.id.logout:
                Logout();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void Logout() {
        try {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LoginData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putBoolean("isLogin", false);

            editor.commit();

            Intent intent = new Intent(this, FlashActivity.class);
            startActivity(intent);
        } catch (Exception e) {

        }
    }
}
