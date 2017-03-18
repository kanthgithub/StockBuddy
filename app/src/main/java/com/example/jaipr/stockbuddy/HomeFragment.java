package com.example.jaipr.stockbuddy; /**
 * Created by jaipr on 25-02-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import Adapter.StockAdapter;
import Controller.StockAPI;

public class HomeFragment extends Fragment {

    ListView list;

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

        StockAPI stockAPI=new StockAPI();

        String[] symbols = new String[]{"INTC", "FB", "TSLA", "NKE", "YHOO", "AMZN", "TCS", "MSFT"};
        if(isNetworkAvailable())
        {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("StockSymbol", Context.MODE_PRIVATE);
            boolean[] _result = getSymbolStatus(symbols);
            final JSONObject jsonObject = stockAPI.getStock(symbols, _result);
            list= (ListView) view.findViewById(R.id.list);
            StockAdapter stockAdapter = new StockAdapter(getActivity(), jsonObject);
            list.setAdapter(stockAdapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String message = null;
                    try {
                        message = jsonObject.getJSONArray("Stock").getJSONObject(position).get("Symbol").toString();
                        Intent intent = new Intent(getContext(), StockActivity.class);
                        intent.putExtra("Symbol", message);
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        else {

        }
        return view;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean[] getSymbolStatus(String[] symbols) {
        boolean[] _symbol = new boolean[symbols.length];
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("StockSymbol", Context.MODE_PRIVATE);
        for (int i = 0; i < symbols.length; i++) {
            _symbol[i] = sharedPreferences.getBoolean(symbols[i], false);
        }
        return _symbol;
    }
}
