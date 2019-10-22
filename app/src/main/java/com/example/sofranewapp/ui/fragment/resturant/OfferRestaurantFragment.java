package com.example.sofranewapp.ui.fragment.resturant;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.sofranewapp.R;
import com.example.sofranewapp.adapter.OfferRestaurantAdapter;
import com.example.sofranewapp.data.api.APiSofraResturant;
import com.example.sofranewapp.data.api.RetrofitSofra;
import com.example.sofranewapp.data.model.deleteItem.DeleteItem;
import com.example.sofranewapp.data.model.myOffers.Datum;
import com.example.sofranewapp.data.model.myOffers.MyOffers;
import com.example.sofranewapp.helper.Swipe.SwipeMenu;
import com.example.sofranewapp.helper.Swipe.SwipeMenuCreator;
import com.example.sofranewapp.helper.Swipe.SwipeMenuItem;
import com.example.sofranewapp.helper.Swipe.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.LoadData;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.USER_API_TOKEN;
import static com.example.sofranewapp.helper.HelperMathod.getStartFragments;

public class OfferRestaurantFragment extends Fragment {

    OfferRestaurantAdapter offerRestaurantAdapter;
    public static List<Datum> datumArrayList = new ArrayList<>();

    SwipeMenuListView listView;
    Unbinder unbinder;

    @BindView(R.id.floatingActionButton)
    Button floatingActionButton;


    private View view;
    private APiSofraResturant apiServerRestaurant;

    private int previousTotal = 0;
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    public int current_page = 1;
     ProgressBar orderRestaurantProgressBar;
    private Integer max=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_offer_restaurnt_sofra, container, false);

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
        listView = view.findViewById(R.id.listView);

        datumArrayList.clear();

        orderRestaurantProgressBar = view.findViewById(R.id.newOrderRestaurantProgressBar);

        // Swipe Menu list
        SwipeMenuList();


    }

    // get getOffersRestaurant
    private void getOffersRestaurant(int Page) {
        orderRestaurantProgressBar.setVisibility(View.VISIBLE);
        apiServerRestaurant.getMyOffers( LoadData(getActivity(),USER_API_TOKEN), Page)
                .enqueue(new Callback<MyOffers>() {
                    @Override
                    public void onResponse(Call<MyOffers> call, Response<MyOffers> response) {

                        Log.d("response", response.body().getMsg());

                        if (response.body().getStatus() == 1) {

                            max = response.body().getData().getLastPage();

                            datumArrayList.addAll(response.body().getData().getData());

                            offerRestaurantAdapter.notifyDataSetChanged();

                            orderRestaurantProgressBar.setVisibility(View.INVISIBLE);

                        } else {
                            Log.d("response", response.body().getMsg());
                            orderRestaurantProgressBar.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<MyOffers> call, Throwable t) {
                        Log.d("response", t.getMessage());
                        orderRestaurantProgressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    // Delete Offers Restaurant
    private void DeleteOffersRestaurant(int offer_id) {
        orderRestaurantProgressBar.setVisibility(View.VISIBLE);
        apiServerRestaurant.deleteOffer( LoadData(getActivity(),USER_API_TOKEN), offer_id)
                .enqueue(new Callback<DeleteItem>() {
                    @Override
                    public void onResponse(Call<DeleteItem> call, Response<DeleteItem> response) {

                        if (response.body().getStatus() == 1) {


                            orderRestaurantProgressBar.setVisibility(View.INVISIBLE);

                        } else {
                            Log.d("response", response.body().getMsg());
                            orderRestaurantProgressBar.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<DeleteItem> call, Throwable t) {
                        Log.d("response", t.getMessage());
                        orderRestaurantProgressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    // Swipe Menu list
    private void SwipeMenuList() {

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getContext());
                // set item background
//                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
//                        0xCE)));
                // set item width
                openItem.setWidth(90);
                // set item title
//                openItem.setTitle("Open");
                openItem.setIcon(R.drawable.icon_edit);
//                openItem.setTitleSize(18);

                // set item title font color
//                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getContext());
                // set item background
//                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
//                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(90);
                // set a icon
                deleteItem.setIcon(R.drawable.icon_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

// set creator
        listView.setMenuCreator(creator);

        listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Bundle bundle = new Bundle();
                        bundle.putInt("getId", datumArrayList.get(position).getId());
                        bundle.putString("getName", datumArrayList.get(position).getName());
                        bundle.putString("getDescription", datumArrayList.get(position).getDescription());
                        bundle.putString("getPhotoUrl", datumArrayList.get(position).getPhotoUrl());
                        bundle.putString("getStartingAt", datumArrayList.get(position).getStartingAt());
                        bundle.putString("getEndingAt", datumArrayList.get(position).getEndingAt());
                        bundle.putString("getPrice", datumArrayList.get(position).getPrice());


                        // open
                        Fragment fragment = new UpdateOffersRestaurantFragment();
                        fragment.setArguments(bundle);

                        if (getFragmentManager() != null) {
                            getStartFragments(getFragmentManager(), R.id.mainRestaurantReplaceFragment, fragment);
                        }


                        break;
                    case 1:
                        // delete
                        DeleteOffersRestaurant(datumArrayList.get(position).getId());
                        datumArrayList.remove(position);
                        offerRestaurantAdapter.notifyDataSetChanged();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

    }

    // listener from count items  list
    private void onEndless() {

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                // on Load More data
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }

                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + 1)) {
                    // Do something
                    current_page++;
                    // get Item Restaurant

                    if (current_page <= max || max != 0 || current_page == 1) {
                        getOffersRestaurant(current_page);
                    }
                    loading = true;
                }

            }
        });

        offerRestaurantAdapter = new OfferRestaurantAdapter(datumArrayList, getActivity());
        listView.setAdapter(offerRestaurantAdapter);

        getOffersRestaurant(1);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.floatingActionButton)
    public void onViewClicked() {
        if (getFragmentManager() != null) {
            getStartFragments(getFragmentManager(), R.id.mainRestaurantReplaceFragment, new AddOffersRestaurantFragment());
        }
    }
}
