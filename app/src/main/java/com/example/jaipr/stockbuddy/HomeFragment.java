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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
            final JSONObject jsonObject = stockAPI.getStock(symbols);
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
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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
}
