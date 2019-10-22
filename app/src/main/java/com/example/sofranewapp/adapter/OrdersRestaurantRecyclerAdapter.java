package com.example.sofranewapp.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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
import com.example.sofranewapp.data.api.APiSofraResturant;
import com.example.sofranewapp.data.api.RetrofitSofra;
import com.example.sofranewapp.data.model.acceptOrder.AcceptOrder;
import com.example.sofranewapp.data.model.myOrders.Datum;
import com.example.sofranewapp.data.model.rejectOrder.RejectOrder;
import com.example.sofranewapp.ui.fragment.resturant.DetailsOrderRestaurantFragment;
import com.example.sofranewapp.ui.fragment.resturant.RequestsRestaurantComponentFragment;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.LoadData;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.USER_API_TOKEN;
import static com.example.sofranewapp.helper.HelperMathod.dismissProgressDialog;
import static com.example.sofranewapp.helper.HelperMathod.getStartFragments;
import static com.example.sofranewapp.helper.HelperMathod.showProgressDialog;

public class OrdersRestaurantRecyclerAdapter extends RecyclerView.Adapter<OrdersRestaurantRecyclerAdapter.ViewHolder> {

    ArrayList<Datum> ordersArrayList;

    Activity context;
    private static final Integer CALL = 0x2;
    private RequestsRestaurantComponentFragment requestsRestaurantComponentFragment;

    private APiSofraResturant apiServerRestaurant;
    String keyRequest;
    private View itemLayoutView;
    private ViewHolder viewHolder;

    public OrdersRestaurantRecyclerAdapter(ArrayList<Datum> ordersArrayList, Activity context
            , RequestsRestaurantComponentFragment requestsRestaurantComponentFragment, String keyRequest) {
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
                .inflate(R.layout.set_adapter_orders_restaurant_recycler_sofra, null);
        // create ViewHolder
        viewHolder = new ViewHolder(itemLayoutView);

        /// get date MyItemRestaurantFragment
        apiServerRestaurant = RetrofitSofra. getRestaurant().create(APiSofraResturant.class);

        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        // set title
        viewHolder.setAdapterNewOrdersNameClientTxv.setText(ordersArrayList.get(i).getClient().getName());
        viewHolder.setAdapterNewOrdersTotalClientTxv.setText(ordersArrayList.get(i).getTotal());
        viewHolder.setAdapterNewOrdersAddressClientTxv.setText(ordersArrayList.get(i).getClient().getAddress());
        viewHolder.setAdapterNumberOrdersTxv.setText(String.valueOf(ordersArrayList.get(i).getId()));

        if (keyRequest.equals("pending")) {

            viewHolder.setAdapterNewOrdersBtnClientCall.setText(context.getResources().getString(R.string.phone));
            viewHolder.setAdapterNewOrdersBtnClientCall.setVisibility(View.VISIBLE);
            viewHolder.setAdapterNewOrdersBtnClientReject.setVisibility(View.VISIBLE);
            viewHolder.setAdapterNewOrdersBtnClientAccept.setVisibility(View.VISIBLE);

        } else if (keyRequest.equals("current")) {

            viewHolder.setAdapterNewOrdersBtnClientCall.setText(context.getResources().getString(R.string.phone));
            viewHolder.setAdapterNewOrdersBtnClientAccept.setText(context.getResources().getString(R.string.confirm_delivery));
            viewHolder.setAdapterNewOrdersBtnClientCall.setVisibility(View.VISIBLE);
            viewHolder.setAdapterNewOrdersBtnClientReject.setVisibility(View.GONE);
            viewHolder.setAdapterNewOrdersBtnClientAccept.setVisibility(View.VISIBLE);

        } else if (keyRequest.equals("completed")) {

            viewHolder.setAdapterNewOrdersBtnClientCall.setVisibility(View.GONE);
            viewHolder.setAdapterNewOrdersBtnClientReject.setVisibility(View.GONE);
            viewHolder.setAdapterNewOrdersBtnClientAccept.setVisibility(View.VISIBLE);
            viewHolder.setAdapterNewOrdersBtnClientCall.setText(ordersArrayList.get(i).getState());

            if (ordersArrayList.get(i).getState().equals("delivered")) {
                viewHolder.setAdapterNewOrdersBtnClientAccept.setBackground(context.getDrawable(R.drawable.shape_btn_accept));
                viewHolder.setAdapterNewOrdersBtnClientAccept.setText(ordersArrayList.get(i).getState());

            } else if (ordersArrayList.get(i).getState().equals("rejected"))  {
                viewHolder.setAdapterNewOrdersBtnClientAccept.setBackground(context.getDrawable(R.drawable.shape_btn_call));
                viewHolder.setAdapterNewOrdersBtnClientAccept.setText(ordersArrayList.get(i).getState());
            }
        }


//         method lode image view post
//        LodeImageCircle(context, dataCommentList.get(i).getDataItem().getLastPageUrl(), viewHolder.setAdapterNewOrdersImgClientPhoto);


        if (keyRequest.equals("pending")) {

            viewHolder.setAdapterNewOrdersBtnClientAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        Integer id = ordersArrayList.get(i).getId();
                        geNewOrder(id, i);
                        showProgressDialog(context, "please wait");
                        apiServerRestaurant.acceptOrder(LoadData(context, USER_API_TOKEN), id).enqueue(new Callback<AcceptOrder>() {
                            @Override
                            public void onResponse(Call<AcceptOrder> call, Response<AcceptOrder> response) {
                                try {
                                    dismissProgressDialog();
                                    if (response.body().getStatus() == 1) {

                                        Log.d("response", response.body().getMsg());

                                        requestsRestaurantComponentFragment.requestsRestaurantFragmentPending.ordersArrayList.add(0, ordersArrayList.get(i));
                                        ordersArrayList.remove(i);

                                        requestsRestaurantComponentFragment.requestsRestaurantFragmentPending.restaurantRecyclerAdapter.notifyDataSetChanged();
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
                            public void onFailure(Call<AcceptOrder> call, Throwable t) {
                                dismissProgressDialog();
                                Log.d("onFailure", t.getMessage());
                            }
                        });
                    } catch (Exception e) {
                        Log.e("Demo Exception", e.getMessage());
                    }
                }
            });


            viewHolder.setAdapterNewOrdersBtnClientReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        Integer id = ordersArrayList.get(i).getId();
                        geNewOrder(id, i);
                        showProgressDialog(context,"please wait");
                        apiServerRestaurant.rejectOrder(LoadData(context, USER_API_TOKEN), id).enqueue(new Callback<RejectOrder>() {
                            @Override
                            public void onResponse(Call<RejectOrder> call, Response<RejectOrder> response) {
                                try {
                                    dismissProgressDialog();
                                    if (response.body().getStatus() == 1) {

                                        Log.d("response", response.body().getMsg());
                                        requestsRestaurantComponentFragment.requestsRestaurantFragmentPending.ordersArrayList.add(0, ordersArrayList.get(i));
                                         ordersArrayList.remove(i);
                                         notifyDataSetChanged();
                                        requestsRestaurantComponentFragment.requestsRestaurantFragmentPending.restaurantRecyclerAdapter.notifyDataSetChanged();
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

        } else if (keyRequest.equals("current")) {

            viewHolder.setAdapterNewOrdersBtnClientAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Integer id = ordersArrayList.get(i).getId();
                        geNewOrder(id, i);
                        showProgressDialog(context,"please wait");
                        apiServerRestaurant.confirmOrder(LoadData(context, USER_API_TOKEN), id).enqueue(new Callback<RejectOrder>() {
                            @Override
                            public void onResponse(Call<RejectOrder> call, Response<RejectOrder> response) {
                                dismissProgressDialog();
                                try {
                                    if (response.body().getStatus() == 1) {
                                        Log.d("response", response.body().getMsg());
                                        requestsRestaurantComponentFragment.requestsRestaurantFragmentAccept.ordersArrayList.add(0, ordersArrayList.get(i));
                                        ordersArrayList.remove(i);

                                        requestsRestaurantComponentFragment.requestsRestaurantFragmentAccept.restaurantRecyclerAdapter.notifyDataSetChanged();
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
        viewHolder.setAdapterNewOrdersBtnClientCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                            == PackageManager.PERMISSION_GRANTED) {

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + ordersArrayList.get(i).getClient().getPhone()));
                        context.startActivity(callIntent);

                    } else {
                        AslForPermission(CALL);
                    }

                } catch (Exception e) {
                    Log.e("Demo application", "Failed to invoke call", e);
                }
            }
        });
        viewHolder.setAdapterNewOrdersItemsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("getId", ordersArrayList.get(i).getId());
                bundle.putString("keyRequest", keyRequest);
                bundle.putInt("posation", i);
                bundle.putString("getPhone", ordersArrayList.get(i).getClient().getPhone());
                Fragment fragment = new DetailsOrderRestaurantFragment();
                fragment.setArguments(bundle);
                getStartFragments(((FragmentActivity) context).getSupportFragmentManager(), R.id.mainRestaurantReplaceFragment, fragment);

            }
        });
    }


