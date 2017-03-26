package com.example.jaipr.stockbuddy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class GraphActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
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
