package com.example.sofranewapp.adapter;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.sofranewapp.R;
import com.example.sofranewapp.data.model.getOffersClient.DataItemOffers;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.sofranewapp.helper.HelperMathod.LodeImage;


public class GetOffersRestaurantClientRecyclerAdapter extends RecyclerView.Adapter<GetOffersRestaurantClientRecyclerAdapter.ViewHolder> {

    List<DataItemOffers> ordersArrayList;

    Activity context;


    private View itemLayoutView;
    private ViewHolder viewHolder;

    public GetOffersRestaurantClientRecyclerAdapter(List<DataItemOffers> ordersArrayList, Activity context) {
        this.ordersArrayList = ordersArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        // create a new view layout pending
        itemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.
                set_adapter_get_offers_restaurant_client_recycler_sofra, null);
        // create ViewHolder
        viewHolder = new ViewHolder(itemLayoutView);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        // set title
        viewHolder.setAdapterNewOfferTxv.setText(ordersArrayList.get(i).getName());


        LodeImage(context, ordersArrayList.get(i).getPhotoUrl(), viewHolder.setAdapterNewOfferImgPhoto);

    }


    @Override
    public int getItemCount() {
        return ordersArrayList.size();
    }


    // inner class to hold a reference to each item of RecyclerView
    class ViewHolder extends RecyclerView.ViewHolder {

        View view;

        @BindView(R.id.setAdapterNewOfferTxv)
        TextView setAdapterNewOfferTxv;
        @BindView(R.id.setAdapterNewOfferImgPhoto)
        ImageView setAdapterNewOfferImgPhoto;

        ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            view = itemLayoutView;
            ButterKnife.bind(this, view);
        }
    }

}