    @Override
    public int getItemCount() {
        return ordersArrayList.size();
    }


    // inner class to hold a reference to each item of RecyclerView
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.setAdapterNewOrdersImgClientPhoto)
        ImageView setAdapterNewOrdersImgClientPhoto;
        @BindView(R.id.setAdapterNewOrdersNameClientTxv)
        TextView setAdapterNewOrdersNameClientTxv;
        @BindView(R.id.setAdapterNewOrdersTotalClientTxv)
        TextView setAdapterNewOrdersTotalClientTxv;
        @BindView(R.id.setAdapterNewOrdersAddressClientTxv)
        TextView setAdapterNewOrdersAddressClientTxv;
        @BindView(R.id.setAdapterNumberOrdersTxv)
        TextView setAdapterNumberOrdersTxv;

        @BindView(R.id.setAdapterNewOrdersBtnClientReject)
        Button setAdapterNewOrdersBtnClientReject;

        @BindView(R.id.setAdapterNewOrdersBtnClientAccept)
        Button setAdapterNewOrdersBtnClientAccept;

        @BindView(R.id.setAdapterNewOrdersBtnClientCall)
        Button setAdapterNewOrdersBtnClientCall;
        View view;
        @BindView(R.id.setAdapterNewOrdersItemsCardView)
        CardView setAdapterNewOrdersItemsCardView;


        ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            view = itemLayoutView;
            ButterKnife.bind(this, view);
        }
    }


    private void AslForPermission(Integer requestCode) {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.CALL_PHONE)) {

                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CALL_PHONE}, requestCode);

            } else {

                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CALL_PHONE}, requestCode);

            }
        } else {

            Toast.makeText(context, "" + Manifest.permission.CALL_PHONE + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }


    // ge New Order
    private void geNewOrder(int getId, final int position) {
//        LoadData(getActivity(), USER_API_TOKEN)


    }
}
