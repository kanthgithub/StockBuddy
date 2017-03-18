package Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaipr.stockbuddy.R;

/**
 * Created by jaipr on 18-03-2017.
 */

public class SymbolAdapter extends ArrayAdapter<String> {

    String[] symbol;
    String[] symbolName;
    boolean[] isSymbolAdd;
    Context context;
    LayoutInflater inflater;
    Activity _getApplicationContext;

    public SymbolAdapter(Context context, String[] symbol, String[] symbolName, boolean[] isSymbolAdd, Activity _getApplicationContext) {
        super(context, R.layout.listview_symbol_list);
        this.context = context;
        this.symbol = symbol;
        this.symbolName = symbolName;
        this.isSymbolAdd = isSymbolAdd;
        this._getApplicationContext = _getApplicationContext;
    }

    @Override
    public int getCount() {
        return symbol.length;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_symbol_list, null);
        }

        final SymbolAdapter.ViewHolder holder = new SymbolAdapter.ViewHolder();
        holder.txtSymbol = (TextView) convertView.findViewById(R.id.text_symbol);
        holder.txtSymbolName = (TextView) convertView.findViewById(R.id.text_symbol_name);
        holder.btnAddRemove = (Button) convertView.findViewById(R.id.btn_add_remove);

        holder.txtSymbol.setText(symbol[position]);
        holder.txtSymbolName.setText(symbolName[position]);

        if (isSymbolAdd[position] == true) {
            holder.btnAddRemove.setText("Remove");
        } else {
            holder.btnAddRemove.setText("Add");
        }

        holder.btnAddRemove.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = _getApplicationContext.getApplicationContext().getSharedPreferences("StockSymbol", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (sharedPreferences.getBoolean(symbol[position], false) == true) {
                    editor.putBoolean(symbol[position], false);
                    editor.commit();
                    holder.btnAddRemove.setText("Add");
                } else {
                    editor.putBoolean(symbol[position], true);
                    editor.commit();
                    holder.btnAddRemove.setText("Remove");
                }

                Toast.makeText(getContext(), symbol[position], Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    public class ViewHolder {
        TextView txtSymbol;
        TextView txtSymbolName;
        Button btnAddRemove;
    }

}
