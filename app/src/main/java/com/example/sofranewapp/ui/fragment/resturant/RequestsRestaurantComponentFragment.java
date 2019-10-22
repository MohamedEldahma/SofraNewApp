package com.example.sofranewapp.ui.fragment.resturant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sofranewapp.R;
import com.example.sofranewapp.adapter.ViewPagerComponentAdapter;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class RequestsRestaurantComponentFragment extends Fragment {


    private View view;
    public TabLayout requestRestaurantFragmentTabLayout;
    public ViewPager requestRestaurantFragmentViewPager;

    public OrdersRequestsRestaurantFragment requestsRestaurantFragmentPending;
    public OrdersRequestsRestaurantFragment requestsRestaurantFragmentAccept;
    public OrdersRequestsRestaurantFragment requestsRestaurantFragmentCompleted;
    private ViewPagerComponentAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_component_restaurnt_sofra, container, false);

        // initializer tools
        inti();


        return view;
    }

    // initializer tools
    private void inti() {
        // Setting ViewPager for each Tabs
        requestRestaurantFragmentViewPager = view.findViewById(R.id.requestRestaurantFragmentViewPager);
        requestRestaurantFragmentTabLayout = view.findViewById(R.id.requestRestaurantFragmentTabLayout);

        adapter = new ViewPagerComponentAdapter(getChildFragmentManager());


        // start  class requestsRestaurantFragmentPending and send pending
        requestsRestaurantFragmentPending = new OrdersRequestsRestaurantFragment();
        Bundle bundlePending = new Bundle();
        bundlePending.putString("KeyRequest", "pending");
        requestsRestaurantFragmentPending.setArguments(bundlePending);

        // start  class requestsRestaurantFragmentAccept and send delivered
        requestsRestaurantFragmentAccept = new OrdersRequestsRestaurantFragment();
        Bundle bundleDelivered = new Bundle();
        bundleDelivered.putString("KeyRequest", "current");
        requestsRestaurantFragmentAccept.setArguments(bundleDelivered);


        // start  class requestsRestaurantFragmentCompleted and send rejected
        requestsRestaurantFragmentCompleted = new OrdersRequestsRestaurantFragment();
        Bundle bundleRejected = new Bundle();
        bundleRejected.putString("KeyRequest", "completed");
        requestsRestaurantFragmentCompleted.setArguments(bundleRejected);


        requestsRestaurantFragmentPending.requestsRestaurantComponentFragment = this;
        requestsRestaurantFragmentAccept.requestsRestaurantComponentFragment = this;
        requestsRestaurantFragmentCompleted.requestsRestaurantComponentFragment = this;

        adapter.addFragment(requestsRestaurantFragmentPending, getResources().getString(R.string.new_request));
        adapter.addFragment(requestsRestaurantFragmentAccept, getResources().getString(R.string.current_requests));
        adapter.addFragment(requestsRestaurantFragmentCompleted, getResources().getString(R.string.previous_requests));

        requestRestaurantFragmentViewPager.setAdapter(adapter);
        requestRestaurantFragmentTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        requestRestaurantFragmentTabLayout.setupWithViewPager(requestRestaurantFragmentViewPager);
    }


}
