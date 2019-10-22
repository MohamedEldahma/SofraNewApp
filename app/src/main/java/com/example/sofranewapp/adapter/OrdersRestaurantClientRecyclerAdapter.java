package com.example.sofranewapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sofranewapp.R;
import com.example.sofranewapp.data.api.APiSofraClint;
import com.example.sofranewapp.data.api.RetrofitSofra;
import com.example.sofranewapp.data.model.myOrders.Datum;
import com.example.sofranewapp.data.model.rejectOrder.RejectOrder;
import com.example.sofranewapp.ui.fragment.user.DetailsOrderRestaurantClientFragment;
import com.example.sofranewapp.ui.fragment.user.RequestsOrderRestaurantClientComponentFragment;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofranewapp.data.local.SharedPreferncesMangerClient.LoadData;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerClient.USER_API_TOKEN;
import static com.example.sofranewapp.helper.HelperMathod.LodeImageCircle;
import static com.example.sofranewapp.helper.HelperMathod.dismissProgressDialog;
import static com.example.sofranewapp.helper.HelperMathod.getStartFragments;
import static com.example.sofranewapp.helper.HelperMathod.showProgressDialog;

public class OrdersRestaurantClientRecyclerAdapter extends RecyclerView.Adapter<OrdersRestaurantClientRecyclerAdapter.ViewHolder> {

    ArrayList<Datum> ordersArrayList;

    Activity context;

    private RequestsOrderRestaurantClientComponentFragment requestsRestaurantComponentFragment;

    private APiSofraClint apiServerClient;
    String keyRequest;
    private View itemLayoutView;
    private ViewHolder viewHolder;

    public OrdersRestaurantClientRecyclerAdapter(ArrayList<Datum> ordersArrayList, Activity context
            , RequestsOrderRestaurantClientComponentFragment requestsRestaurantComponentFragment, String keyRequest) {
        this.ordersArrayList = ordersArrayList;
        this.context = context;
        this.requestsRestaurantComponentFragment = requestsRestaurantComponentFragment;
        this.keyRequest = keyRequest;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        // create a new view layout pending
        itemLayoutView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.set_adapter_orders_request_restaurant_client_recycler_sofra, null);
        // create ViewHolder
        viewHolder = new ViewHolder(itemLayoutView);

        /// get date MyItemRestaurantFragment
        apiServerClient = RetrofitSofra.getRestaurant().create(APiSofraClint.class);

        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        // set title
        viewHolder.setAdapterOrdersRequestClientNameClientTxv.setText(ordersArrayList.get(i).getRestaurant().getName());
        viewHolder.setAdapterOrdersRequestClientNumberOrdersTxv.setText(""+ordersArrayList.get(i).getId());

        viewHolder.setAdapterOrdersRequestClientTotalClientTxv.setText(ordersArrayList.get(i).getTotal()+" LE");

        if (keyRequest.equals("current")) {

             viewHolder.setAdapterOrdersRequestClientBtnClientReject.setVisibility(View.VISIBLE);
            viewHolder.setAdapterOrdersRequestClientBtnClientConfirmOrder.setVisibility(View.VISIBLE);

        } else if (keyRequest.equals("completed")) {

            viewHolder.setAdapterOrdersRequestClientBtnClientReject.setVisibility(View.GONE);
            viewHolder.setAdapterOrdersRequestClientBtnClientConfirmOrder.setVisibility(View.GONE);

        }


//         method lode image view post
        LodeImageCircle(context, ordersArrayList.get(i).getRestaurant().getPhotoUrl(), viewHolder.setAdapterOrdersRequestClientImgClientPhoto);


        if (keyRequest.equals("current")) {

            viewHolder.setAdapterOrdersRequestClientBtnClientReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Integer id = ordersArrayList.get(i).getId();
                        showProgressDialog(context, "please wait");
                        apiServerClient.rejectOrder(LoadData(context, USER_API_TOKEN), id).enqueue(new Callback<RejectOrder>() {
                            @Override
                            public void onResponse(Call<RejectOrder> call, Response<RejectOrder> response) {
                                dismissProgressDialog();
                                try {
                                    if (response.body().getStatus() == 1) {

                                        Log.d("response", response.body().getMsg());

                                        requestsRestaurantComponentFragment.requestsRestaurantFragmentAccept.ordersArrayList.add(0, ordersArrayList.get(i));

                                        ordersArrayList.remove(i);

                                        requestsRestaurantComponentFragment.requestsRestaurantFragmentAccept.restaurantClientRecyclerAdapter.notifyDataSetChanged();

                                        notifyDataSetChanged();

                                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    e.getMessage();
                                }
                            }

                            @Override
                            public void onFailure(Call<RejectOrder> call, Throwable t) {
                                Log.d("onFailure", t.getMessage());
                            }
                        });
                    } catch (Exception e) {
                        Log.e("Demo Exception", e.getMessage());
                    }

                }
            });
            viewHolder.setAdapterOrdersRequestClientBtnClientConfirmOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Integer id = ordersArrayList.get(i).getId();
                            showProgressDialog(context, "please wait");
                        apiServerClient.confirmOrder(LoadData(context, USER_API_TOKEN), id).enqueue(new Callback<RejectOrder>() {
                            @Override
                            public void onResponse(Call<RejectOrder> call, Response<RejectOrder> response) {
                                dismissProgressDialog();
                                try {
                                    if (response.body().getStatus() == 1) {

                                        Log.d("response", response.body().getMsg());
                                        requestsRestaurantComponentFragment.requestsRestaurantFragmentAccept.ordersArrayList.add(0, ordersArrayList.get(i));
                                        ordersArrayList.remove(i);

                                        requestsRestaurantComponentFragment.requestsRestaurantFragmentAccept.restaurantClientRecyclerAdapter.notifyDataSetChanged();
                                        notifyDataSetChanged();
                                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    e.getMessage();
                                }
                            }

                            @Override
                            public void onFailure(Call<RejectOrder> call, Throwable t) {
                                Log.d("onFailure", t.getMessage());
                            }
                        });
                    } catch (Exception e) {
                        Log.e("Demo Exception", e.getMessage());
                    }
                }
            });


        }
        viewHolder.setAdapterOrdersRequestClientItemsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("getId", ordersArrayList.get(i).getId());

                // start fragment  Details Order Restaurant Client
                Fragment fragment = new DetailsOrderRestaurantClientFragment();
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
        @BindView(R.id.setAdapterOrdersRequestClientImgClientPhoto)
        ImageView setAdapterOrdersRequestClientImgClientPhoto;
        @BindView(R.id.setAdapterOrdersRequestClientNameClientTxv)
        TextView setAdapterOrdersRequestClientNameClientTxv;
        @BindView(R.id.setAdapterOrdersRequestClientNumberOrdersTxv)
        TextView setAdapterOrdersRequestClientNumberOrdersTxv;
        @BindView(R.id.setAdapterOrdersRequestClientTotalClientTxv)
        TextView setAdapterOrdersRequestClientTotalClientTxv;
        @BindView(R.id.setAdapterOrdersRequestClientBtnClientConfirmOrder)
        Button setAdapterOrdersRequestClientBtnClientConfirmOrder;
        @BindView(R.id.setAdapterOrdersRequestClientBtnClientReject)
        Button setAdapterOrdersRequestClientBtnClientReject;
        @BindView(R.id.setAdapterOrdersRequestClientItemsCardView)
        CardView setAdapterOrdersRequestClientItemsCardView;
        View view;


        ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            view = itemLayoutView;
            ButterKnife.bind(this, view);
        }
    }

}
