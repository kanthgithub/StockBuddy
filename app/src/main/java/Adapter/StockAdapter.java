package Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jaipr.stockbuddy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jaipr on 13-03-2017.
 */

public class StockAdapter extends ArrayAdapter<String> {

    JSONArray jsonArray;
    Context context;
    LayoutInflater inflater;

    public StockAdapter(Context context, JSONObject jsonObject) {
        super(context, R.layout.listview_stock_list);
        try {
            this.context=context;
            this.jsonArray=jsonObject.getJSONArray("Stock");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return jsonArray.length();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null)
        {
            inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_stock_list, null);
        }

        final ViewHolder holder=new ViewHolder();
        holder.txtSymbol= (TextView) convertView.findViewById(R.id.stock_symbol);
        holder.txtVolume= (TextView) convertView.findViewById(R.id.stock_volume);
        holder.txtPrice= (TextView) convertView.findViewById(R.id.stock_price);
        holder.txtChange= (TextView) convertView.findViewById(R.id.stock_change);
        holder.imgUpDown= (ImageView) convertView.findViewById(R.id.stock_up_down);
        holder.changeLayout= (RelativeLayout) convertView.findViewById(R.id.changeInStock);

        try {
            JSONObject jsonObject=jsonArray.getJSONObject(position);
            holder.txtSymbol.setText(jsonObject.get("Symbol").toString());
            holder.txtVolume.setText("Volume : "+jsonObject.get("Volume").toString());
            holder.txtPrice.setText("$"+jsonObject.get("Price").toString());
            holder.txtChange.setText(jsonObject.get("Change").toString()+"%");
            String change=jsonObject.get("Change").toString();
            if(change.charAt(0)=='-')
            {
                holder.imgUpDown.setImageResource(R.drawable.down);
                holder.changeLayout.setBackgroundResource(R.drawable.red_shape);
            }
            else {
                holder.imgUpDown.setImageResource(R.drawable.up);
                holder.changeLayout.setBackgroundResource(R.drawable.green_shape);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public class ViewHolder {
        TextView txtSymbol;
        TextView txtVolume;
        TextView txtPrice;
        TextView txtChange;
        ImageView imgUpDown;
        RelativeLayout changeLayout;
    }
}
