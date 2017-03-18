package com.example.jaipr.stockbuddy;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import Adapter.SymbolAdapter;

/**
 * Created by jaipr on 25-02-2017.
 */

public class SearchFragment extends Fragment {

    private ListView listView;

    public SearchFragment() {
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);


        final String[] symbols = new String[]{"INTC", "FB", "TSLA", "NKE", "YHOO", "AMZN", "TCS", "MSFT"};
        String[] symbolsName = new String[]{"INTC", "FB", "TSLA", "NKE", "YHOO", "AMZN", "TCS", "MSFT"};
        boolean[] isSymbolAdd = getSymbolStatus(symbols);
        listView = (ListView) view.findViewById(R.id.symbol_list);
        SymbolAdapter symbolAdapter = new SymbolAdapter(getActivity(), symbols, symbolsName, isSymbolAdd, getActivity());
        listView.setAdapter(symbolAdapter);

        return view;
    }

    public boolean[] getSymbolStatus(String[] symbols) {
        boolean[] _symbol = new boolean[symbols.length];
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("StockSymbol", Context.MODE_PRIVATE);
        for (int i = 0; i < symbols.length; i++) {
            _symbol[i] = sharedPreferences.getBoolean(symbols[i], false);
        }
        return _symbol;
    }

}
