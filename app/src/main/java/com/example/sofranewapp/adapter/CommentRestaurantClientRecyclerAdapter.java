package com.example.sofranewapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.sofranewapp.R;
import com.example.sofranewapp.data.api.APiSofraResturant;
import com.example.sofranewapp.data.api.RetrofitSofra;
import com.example.sofranewapp.data.model.showReviews.DataComment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;




public class CommentRestaurantClientRecyclerAdapter extends RecyclerView.Adapter<CommentRestaurantClientRecyclerAdapter.ViewHolder> {

    List<DataComment> dataCommentList;

    Activity context;

    private APiSofraResturant apiServerRestaurant;
    String keyRequest;
    private View itemLayoutView;
    private ViewHolder viewHolder;

    public CommentRestaurantClientRecyclerAdapter(List<DataComment> dataCommentList, Activity context) {
        this.dataCommentList = dataCommentList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        // create a new view layout pending
        itemLayoutView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.set_adapter_comment_restaurant_client_recycler_sofra, null);
        // create ViewHolder
        viewHolder = new ViewHolder(itemLayoutView);


        /// get date MyItemRestaurantFragment
        apiServerRestaurant = RetrofitSofra.getRestaurant().create(APiSofraResturant.class);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        // set title
        viewHolder.setAdapterCommentRestaurantClientTVNameUser.setText(dataCommentList.get(i).getClient().getName());
        viewHolder.setAdapterCommentRestaurantClientTvComment.setText(dataCommentList.get(i).getComment());

        if (dataCommentList.get(i).getRate().equals("1")) {
            viewHolder.setAdapterCommentRestaurantClientImgPhotoStatus.setImageResource(R.drawable.angry_active1);
        } else if (dataCommentList.get(i).getRate().equals("2")) {
            viewHolder.setAdapterCommentRestaurantClientImgPhotoStatus.setImageResource(R.drawable.sad_active2);

        } else if (dataCommentList.get(i).getRate().equals("3")) {
            viewHolder.setAdapterCommentRestaurantClientImgPhotoStatus.setImageResource(R.drawable.smiling_active3);

        } else if (dataCommentList.get(i).getRate().equals("4")) {
            viewHolder.setAdapterCommentRestaurantClientImgPhotoStatus.setImageResource(R.drawable.smiley_active4);

        } else if (dataCommentList.get(i).getRate().equals("5")) {
            viewHolder.setAdapterCommentRestaurantClientImgPhotoStatus.setImageResource(R.drawable.love_active5);

        }


    }


    @Override
    public int getItemCount() {
        return dataCommentList.size();
    }


    // inner class to hold a reference to each item of RecyclerView
    class ViewHolder extends RecyclerView.ViewHolder {

        View view;

        @BindView(R.id.setAdapterCommentRestaurantClientTVNameUser)
        TextView setAdapterCommentRestaurantClientTVNameUser;
        @BindView(R.id.setAdapterCommentRestaurantClientTvComment)
        TextView setAdapterCommentRestaurantClientTvComment;
        @BindView(R.id.setAdapterCommentRestaurantClientImgPhotoStatus)
        ImageView setAdapterCommentRestaurantClientImgPhotoStatus;


        ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            view = itemLayoutView;
            ButterKnife.bind(this, view);
        }
    }

}
