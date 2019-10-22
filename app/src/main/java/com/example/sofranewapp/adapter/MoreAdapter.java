package com.example.sofranewapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sofranewapp.R;
import com.example.sofranewapp.data.model.Generated.GeneratedModelMore;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoreAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;

    List<GeneratedModelMore> spinnnerArrayList;

    public MoreAdapter(Context context, List<GeneratedModelMore> spinnerArrayList) {
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



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.set_adapter_more_sofra, parent, false);

            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.setAdapterImgIconMore.setImageResource(spinnnerArrayList.get(position).getImg());
        viewHolder.setAdapterTvMore.setText(spinnnerArrayList.get(position).getName());

        return convertView;
    }


    class ViewHolder {
        @BindView(R.id.setAdapterImgIconMore)
        ImageView setAdapterImgIconMore;
        @BindView(R.id.setAdapterTvMore)
        TextView setAdapterTvMore;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
