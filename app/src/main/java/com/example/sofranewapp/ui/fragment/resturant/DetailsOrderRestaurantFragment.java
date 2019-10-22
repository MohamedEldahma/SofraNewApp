package com.example.sofranewapp.ui.fragment.resturant;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.sofranewapp.R;
import com.example.sofranewapp.adapter.AdapterListItemsOrder;
import com.example.sofranewapp.data.api.APiSofraResturant;
import com.example.sofranewapp.data.api.RetrofitSofra;
import com.example.sofranewapp.data.model.acceptOrder.AcceptOrder;
import com.example.sofranewapp.data.model.rejectOrder.RejectOrder;
import com.example.sofranewapp.data.model.showOrder.ShowOrder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.LoadData;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.USER_API_TOKEN;
import static com.example.sofranewapp.helper.HelperMathod.LodeImageCircle;
import static com.example.sofranewapp.helper.HelperMathod.dismissProgressDialog;
import static com.example.sofranewapp.helper.HelperMathod.showProgressDialog;

public class DetailsOrderRestaurantFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.showOrderRestaurantFragmentImgPhotoRestaurant)
    ImageView showOrderRestaurantFragmentImgPhotoRestaurant;
    @BindView(R.id.showOrderRestaurantFragmentTvNameRestaurant)
    TextView showOrderRestaurantFragmentTvNameRestaurant;
    @BindView(R.id.showOrderRestaurantFragmentTvDateOrderRestaurant)
    TextView showOrderRestaurantFragmentTvDateOrderRestaurant;
    @BindView(R.id.showOrderRestaurantFragmentTvAddressRestaurant)
    TextView showOrderRestaurantFragmentTvAddressRestaurant;
    @BindView(R.id.showOrderRestaurantFragmentTvOrderDetailsRestaurant)
    ListView showOrderRestaurantFragmentTvOrderDetailsRestaurant;
    @BindView(R.id.showOrderRestaurantFragmentTvPriceRestaurant)
    TextView showOrderRestaurantFragmentTvPriceRestaurant;
    @BindView(R.id.showOrderRestaurantFragmentTvDeliveryCostRestaurant)
    TextView showOrderRestaurantFragmentTvDeliveryCostRestaurant;
    @BindView(R.id.showOrderRestaurantFragmentTvTotalRestaurant)
    TextView showOrderRestaurantFragmentTvTotalRestaurant;
    @BindView(R.id.showOrderRestaurantFragmentTvPayingRestaurant)
    TextView showOrderRestaurantFragmentTvPayingRestaurant;
    @BindView(R.id.showOrderRestaurantFragmentItemProgressBar)
    ProgressBar showOrderRestaurantFragmentItemProgressBar;
    @BindView(R.id.showOrderRestaurantFragmentShowOrdersBtnClientReject)
    Button showOrderRestaurantFragmentShowOrdersBtnClientReject;
    @BindView(R.id.showOrderRestaurantFragmentShowOrdersBtnClientAccept)
    Button showOrderRestaurantFragmentShowOrdersBtnClientAccept;
    @BindView(R.id.showOrderRestaurantFragmentShowOrdersBtnClientCall)
    Button showOrderRestaurantFragmentShowOrdersBtnClientCall;
    @BindView(R.id.relativeLayout)
    ConstraintLayout relativeLayout;

    private APiSofraResturant apiServerRestaurant;

    private View view;

    private int getId;
    private String keyRequest;
    OrdersRequestsRestaurantFragment ordersRequestsRestaurantFragment;
    private int posation;
    private static final Integer CALL = 0x2;
    private String getPhone;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.show_order_restaurant_fragment_sofra, container, false);

        unbinder = ButterKnife.bind(this, view);
        // initializer tools
        inti();


        return view;
    }

    // initializer tools
    private void inti() {
        /// get date MyItemRestaurantFragment

        apiServerRestaurant = RetrofitSofra.getRestaurant().create(APiSofraResturant.class);

        showOrderRestaurantFragmentItemProgressBar.setVisibility(View.INVISIBLE);

        Bundle getBundle = getArguments();
        if (getBundle != null) {
            getId = getBundle.getInt("getId");
            keyRequest = getBundle.getString("keyRequest");
            posation = getBundle.getInt("posation");
            getPhone = getBundle.getString("getPhone");
        }

        showOrderRestaurant();

        if (keyRequest.equals("pending")) {

            showOrderRestaurantFragmentShowOrdersBtnClientCall.setText(getResources().getString(R.string.phone));
            showOrderRestaurantFragmentShowOrdersBtnClientCall.setVisibility(View.VISIBLE);
            showOrderRestaurantFragmentShowOrdersBtnClientReject.setVisibility(View.VISIBLE);
            showOrderRestaurantFragmentShowOrdersBtnClientAccept.setVisibility(View.VISIBLE);

        } else if (keyRequest.equals("current")) {

            showOrderRestaurantFragmentShowOrdersBtnClientCall.setText(getResources().getString(R.string.phone));
            showOrderRestaurantFragmentShowOrdersBtnClientAccept.setText(getResources().getString(R.string.confirm_delivery));
            showOrderRestaurantFragmentShowOrdersBtnClientCall.setVisibility(View.VISIBLE);
            showOrderRestaurantFragmentShowOrdersBtnClientReject.setVisibility(View.GONE);
            showOrderRestaurantFragmentShowOrdersBtnClientCall.setVisibility(View.VISIBLE);

        } else if (keyRequest.equals("completed")) {

            showOrderRestaurantFragmentShowOrdersBtnClientCall.setVisibility(View.GONE);
            showOrderRestaurantFragmentShowOrdersBtnClientReject.setVisibility(View.GONE);
            showOrderRestaurantFragmentShowOrdersBtnClientAccept.setVisibility(View.GONE);

        }

        if (keyRequest.equals("pending")) {

            showOrderRestaurantFragmentShowOrdersBtnClientAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {

                        showProgressDialog(getActivity(), "please wait");
                        apiServerRestaurant.acceptOrder(LoadData(getActivity(), USER_API_TOKEN), getId).enqueue(new Callback<AcceptOrder>() {
                            @Override
                            public void onResponse(Call<AcceptOrder> call, Response<AcceptOrder> response) {
                                try {
                                    dismissProgressDialog();
                                    if (response.body().getStatus() == 1) {

                                        Log.d("response", response.body().getMsg());

                                        ordersRequestsRestaurantFragment.ordersArrayList.remove(posation);

                                        ordersRequestsRestaurantFragment.restaurantRecyclerAdapter.notifyDataSetChanged();

                                    } else {
                                        Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
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


            showOrderRestaurantFragmentShowOrdersBtnClientReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        showProgressDialog(getActivity(), "please wait");

                        apiServerRestaurant.rejectOrder(LoadData(getActivity(), USER_API_TOKEN), getId).enqueue(new Callback<RejectOrder>() {
                            @Override
                            public void onResponse(Call<RejectOrder> call, Response<RejectOrder> response) {
                                try {
                                    dismissProgressDialog();
                                    if (response.body().getStatus() == 1) {

                                        Log.d("response", response.body().getMsg());

                                        ordersRequestsRestaurantFragment.ordersArrayList.remove(posation);

                                        ordersRequestsRestaurantFragment.restaurantRecyclerAdapter.notifyDataSetChanged();

                                    } else {
                                        Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
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

            showOrderRestaurantFragmentShowOrdersBtnClientAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        showProgressDialog(getActivity(), "please wait");
                        apiServerRestaurant.confirmOrder(LoadData(getActivity(), USER_API_TOKEN), getId).enqueue(new Callback<RejectOrder>() {
                            @Override
                            public void onResponse(Call<RejectOrder> call, Response<RejectOrder> response) {
                                dismissProgressDialog();
                                try {
                                    if (response.body().getStatus() == 1) {
                                        Log.d("response", response.body().getMsg());

                                        ordersRequestsRestaurantFragment.ordersArrayList.remove(posation);

                                        ordersRequestsRestaurantFragment.restaurantRecyclerAdapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
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

      showOrderRestaurantFragmentShowOrdersBtnClientCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
                            == PackageManager.PERMISSION_GRANTED) {

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + getPhone));
                         startActivity(callIntent);

                    } else {
                        AslForPermission(CALL);
                    }

                } catch (Exception e) {
                    Log.e("Demo application", "Failed to invoke call", e);
                }
            }
        });
    }


    private void showOrderRestaurant() {

        showOrderRestaurantFragmentItemProgressBar.setVisibility(View.VISIBLE);
//        LoadData(getActivity(),USER_API_TOKEN);

        apiServerRestaurant.myShowOrder(LoadData(getActivity(), USER_API_TOKEN), getId).enqueue(new Callback<ShowOrder>() {
            @Override
            public void onResponse(Call<ShowOrder> call, Response<ShowOrder> response) {
                try {
                    Log.d("response", response.body().getMsg());

                    if (response.body().getStatus() == 1) {

                        Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_LONG).show();

                        LodeImageCircle(getContext(), response.body().getData().getRestaurant().getPhotoUrl(), showOrderRestaurantFragmentImgPhotoRestaurant);

                        showOrderRestaurantFragmentTvNameRestaurant.setText(response.body().getData().getRestaurant().getName());
                        showOrderRestaurantFragmentTvDateOrderRestaurant.setText(response.body().getData().getUpdatedAt());
                        showOrderRestaurantFragmentTvAddressRestaurant.setText(response.body().getData().getAddress());
                        showOrderRestaurantFragmentTvPriceRestaurant.setText(response.body().getData().getCost());
                        showOrderRestaurantFragmentTvDeliveryCostRestaurant.setText(response.body().getData().getDeliveryCost());
                        showOrderRestaurantFragmentTvTotalRestaurant.setText(response.body().getData().getTotal());
                        if (response.body().getData().getPaymentMethodId().equals("1")) {
                            showOrderRestaurantFragmentTvPayingRestaurant.setText("Cash");

                        }

                        AdapterListItemsOrder adapterListItemsOrder = new AdapterListItemsOrder(getContext(), response.body().getData().getItems());
                        showOrderRestaurantFragmentTvOrderDetailsRestaurant.setAdapter(adapterListItemsOrder);

                        showOrderRestaurantFragmentItemProgressBar.setVisibility(View.INVISIBLE);

                    } else {
                        Log.d("response", response.body().getMsg());
                        showOrderRestaurantFragmentItemProgressBar.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            }

            @Override
            public void onFailure(Call<ShowOrder> call, Throwable t) {
                Log.d("response", t.getMessage());
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void AslForPermission(Integer requestCode) {

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, requestCode);

            } else {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, requestCode);

            }
        } else {

            Toast.makeText(getContext(), "" + Manifest.permission.CALL_PHONE + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }


}
