package com.example.sofranewapp.ui.fragment.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;


import com.example.sofranewapp.R;
import com.example.sofranewapp.adapter.ShowRestaurantClientRecyclerAdapter;
import com.example.sofranewapp.adapter.SpinnerAdapter;
import com.example.sofranewapp.data.api.APiSofraResturant;
import com.example.sofranewapp.data.api.RetrofitSofra;
import com.example.sofranewapp.data.model.Generated.GeneratedDataUser;
import com.example.sofranewapp.data.model.Generated.GeneratedModelSpinner;
import com.example.sofranewapp.data.model.allRestaurants.AllRestaurants;
import com.example.sofranewapp.data.model.cities.Cities;
import com.example.sofranewapp.helper.OnEndless;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AllRestaurantClientFragment extends Fragment {

    ShowRestaurantClientRecyclerAdapter showRestaurantClientRecyclerAdapter;
    public static List<GeneratedDataUser> userArrayList  ;

    Unbinder unbinder;
    @BindView(R.id.fragmentAllRestaurantClientBtnSearch)
    ImageButton fragmentAllRestaurantClientBtnSearch;
    @BindView(R.id.fragmentAllRestaurantClientEdtSearch)
    TextInputEditText fragmentAllRestaurantClientEdtSearch;
    @BindView(R.id.fragmentAllRestaurantClientSpnCity)
    Spinner fragmentAllRestaurantClientSpnCity;
    @BindView(R.id.fragmentAllRestaurantClientRvRestaurant)
    RecyclerView fragmentAllRestaurantClientRvRestaurant;

    @BindView(R.id.fragmentAllRestaurantClientSprRefresh)
    SwipeRefreshLayout fragmentAllRestaurantClientSprRefresh;
    private boolean checkFilterPost = true;

    private View view;
    private APiSofraResturant apiServerRestaurant;
    private int max = 0;

    public int current_page = 1;

    private OnEndless onEndless;

    // value adapter city
    SpinnerAdapter spinnerAdapter;
    ArrayList<GeneratedModelSpinner> generatedModelSpinnerArrayListCity;
    GeneratedModelSpinner cityGeneratedModelSpinner;
    private int idCity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_all_restaurant_client_sofra, container, false);

        unbinder = ButterKnife.bind(this, view);
        // initializer tools
        inti();

        getDataCity();
        // this is scroll list view
        onEndless();

        SwipeRefresh();
        return view;
    }

    // initializer tools
    @SuppressLint("RestrictedApi")
    private void inti() {

        apiServerRestaurant = RetrofitSofra.getRestaurant().create(APiSofraResturant.class);
//        fragmentAllRestaurantClientBtnSearch =view.findViewById(R.id.fragmentAllRestaurantClientBtnSearch);
        generatedModelSpinnerArrayListCity = new ArrayList<>();
        userArrayList = new ArrayList<>();

    }

    // get  All Restaurant
    private void getAllRestaurant(int Page) {

        fragmentAllRestaurantClientSprRefresh.setRefreshing(true);

        apiServerRestaurant.getAllRestaurant(Page).enqueue(new Callback<AllRestaurants>() {
            @Override
            public void onResponse(Call<AllRestaurants> call, Response<AllRestaurants> response) {
                try {
                    if (response.body().getStatus() == 1) {

                        max = response.body().getData().getLastPage();

                        userArrayList.addAll(response.body().getData().getData());

                        showRestaurantClientRecyclerAdapter.notifyDataSetChanged();

                        fragmentAllRestaurantClientSprRefresh.setRefreshing(false);

                    } else {
                        Log.d("response", response.body().getMsg());

                        fragmentAllRestaurantClientSprRefresh.setRefreshing(false);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            }
            @Override
            public void onFailure(Call<AllRestaurants> call, Throwable t) {
                Log.d("response", t.getMessage());

            }
        });

    }

    // get   Restaurant Filter
    private void getRestaurantFilter(int Page) {
        fragmentAllRestaurantClientSprRefresh.setRefreshing(true);
        apiServerRestaurant.getAllRestaurantFilter(fragmentAllRestaurantClientEdtSearch.getText().toString(),idCity,Page).enqueue(new Callback<AllRestaurants>() {
            @Override
            public void onResponse(Call<AllRestaurants> call, Response<AllRestaurants> response) {
                try {
                    if (response.body().getStatus() == 1) {

                        max = response.body().getData().getLastPage();

                        userArrayList.clear();

                        userArrayList.addAll(response.body().getData().getData());

                        showRestaurantClientRecyclerAdapter.notifyDataSetChanged();
                        fragmentAllRestaurantClientSprRefresh.setRefreshing(false);

                    } else {
                        Log.d("response", response.body().getMsg());
                        fragmentAllRestaurantClientSprRefresh.setRefreshing(false);
                    }
                } catch (Exception e) {
                    e.getMessage();
                    fragmentAllRestaurantClientSprRefresh.setRefreshing(false);

                }
            }

            @Override
            public void onFailure(Call<AllRestaurants> call, Throwable t) {
                Log.d("response", t.getMessage());
                fragmentAllRestaurantClientSprRefresh.setRefreshing(false);
            }
        });

    }

    // listener from count items  recyclerView
    private void onEndless() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        fragmentAllRestaurantClientRvRestaurant.setLayoutManager(linearLayoutManager);

        onEndless = new OnEndless(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= max || max != 0 || current_page == 1) {
                    if (checkFilterPost) {

                        getAllRestaurant(current_page);
                    } else {
                        getRestaurantFilter(current_page);
                    }
                }
            }
        };

        fragmentAllRestaurantClientRvRestaurant.addOnScrollListener(onEndless);
        showRestaurantClientRecyclerAdapter = new ShowRestaurantClientRecyclerAdapter(userArrayList, getActivity());
        fragmentAllRestaurantClientRvRestaurant.setAdapter(showRestaurantClientRecyclerAdapter);

        getAllRestaurant(1);

    }

    // get getDataCity
    private void getDataCity() {

        apiServerRestaurant.getCities().enqueue(new Callback<Cities>() {
            @Override
            public void onResponse(Call<Cities> call, Response<Cities> response) {
                try {
                    if (response.body().getStatus() == 1) {

                        // clear list
                        generatedModelSpinnerArrayListCity.clear();
                        // add new data from list
                        generatedModelSpinnerArrayListCity.add(new GeneratedModelSpinner(0, getResources().getString(R.string.city)));

                        for (int i = 0; i < response.body().getData().getData().size(); i++) {

                            cityGeneratedModelSpinner = new GeneratedModelSpinner(response.body().getData().getData().get(i).getId(),
                                    response.body().getData().getData().get(i).getName());

                            generatedModelSpinnerArrayListCity.add(cityGeneratedModelSpinner);
                        }

                        spinnerAdapter = new SpinnerAdapter(getContext(), generatedModelSpinnerArrayListCity);
                        fragmentAllRestaurantClientSpnCity.setAdapter(spinnerAdapter);
                        fragmentAllRestaurantClientSpnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position != 0) {
                                    idCity = generatedModelSpinnerArrayListCity.get(position).getId();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                    } else {
                        Log.d("response", response.body().getMsg());
                    }
                }catch (Exception e){
                    e.getMessage();
                }

            }

            @Override
            public void onFailure(Call<Cities> call, Throwable t) {
                Log.d("response", t.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fragmentAllRestaurantClientBtnSearch)
    public void onViewClicked() {
        if (fragmentAllRestaurantClientSpnCity.getSelectedItemPosition() ==
                0 && fragmentAllRestaurantClientEdtSearch.getText().toString().isEmpty()&& !checkFilterPost) {

            showRestaurantClientRecyclerAdapter = new ShowRestaurantClientRecyclerAdapter(userArrayList, getActivity());
            fragmentAllRestaurantClientRvRestaurant.setAdapter(showRestaurantClientRecyclerAdapter);

            checkFilterPost = true;

        } else {

             showRestaurantClientRecyclerAdapter = new ShowRestaurantClientRecyclerAdapter(userArrayList, getActivity());
            fragmentAllRestaurantClientRvRestaurant.setAdapter(showRestaurantClientRecyclerAdapter);


            checkFilterPost = false;

            getRestaurantFilter(1);

        }

    }

    //  swipeRefresh All list
    private void SwipeRefresh() {

        fragmentAllRestaurantClientSprRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllRestaurant(1);
                userArrayList.clear();
            }
        });
    }
}
