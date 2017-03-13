package com.example.jaipr.stockbuddy; /**
 * Created by jaipr on 25-02-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Bean.StockJSON;
import yahoofinance.Stock;

public class HomeFragment extends Fragment {

    Stock stock;
    String urlString="http://finance.google.com/finance/info?client=ig&q=NASDAQ%3AAAPL,GOOG,FB";

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        TextView textView=(TextView) view.findViewById(R.id.textStock);
        StockAPI stockAPI=new StockAPI();
        List<String> symbolList = new ArrayList<String>();
        symbolList.add("FB");
        symbolList.add("INTC");
        symbolList.add("MSFT");
        symbolList.add("NKE");
        symbolList.add("AMZN");
        if(isNetworkAvailable())
        {
            JSONObject jsonObject=stockAPI.getStock(symbolList);
            String jsonStr = jsonObject.toString();
            textView.setText(jsonStr);
        }
        else {
            textView.setText("No Connection !");
        }
        return view;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
