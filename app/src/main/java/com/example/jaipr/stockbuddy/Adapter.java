package com.example.jaipr.stockbuddy;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.icu.math.BigDecimal;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jaipr on 13-03-2017.
 */

public class Adapter extends ArrayAdapter<String>{

    JSONArray jsonArray;
    Context context;
    LayoutInflater inflater;
    public Adapter(Context context, JSONObject jsonObject) {
        super(context, R.layout.stock_list_layout);
        try {
            this.context=context;
            this.jsonArray=jsonObject.getJSONArray("Stock");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class ViewHolder{
        TextView txtSymbol;
        TextView txtVolume;
        TextView txtPrice;
        TextView txtChange;
        ImageView imgUpDown;
        RelativeLayout changeLayout;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null)
        {
            inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.stock_list_layout,null);
        }

        final ViewHolder holder=new ViewHolder();
        holder.txtSymbol= (TextView) convertView.findViewById(R.id.stock_symbol);
        holder.txtVolume= (TextView) convertView.findViewById(R.id.stock_volume);
        holder.txtPrice= (TextView) convertView.findViewById(R.id.stock_price);
        holder.txtChange= (TextView) convertView.findViewById(R.id.stock_price);
        holder.imgUpDown= (ImageView) convertView.findViewById(R.id.stock_up_down);
        holder.changeLayout= (RelativeLayout) convertView.findViewById(R.id.changeInStock);

        try {
            JSONObject jsonObject=jsonArray.getJSONObject(position);
            holder.txtSymbol.setText(jsonObject.get("Symbol").toString());
            holder.txtVolume.setText(jsonObject.get("Volume").toString());
            holder.txtPrice.setText(jsonObject.get("Price").toString());
            holder.txtChange.setText(jsonObject.get("Price").toString());
            holder.imgUpDown.setImageResource(R.drawable.down);
            holder.changeLayout.setBackgroundResource(R.drawable.red_shape);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
