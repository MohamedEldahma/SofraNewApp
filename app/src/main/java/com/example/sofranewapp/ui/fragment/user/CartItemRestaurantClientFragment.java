package com.example.sofranewapp.ui.fragment.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.example.sofranewapp.R;
import com.example.sofranewapp.adapter.CartMyItemsRestaurantClientAdapter;
import com.example.sofranewapp.data.local.AppRoomDatabase;
import com.example.sofranewapp.ui.activity.user.LoginClientActivity;
import com.example.sofranewapp.ui.activity.user.MainClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.sofranewapp.data.local.SharedPreferncesMangerClient.LoadBooleanClient;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.KEY_IS_CHECK_BOX;
import static com.example.sofranewapp.helper.HelperMathod.getStartFragments;


public class CartItemRestaurantClientFragment extends Fragment {

    public CartMyItemsRestaurantClientAdapter cartMyItemsRestaurantClientAdapter;

    public MainClient mainClient;
    Unbinder unbinder;
    @BindView(R.id.cartItemRestaurantClientFragmentBtnMoreCart)
    Button cartItemRestaurantClientFragmentBtnMoreCart;
    @BindView(R.id.cartItemRestaurantClientFragmentBtnConfirmCart)
    Button cartItemRestaurantClientFragmentBtnConfirmCart;
    @BindView(R.id.cartItemRestaurantClientFragmentRvShowItemCart)
    RecyclerView cartItemRestaurantClientFragmentRvShowItemCart;

     public TextView cartItemRestaurantClientFragmentTvTotalCart;


    private View view;
    private AppRoomDatabase database;

    private Handler handler;
    private static final long SLIDER_TIMER = 1000;

    Runnable runnable = (new Runnable() {

        @Override
        public void run() {
            new CountDownTimer(SLIDER_TIMER, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {

                    try {
                        cartItemRestaurantClientFragmentTvTotalCart.setText("" + cartMyItemsRestaurantClientAdapter.getTotal());
                    }catch (Exception e){
                        e.getMessage();
                    }
                }

                @Override
                public void onFinish() {



                }
            }.start();

            handler.postDelayed(runnable, SLIDER_TIMER);
        }
    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_cart_item_restaurnt_client_sofra, container, false);

        unbinder = ButterKnife.bind(this, view);

        // initializer tools
        inti();

        handler = new Handler();
        handler.postDelayed(runnable, SLIDER_TIMER);
        runnable.run();

        return view;
    }

    // initializer tools
    @SuppressLint("RestrictedApi")
    private void inti() {

        database = AppRoomDatabase.getAppDatabase(getContext());

        cartItemRestaurantClientFragmentTvTotalCart =  view.findViewById(R.id.cartItemRestaurantClientFragmentTvTotalCart);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        cartItemRestaurantClientFragmentRvShowItemCart.setLayoutManager(linearLayoutManager);

        cartMyItemsRestaurantClientAdapter = new CartMyItemsRestaurantClientAdapter(database.getItemDAO().getItems(), getActivity(), mainClient);
        cartItemRestaurantClientFragmentRvShowItemCart.setAdapter(cartMyItemsRestaurantClientAdapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.cartItemRestaurantClientFragmentBtnMoreCart
            , R.id.cartItemRestaurantClientFragmentBtnConfirmCart})
    public void onViewClicked(View view) {

        if (!cartMyItemsRestaurantClientAdapter.ordersArrayList.isEmpty()) {

            switch (view.getId()) {
                case R.id.cartItemRestaurantClientFragmentBtnMoreCart:

                    // open menu restaurant is on click btn more
                    // and send id restaurant
                    Bundle bundle = new Bundle();
                    bundle.putInt("IdRestaurantFromCartOrShowAdapter", cartMyItemsRestaurantClientAdapter.IdRestauran);

                    Fragment fragment = new ContentRestaurantComponentClientFragment();
                    fragment.setArguments(bundle);


                    assert getFragmentManager() != null;
                    getStartFragments(getFragmentManager(), R.id.mainClientReplaceFragment, fragment);

                    break;
                case R.id.cartItemRestaurantClientFragmentBtnConfirmCart:

                    // check is checkBox is Checked
                    if (!LoadBooleanClient(getActivity(), KEY_IS_CHECK_BOX)) {

                        startActivity(new Intent(getContext(), LoginClientActivity.class));

                    } else {

                        Bundle bundle1 = new Bundle();
                        bundle1.putInt("getTotal", (int) cartMyItemsRestaurantClientAdapter.getTotal());
                        bundle1.putInt("getIdRestaurant", cartMyItemsRestaurantClientAdapter.IdRestauran);

                        Fragment fragment1 = new ConfirmOrderDetailsRestaurantClientFragment();
                        fragment1.setArguments(bundle1);


                        assert getFragmentManager() != null;
                        getStartFragments(getFragmentManager(), R.id.mainClientReplaceFragment, fragment1);
                    }
                    break;
            }
        }
    }
}
