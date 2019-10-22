package com.example.sofranewapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sofranewapp.R;
import com.example.sofranewapp.data.model.Generated.GeneratedModelSpinner;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpinnerAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;

    ArrayList<GeneratedModelSpinner> spinnnerArrayList;

    public SpinnerAdapter(Context context, ArrayList<GeneratedModelSpinner> spinnerArrayList) {
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
        @BindView(R.id.tvAdapterSpinner)
        TextView adapterSpinnerTex;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView =  LayoutInflater.from(context).inflate(R.layout.set_adapter_spinner_cite_sofra, parent, false);

            viewHolder = new ViewHolder(convertView);

            viewHolder.adapterSpinnerTex = convertView.findViewById(R.id.tvAdapterSpinner);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.adapterSpinnerTex.setText(spinnnerArrayList.get(position).getName());

        return convertView;
    }


}
