package com.example.sofranewapp.ui.fragment.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


import com.example.sofranewapp.R;
import com.example.sofranewapp.adapter.MenuRestaurantClientRecyclerAdapter;
import com.example.sofranewapp.data.api.APiSofraResturant;
import com.example.sofranewapp.data.api.RetrofitSofra;
import com.example.sofranewapp.data.model.Generated.GeneratedItem;
import com.example.sofranewapp.data.model.myItems.MyItems;
import com.example.sofranewapp.helper.OnEndless;

import java.util.ArrayList;
import java.util.List;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MenuRestaurantClientFragment extends Fragment {

    MenuRestaurantClientRecyclerAdapter menuRestaurantClientRecyclerAdapter;
    public static List<GeneratedItem> userArrayList;

    Unbinder unbinder;
    @BindView(R.id.fragmentMenuRestaurantClientRvMenuRestaurant)
    RecyclerView fragmentMenuRestaurantClientRvRestaurant;
    @BindView(R.id.fragmentMenuRestaurantClientPbWait)
    ProgressBar fragmentMenuRestaurantClientPbWait;


    private View view;
    private APiSofraResturant apiServerRestaurant;
    private int max = 0;

    public int current_page = 1;

    private OnEndless onEndless;
    private int idRestaurant;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_menu_restaurant_client_sofra, container, false);

        unbinder = ButterKnife.bind(this, view);
        // initializer tools
        inti();

        // this is scroll list view
        onEndless();

        return view;
    }

    // initializer tools
    @SuppressLint("RestrictedApi")
    private void inti() {
        apiServerRestaurant = RetrofitSofra.getRestaurant().create(APiSofraResturant.class);
        userArrayList = new ArrayList<>();

        // get id restaurant from return Content Component

        Bundle arguments =getArguments();
        if (arguments != null) {
            idRestaurant = arguments.getInt("idRestaurantContentComponent");
            Log.d("idRestaurantMenu", String.valueOf(idRestaurant));

        }
    }

    //   get Item Restaurant
    private void getItemRestaurant(int Page) {
        fragmentMenuRestaurantClientPbWait.setVisibility(View.VISIBLE);
        apiServerRestaurant.getItemRestaurantClient(idRestaurant, Page)
                .enqueue(new Callback<MyItems>() {
                    @Override
                    public void onResponse(Call<MyItems> call, Response<MyItems> response) {
                        try {
                            if (response.body().getStatus() == 1) {

                                max = response.body().getDataItem().getLastPage();

                                userArrayList.addAll(response.body().getDataItem().getData());

                                menuRestaurantClientRecyclerAdapter.notifyDataSetChanged();

                                fragmentMenuRestaurantClientPbWait.setVisibility(View.INVISIBLE);

                            } else {
                                Log.d("response", response.body().getMsg());
                                fragmentMenuRestaurantClientPbWait.setVisibility(View.INVISIBLE);
                            }
                        } catch (Exception e) {
                            e.getMessage();
                        }

                    }

                    @Override
                    public void onFailure(Call<MyItems> call, Throwable t) {
                        Log.d("response", t.getMessage());
                    }
                });

    }

    // listener from count items  recyclerView
    private void onEndless() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        fragmentMenuRestaurantClientRvRestaurant.setLayoutManager(linearLayoutManager);

        onEndless = new OnEndless(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= max || max != 0 || current_page == 1) {
                    getItemRestaurant(current_page);
                }
            }
        };

        fragmentMenuRestaurantClientRvRestaurant.addOnScrollListener(onEndless);
        menuRestaurantClientRecyclerAdapter = new MenuRestaurantClientRecyclerAdapter(userArrayList, getActivity());
        fragmentMenuRestaurantClientRvRestaurant.setAdapter(menuRestaurantClientRecyclerAdapter);

        getItemRestaurant(1);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
