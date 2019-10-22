package com.example.sofranewapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sofranewapp.R;
import com.example.sofranewapp.data.model.myOffers.Datum;

import java.util.List;

import static com.example.sofranewapp.helper.HelperMathod.LodeImageCircle;

public class OfferRestaurantAdapter extends BaseAdapter {

    public static List<Datum> requestBloodData;


    Activity context;


    public OfferRestaurantAdapter(List<Datum> postsArrayList, Activity context) {
         requestBloodData = postsArrayList;
        this.context = context;
    }



    @Override
    public int getCount() {
        return requestBloodData.size();
    }

    @Override
    public Object getItem(int position) {
        return requestBloodData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.set_adapter_new_offer_restaurant_sofra, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.setAdapterNewOfferTxv = convertView.findViewById(R.id.setAdapterNewOfferTxv);
            viewHolder.setAdapterNewOfferImgPhoto = convertView.findViewById(R.id.setAdapterNewOfferImgPhoto);


            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.setAdapterNewOfferTxv.setText(requestBloodData.get(i).getName());


        // method lode image view post
        LodeImageCircle(context, requestBloodData.get(i).getPhotoUrl(),
                viewHolder.setAdapterNewOfferImgPhoto);

        return convertView;
    }


      class ViewHolder {
         ImageView setAdapterNewOfferImgPhoto;
         TextView setAdapterNewOfferTxv;



    }
}
