package com.example.sofranewapp.adapter;

import android.app.Activity;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.example.sofranewapp.R;
import com.example.sofranewapp.data.api.APiSofraResturant;
import com.example.sofranewapp.data.api.RetrofitSofra;
import com.example.sofranewapp.data.model.Generated.GeneratedDataUser;
import com.example.sofranewapp.ui.fragment.user.ContentRestaurantComponentClientFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.sofranewapp.helper.HelperMathod.LodeImageCircle;
import static com.example.sofranewapp.helper.HelperMathod.getStartFragments;


public class ShowRestaurantClientRecyclerAdapter extends RecyclerView.Adapter<ShowRestaurantClientRecyclerAdapter.ViewHolder> {

    List<GeneratedDataUser> ordersArrayList;

    Activity context;

    private APiSofraResturant apiServerRestaurant;
    private View itemLayoutView;
    private ViewHolder viewHolder;
    public static Integer idRestaurant;
    public static String PhotoUrl;
    public static  String Name;

    public ShowRestaurantClientRecyclerAdapter(List<GeneratedDataUser> ordersArrayList, Activity context) {
        this.ordersArrayList = ordersArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        // create a new view layout pending
        itemLayoutView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.set_adapter_show_restaurant_client_recycler_sofra, null);
        // create ViewHolder
        viewHolder = new ViewHolder(itemLayoutView);


        /// get date MyItemRestaurantFragment
        apiServerRestaurant = RetrofitSofra.getRestaurant().create(APiSofraResturant.class);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        // set title
        viewHolder.setAdapterShowRestaurantClientTVNameRestaurant.setText(ordersArrayList.get(i).getName());
        viewHolder.setAdapterShowRestaurantClientTvMinimumRequest.setText(ordersArrayList.get(i).getMinimumCharger());
        viewHolder.setAdapterShowRestaurantClientTvDeliveryCost.setText(ordersArrayList.get(i).getDeliveryCost());
        viewHolder.setAdapterShowRestaurantClientRatingBarRestaurant.setRating(ordersArrayList.get(i).getRate());

        if (ordersArrayList.get(i).getAvailability().equals("open")) {

            viewHolder.setAdapterShowRestaurantClientImgPhotoOnline.setImageResource(R.drawable.icons_online);
            viewHolder.setAdapterShowRestaurantClientTvOnline.setText("Open");
        } else {
            viewHolder.setAdapterShowRestaurantClientImgPhotoOnline.setImageResource(R.drawable.icons_offline);
            viewHolder.setAdapterShowRestaurantClientTvOnline.setText("Close");

        }
        LodeImageCircle(context, ordersArrayList.get(i).getPhotoUrl(), viewHolder.setAdapterShowRestaurantClientImgPhotoRestaurant);

        viewHolder.setAdapterItemsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                idRestaurant = ordersArrayList.get(i).getId();
                PhotoUrl = ordersArrayList.get(i).getPhotoUrl();
                Name = ordersArrayList.get(i).getName();

                Bundle bundle = new Bundle();
                bundle.putInt("IdRestaurantFromCartOrShowAdapter", ordersArrayList.get(i).getId());


                Fragment fragment = new ContentRestaurantComponentClientFragment();
                Log.d("idRestaurantAdap", String.valueOf(ordersArrayList.get(i).getId()));

                fragment.setArguments(bundle);

                getStartFragments(((FragmentActivity) context).getSupportFragmentManager(), R.id.mainClientReplaceFragment, fragment);

            }
        });
    }


    @Override
    public int getItemCount() {
        return ordersArrayList.size();
    }


    // inner class to hold a reference to each item of RecyclerView
    class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        @BindView(R.id.setAdapterShowRestaurantClientTVNameRestaurant)
        TextView setAdapterShowRestaurantClientTVNameRestaurant;
        @BindView(R.id.setAdapterShowRestaurantClientRatingBarRestaurant)
        RatingBar setAdapterShowRestaurantClientRatingBarRestaurant;
        @BindView(R.id.setAdapterShowRestaurantClientTvMinimumRequest)
        TextView setAdapterShowRestaurantClientTvMinimumRequest;
        @BindView(R.id.setAdapterShowRestaurantClientTvDeliveryCost)
        TextView setAdapterShowRestaurantClientTvDeliveryCost;

        @BindView(R.id.setAdapterShowRestaurantClientImgPhotoRestaurant)
        ImageView setAdapterShowRestaurantClientImgPhotoRestaurant;
        @BindView(R.id.setAdapterShowRestaurantClientImgPhotoOnline)
        ImageView setAdapterShowRestaurantClientImgPhotoOnline;
        @BindView(R.id.setAdapterShowRestaurantClientTvOnline)
        TextView setAdapterShowRestaurantClientTvOnline;
        @BindView(R.id.setAdapterItemsCardView)
        CardView setAdapterItemsCardView;

        ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            view = itemLayoutView;
            ButterKnife.bind(this, view);
        }
    }

}
