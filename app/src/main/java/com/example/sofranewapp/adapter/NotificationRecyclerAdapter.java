package com.example.sofranewapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.sofranewapp.R;
import com.example.sofranewapp.data.api.APiSofraResturant;
import com.example.sofranewapp.data.api.RetrofitSofra;
import com.example.sofranewapp.data.model.notifications.DataNotify;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;




public class NotificationRecyclerAdapter extends RecyclerView.Adapter<NotificationRecyclerAdapter.ViewHolder> {

    ArrayList<DataNotify> postsArrayList;

    private APiSofraResturant apiServer;
    Activity context;
    private boolean numFavorite = true;
    private int postion;

    public NotificationRecyclerAdapter(ArrayList<DataNotify> postsArrayList, Activity context) {
        this.postsArrayList = postsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.set_adapter_recycler_notifcation_sofra, null);
        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        apiServer = RetrofitSofra.getRestaurant().create(APiSofraResturant.class);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        // set title
        viewHolder.notificationAdapterTitleTxt.setText(postsArrayList.get(i).getTitle());
        viewHolder.notificationAdapterTimeTxt.setText(postsArrayList.get(i).getUpdatedAt());
//
//        if (postsArrayList.get(i).getPivotNotify().getIsRead().equals("0")) {
//            viewHolder.notification_adapter.setImageResource(R.drawable.icon_notify);
//        } else {
//            viewHolder.notification_adapter.setImageResource(R.drawable.icon_un_notify);
//        }

        viewHolder.notificationAdapterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//
//                bundle.putString("getDonationRequestId", postsArrayList.get(i).getDonationRequestId());
//                bundle.putInt("returnResult", 1);
//                Fragment fragment = new ContentDonationFragment();
//                fragment.setArguments(bundle);
//                getStartFragments(((FragmentActivity) context).getSupportFragmentManager(), R.id.mainRestaurantReplaceFragment
//                        , fragment);
             }
        });
    }


    @Override
    public int getItemCount() {
        return postsArrayList.size();
    }

    // inner class to hold a reference to each item of RecyclerView
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.notification_adapter)
        ImageView notification_adapter;
        @BindView(R.id.notificationAdapterTimeTxt)
        TextView notificationAdapterTimeTxt;
        @BindView(R.id.notificationAdapterTitleTxt)
        TextView notificationAdapterTitleTxt;
        @BindView(R.id.notificationAdapterLayout)
        LinearLayout notificationAdapterLayout;

        View view;

        ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            view = itemLayoutView;
            ButterKnife.bind(this, view);
        }
    }

}
