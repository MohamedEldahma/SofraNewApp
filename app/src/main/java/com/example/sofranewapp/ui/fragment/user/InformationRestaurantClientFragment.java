package com.example.sofranewapp.ui.fragment.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sofranewapp.R;
import com.example.sofranewapp.data.api.APiSofraResturant;
import com.example.sofranewapp.data.api.RetrofitSofra;
import com.example.sofranewapp.data.model.informationRestaurant.InformationRestaurant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InformationRestaurantClientFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.fragmentInformationRestaurantClientPbWait)
    ProgressBar fragmentInformationRestaurantClientPbWait;
    @BindView(R.id.fragmentInformationRestaurantClientTvState)
    TextView fragmentInformationRestaurantClientTvState;
    @BindView(R.id.fragmentInformationRestaurantClientTvCity)
    TextView fragmentInformationRestaurantClientTvCity;
    @BindView(R.id.fragmentInformationRestaurantClientTvRegions)
    TextView fragmentInformationRestaurantClientTvRegions;
    @BindView(R.id.fragmentInformationRestaurantClientTvMinimum)
    TextView fragmentInformationRestaurantClientTvMinimum;
    @BindView(R.id.fragmentInformationRestaurantClientTvDeliveryCost)
    TextView fragmentInformationRestaurantClientTvDeliveryCost;


    private View view;
    private APiSofraResturant apiServerRestaurant;
    private int idRestaurant;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_information_restaurant_client_sofra, container, false);

        unbinder = ButterKnife.bind(this, view);
        // initializer tools
        inti();

        // this is getReviewsRestaurant
        getReviewsRestaurant();


        return view;
    }

    // initializer tools
    @SuppressLint("RestrictedApi")
    private void inti() {
        apiServerRestaurant = RetrofitSofra.getRestaurant().create(APiSofraResturant.class);

        // get id restaurant from return  Content Component

        Bundle arguments =getArguments();
        if (arguments != null) {
            idRestaurant = arguments.getInt("idRestaurantContentComponent");
        }
     }

    //   get Reviews Restaurant
    private void getReviewsRestaurant() {
        fragmentInformationRestaurantClientPbWait.setVisibility(View.VISIBLE);
        apiServerRestaurant.getInformationRestaurantClient(idRestaurant)
                .enqueue(new Callback<InformationRestaurant>() {
                    @Override
                    public void onResponse(Call<InformationRestaurant> call, Response<InformationRestaurant> response) {
                        try {
                            if (response.body().getStatus() == 1) {
                                fragmentInformationRestaurantClientPbWait.setVisibility(View.INVISIBLE);
                                     fragmentInformationRestaurantClientTvState.setText(response.body().getData().getAvailability());
                                    fragmentInformationRestaurantClientTvCity.setText(response.body().getData().getRegion().getCity().getName());
                                    fragmentInformationRestaurantClientTvRegions.setText(response.body().getData().getRegion().getName());
                                    fragmentInformationRestaurantClientTvMinimum.setText(response.body().getData().getMinimumCharger());
                                    fragmentInformationRestaurantClientTvDeliveryCost.setText(""+response.body().getData().getDeliveryCost());



                            } else {
                                Log.d("response", response.body().getMsg());
                                fragmentInformationRestaurantClientPbWait.setVisibility(View.INVISIBLE);
                            }
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    }

                    @Override
                    public void onFailure(Call<InformationRestaurant> call, Throwable t) {
                        Log.d("response", t.getMessage());
                    }
                });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
