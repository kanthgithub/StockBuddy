package com.example.jaipr.stockbuddy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by jaipr on 25-02-2017.
 */

public class SearchFragment extends Fragment {

    private  ListView listView;
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
        View view= inflater.inflate(R.layout.fragment_search, container, false);

        String[] mobileArray = {"INTC", "FB", "TSLA", "NKE", "YHOO", "AMZN", "TCS", "MSFT"};
        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), R.layout.listview_symbol_list, mobileArray);
        listView = (ListView) view.findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);
        return view;
    }

}
