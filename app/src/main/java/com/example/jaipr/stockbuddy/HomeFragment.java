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
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import Adapter.StockAdapter;
import Controller.StockAPI;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ListView list;
    private SwipeRefreshLayout swipeRefreshLayout;
    private JSONObject jsonObject;
    private StockAdapter stockAdapter;
    private StockAPI stockAPI;

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
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);

        stockAPI = new StockAPI();

        String[] symbols = new String[]{"INTC", "FB", "TSLA", "NKE", "YHOO", "AMZN", "TCS", "MSFT"};
        if (isNetworkAvailable()) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("StockSymbol", Context.MODE_PRIVATE);
            boolean[] _result = getSymbolStatus(symbols);
            //jsonObject = stockAPI.getStock(symbols, _result);
            SharedPreferences sharedPreferences1 = getActivity().getApplicationContext().getSharedPreferences("StockSymbol", Context.MODE_PRIVATE);
            try {
                jsonObject = new JSONObject(sharedPreferences1.getString("StockJSON", null).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            list = (ListView) view.findViewById(R.id.list);
            stockAdapter = new StockAdapter(getActivity(), jsonObject);
            list.setAdapter(stockAdapter);

            swipeRefreshLayout.setOnRefreshListener(this);

            /**
             * Showing Swipe Refresh animation on activity create
             * As animation won't start on onCreate, post runnable is used
             */

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
        } else {

        }

        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        fetchStock();
                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                }
        );
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

    @Override
    public void onRefresh() {
        fetchStock();
    }

    public void fetchStock() {
        swipeRefreshLayout.setRefreshing(true);
        String[] symbols = new String[]{"INTC", "FB", "TSLA", "NKE", "YHOO", "AMZN", "TCS", "MSFT"};
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("StockSymbol", Context.MODE_PRIVATE);
        boolean[] _result = getSymbolStatus(symbols);
        if (isNetworkAvailable()) {
            jsonObject = stockAPI.getStock(symbols, _result);
            stockAdapter = new StockAdapter(getActivity(), jsonObject);
            list.setAdapter(stockAdapter);
        }
        swipeRefreshLayout.setRefreshing(false);
    }
}
