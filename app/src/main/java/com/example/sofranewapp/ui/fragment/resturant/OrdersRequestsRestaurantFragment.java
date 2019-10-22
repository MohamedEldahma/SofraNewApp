package com.example.sofranewapp.ui.fragment.resturant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sofranewapp.R;
import com.example.sofranewapp.adapter.OrdersRestaurantRecyclerAdapter;
import com.example.sofranewapp.data.api.APiSofraResturant;
import com.example.sofranewapp.data.api.RetrofitSofra;
import com.example.sofranewapp.data.model.myOrders.Datum;
import com.example.sofranewapp.data.model.myOrders.MyOrders;
import com.example.sofranewapp.helper.OnEndless;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.LoadData;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.USER_API_TOKEN;

public class OrdersRequestsRestaurantFragment extends Fragment {


    public RequestsRestaurantComponentFragment requestsRestaurantComponentFragment;
    Unbinder unbinder;
    @BindView(R.id.RequestsRestaurantFragmentRequestRecyclerView)
    RecyclerView RequestsRestaurantFragmentRequestRecyclerView;

    @BindView(R.id.RequestsRestaurantFragmentRequestSwipeRefresh)
    SwipeRefreshLayout RequestsRestaurantFragmentRequestSwipeRefresh;

    public OrdersRestaurantRecyclerAdapter restaurantRecyclerAdapter;
    private View view;
    private OnEndless onEndless;

    public ArrayList<Datum> ordersArrayList = new ArrayList<>();
    private int max;
    private APiSofraResturant apiServerRestaurant;
    private String KeyRequest;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_requests_restaurnt_sofra, container, false);

        unbinder = ButterKnife.bind(this, view);
        // initializer tools
        inti();

        // listener from count items  recyclerView
        onEndless();

        SwipeRefresh();
        return view;
    }

    // initializer tools
    private void inti() {
        /// get date MyItemRestaurantFragment
        apiServerRestaurant = RetrofitSofra.getRestaurant().create(APiSofraResturant.class);
        Bundle bundle = getArguments();
        if (bundle != null) {
            KeyRequest = bundle.getString("KeyRequest");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        ordersArrayList.clear();
    }

    // listener from count items  recyclerView
    private void onEndless() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        RequestsRestaurantFragmentRequestRecyclerView.setLayoutManager(linearLayoutManager);

        onEndless = new OnEndless(linearLayoutManager, 10) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= max || max != 0 || current_page == 1) {
                    geNewOrder(current_page);
                }
            }
        };

        RequestsRestaurantFragmentRequestRecyclerView.addOnScrollListener(onEndless);

        restaurantRecyclerAdapter =
                new OrdersRestaurantRecyclerAdapter(ordersArrayList, getActivity(), requestsRestaurantComponentFragment,KeyRequest);
        RequestsRestaurantFragmentRequestRecyclerView.setAdapter(restaurantRecyclerAdapter);


        geNewOrder(1);

    }


    // ge New Order
    private void geNewOrder(int current_page) {
//        LoadData(getActivity(), USER_API_TOKEN)
        RequestsRestaurantFragmentRequestSwipeRefresh.setRefreshing(true);
//delivered pending
        apiServerRestaurant.getMyOrders(LoadData(getActivity(), USER_API_TOKEN), KeyRequest, current_page)
                .enqueue(new Callback<MyOrders>() {
                    @Override
                    public void onResponse(Call<MyOrders> call, Response<MyOrders> response) {
                        try {
                            Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            if (response.body().getStatus() == 1) {

                                max = response.body().getData().getLastPage();

                                // add All
                                ordersArrayList.addAll(response.body().getData().getData());

                                restaurantRecyclerAdapter.notifyDataSetChanged();

                                //  set Visibility INVISIBLE
                                RequestsRestaurantFragmentRequestSwipeRefresh.setRefreshing(false);

                            } else {

                                Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                RequestsRestaurantFragmentRequestSwipeRefresh.setRefreshing(false);

                            }
                        } catch (Exception e) {

                            e.getMessage();
                        }


                    }

                    @Override
                    public void onFailure(Call<MyOrders> call, Throwable t) {


                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //  swipeRefresh All list
    private void SwipeRefresh() {

        RequestsRestaurantFragmentRequestSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ordersArrayList.clear();
                geNewOrder(1);


            }
        });
    }
}
