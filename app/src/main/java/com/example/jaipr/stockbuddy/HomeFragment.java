package com.example.jaipr.stockbuddy; /**
 * Created by jaipr on 25-02-2017.
 */

import android.content.Context;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import yahoofinance.Stock;

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
        List<String> symbolList = new ArrayList<String>();
        symbolList.add("FB");
        symbolList.add("INTC");
        symbolList.add("MSFT");
        symbolList.add("NKE");
        symbolList.add("AMZN");
        if(isNetworkAvailable())
        {
            final JSONObject jsonObject=stockAPI.getStock(symbolList);
            list= (ListView) view.findViewById(R.id.list);
            Adapter adapter=new Adapter(getActivity(),jsonObject);
            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getContext(),"hello",Toast.LENGTH_SHORT).show();
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
