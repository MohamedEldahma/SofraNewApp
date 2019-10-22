package com.example.sofranewapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.sofranewapp.R;
import com.example.sofranewapp.data.model.Generated.GeneratedItem;

import java.util.List;

import butterknife.ButterKnife;

public class AdapterListItemsOrder extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    List<GeneratedItem> spinnnerArrayList;

    public AdapterListItemsOrder(Context context, List<GeneratedItem> spinnerArrayList) {
        this.context = context;

        this.spinnnerArrayList = spinnerArrayList;
    }

    @Override
    public int getCount() {
        return spinnnerArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return spinnnerArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    class ViewHolder {
        TextView setAdapterListTvNameItemsOrder, setAdapterListTvPriceItemsOrder;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.set_adapter_list_items_order_sofra, parent, false);

            viewHolder = new ViewHolder(convertView);

            viewHolder.setAdapterListTvNameItemsOrder = convertView.findViewById(R.id.setAdapterListTvNameItemsOrder);
            viewHolder.setAdapterListTvPriceItemsOrder = convertView.findViewById(R.id.setAdapterListTvPriceItemsOrder);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.setAdapterListTvNameItemsOrder.setText(spinnnerArrayList.get(position).getName());
        double total = spinnnerArrayList.get(position).getPivot().getQuantity() * spinnnerArrayList.get(position).getPivot().getPrice();
        viewHolder.setAdapterListTvPriceItemsOrder.setText(total + " LE");

        return convertView;
    }


}
