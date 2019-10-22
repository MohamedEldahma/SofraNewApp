package com.example.sofranewapp.ui.fragment.resturant;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import com.example.sofranewapp.R;
import com.example.sofranewapp.adapter.MyItemsRestaurantAdapter;
import com.example.sofranewapp.data.api.APiSofraResturant;
import com.example.sofranewapp.data.api.RetrofitSofra;
import com.example.sofranewapp.data.model.Generated.GeneratedItem;
import com.example.sofranewapp.data.model.deleteItem.DeleteItem;
import com.example.sofranewapp.data.model.myItems.MyItems;
import com.example.sofranewapp.helper.Swipe.SwipeMenu;
import com.example.sofranewapp.helper.Swipe.SwipeMenuCreator;
import com.example.sofranewapp.helper.Swipe.SwipeMenuItem;
import com.example.sofranewapp.helper.Swipe.SwipeMenuListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.LoadData;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.USER_API_TOKEN;
import static com.example.sofranewapp.helper.HelperMathod.getStartFragments;

public class MyItemRestaurantFragment extends Fragment {

    MyItemsRestaurantAdapter myItemsRestaurantAdapter;
    public static List<GeneratedItem> datumArrayList = new ArrayList<>();

    SwipeMenuListView listView;
    Unbinder unbinder;

    @BindView(R.id.floatingActionButton)
    FloatingActionButton floatingActionButton;
    private View view;
    private APiSofraResturant apiServerRestaurant;
    private int max = 0;
    private int previousTotal = 0;
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    public int current_page = 1;
    private int myLastVisiblePos_Makers;
    ProgressBar myItemRestaurantProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_my_item_restaurnt_sofra, container, false);

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

        myItemRestaurantProgressBar = view.findViewById(R.id.myItemRestaurantProgressBar);
        floatingActionButton.setVisibility(View.VISIBLE);


        // Swipe Menu list
        SwipeMenuList();

        // floating Action  Button
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert getFragmentManager() != null;
                getStartFragments(getFragmentManager(), R.id.mainRestaurantReplaceFragment, new AddtemRestaurntFragment());
            }
        });

    }

    // get GeneratedDataOrder cities
    private void getItemRestaurant(int Page) {
        myItemRestaurantProgressBar.setVisibility(View.VISIBLE);
        apiServerRestaurant.getItemRestaurant(LoadData(getActivity(), USER_API_TOKEN), Page)
                .enqueue(new Callback<MyItems>() {
                    @Override
                    public void onResponse(Call<MyItems> call, Response<MyItems> response) {
                        try {
                            if (response.body().getStatus() == 1) {

                                max = response.body().getDataItem().getLastPage();

                                datumArrayList.addAll(response.body().getDataItem().getData());

                                myItemsRestaurantAdapter.notifyDataSetChanged();
                                myItemRestaurantProgressBar.setVisibility(View.INVISIBLE);

                            } else {
                                Log.d("response", response.body().getMsg());
                                myItemRestaurantProgressBar.setVisibility(View.INVISIBLE);
                            }
                        } catch (Exception e) {
                            e.getMessage();
                        }

                    }

                    @Override
                    public void onFailure(Call<MyItems> call, Throwable t) {
                        Log.d("response", t.getMessage());
                        myItemRestaurantProgressBar.setVisibility(View.INVISIBLE);
                    }
                });

    }

    // Delete GeneratedDataOrder cities
    private void DeleteItemRestaurant(int item_id) {
        myItemRestaurantProgressBar.setVisibility(View.VISIBLE);
        apiServerRestaurant.DeleteItemRestaurant(
                LoadData(getActivity(), USER_API_TOKEN), item_id)
                .enqueue(new Callback<DeleteItem>() {
                    @Override
                    public void onResponse(Call<DeleteItem> call, Response<DeleteItem> response) {

                        if (response.body().getStatus() == 1) {


                            myItemRestaurantProgressBar.setVisibility(View.INVISIBLE);

                        } else {
                            Log.d("response", response.body().getMsg());
                            myItemRestaurantProgressBar.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<DeleteItem> call, Throwable t) {
                        Log.d("response", t.getMessage());
                        myItemRestaurantProgressBar.setVisibility(View.INVISIBLE);
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
                        bundle.putString("getPrice",""+ datumArrayList.get(position).getPrice());
//                        bundle.putString("getOfferPrice", datumArrayList.get(position).getOfferPrice());
                        bundle.putString("getPreparingTime", datumArrayList.get(position).getPreparingTime());
                        // open
                        Fragment fragment = new EditItemRestaurntFragment();
                        fragment.setArguments(bundle);

                        getStartFragments(getFragmentManager(), R.id.mainRestaurantReplaceFragment, fragment);


                        break;
                    case 1:
                        // delete
                        DeleteItemRestaurant(datumArrayList.get(position).getId());
                        datumArrayList.remove(position);
                        myItemsRestaurantAdapter.notifyDataSetChanged();
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

                // listener on scroll list setVisibility floatingActionButton
//                int currentFirstVisPos = view.getFirstVisiblePosition();

                if (firstVisibleItem > myLastVisiblePos_Makers) {
                    floatingActionButton.setVisibility(View.GONE);
                } else if (firstVisibleItem < myLastVisiblePos_Makers) {
                    floatingActionButton.setVisibility(View.VISIBLE);
                }
                myLastVisiblePos_Makers = firstVisibleItem;


                // on Load More data
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }

                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + 1)) {


                    if (current_page <= max || max != 0 || current_page == 1) {
                        // get Item Restaurant
                        getItemRestaurant(current_page);
                        current_page++;
                    }
                    loading = true;   // Do something

                }

            }
        });

        myItemsRestaurantAdapter = new MyItemsRestaurantAdapter(datumArrayList, getActivity());
        listView.setAdapter(myItemsRestaurantAdapter);

        getItemRestaurant(1);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
