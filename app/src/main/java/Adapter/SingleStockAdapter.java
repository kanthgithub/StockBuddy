package Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jaipr.stockbuddy.R;

/**
 * Created by jaipr on 18-03-2017.
 */

public class SingleStockAdapter extends ArrayAdapter<String> {

    String[] key;
    String[] value;
    Context context;
    LayoutInflater inflater;

    public SingleStockAdapter(Context context, String[] key, String[] value) {
        super(context, R.layout.listview_single_stock);

        this.context = context;
        this.key = key;
        this.value = value;
    }

    public int getCount() {
        return key.length;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_single_stock, null);
        }

        final SingleStockAdapter.ViewHolder holder = new SingleStockAdapter.ViewHolder();
        holder.txtKey = (TextView) convertView.findViewById(R.id.title);
        holder.txtValue = (TextView) convertView.findViewById(R.id.value);

        holder.txtKey.setText(key[position]);
        holder.txtValue.setText(value[position]);

        return convertView;
    }

    public class ViewHolder {
        TextView txtKey;
        TextView txtValue;
    }


}


