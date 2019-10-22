package com.example.sofranewapp.ui.fragment.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.sofranewapp.R;
import com.example.sofranewapp.data.api.APiSofraClint;
import com.example.sofranewapp.data.api.APiSofraResturant;
import com.example.sofranewapp.data.api.RetrofitSofra;
import com.example.sofranewapp.data.local.AppRoomDatabase;
import com.example.sofranewapp.data.model.informationRestaurant.InformationRestaurant;
import com.example.sofranewapp.data.model.newOrder.NewOrder;
import com.example.sofranewapp.data.model.updateProfileClient.ProfileClient;

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

import static com.example.sofranewapp.data.local.SharedPreferncesMangerClient.LoadData;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerClient.USER_API_TOKEN;
import static com.example.sofranewapp.helper.HelperMathod.dismissProgressDialog;
import static com.example.sofranewapp.helper.HelperMathod.getStartFragments;
import static com.example.sofranewapp.helper.HelperMathod.showProgressDialog;

public class ConfirmOrderDetailsRestaurantClientFragment extends Fragment {


    Unbinder unbinder;

    @BindView(R.id.confirmOrderDetailsRestaurantFragmentClientEtNote)
    EditText confirmOrderDetailsRestaurantFragmentClientEtNote;
    @BindView(R.id.confirmOrderDetailsRestaurantFragmentClientTvAddress)
    EditText confirmOrderDetailsRestaurantFragmentClientTvAddress;
    @BindView(R.id.confirmOrderDetailsRestaurantFragmentClientRbPayOnline)
    RadioButton confirmOrderDetailsRestaurantFragmentClientRbPayOnline;
    @BindView(R.id.confirmOrderDetailsRestaurantFragmentClientRbCash)
    RadioButton confirmOrderDetailsRestaurantFragmentClientRbCash;
    @BindView(R.id.confirmOrderDetailsRestaurantFragmentClientRgContainer)
    RadioGroup confirmOrderDetailsRestaurantFragmentClientRgContainer;
    @BindView(R.id.confirmOrderDetailsRestaurantFragmentClientTvTotal)
    TextView confirmOrderDetailsRestaurantFragmentClientTvTotal;
    @BindView(R.id.confirmOrderDetailsRestaurantFragmentClientTvDeliveryCost)
    TextView confirmOrderDetailsRestaurantFragmentClientTvDeliveryCost;
    @BindView(R.id.confirmOrderDetailsRestaurantFragmentClientTvTotalAmount)
    TextView confirmOrderDetailsRestaurantFragmentClientTvTotalAmount;
    @BindView(R.id.confirmOrderDetailsRestaurantFragmentClientBtnConfirm)
    Button confirmOrderDetailsRestaurantFragmentClientBtnConfirm;
    @BindView(R.id.showOrderRestaurantFragmentItemProgressBar)
    ProgressBar showOrderRestaurantFragmentItemProgressBar;

    private APiSofraResturant apiServerRestaurant;

    APiSofraClint apiServerClient;
    private View view;

     private int getTotal;
    private int getIdRestaurant;
     private int paymentMethods=1;

    AppRoomDatabase database;
    List<Integer>  items =new ArrayList<>();
    List<Integer> quantities=new ArrayList<>();
    List<String>  notes=new ArrayList<>();
    private String getPhone;
    private String getName;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.confirm_order_details_restaurant_client_fragment_sofra, container, false);

        unbinder = ButterKnife.bind(this, view);
        // initializer tools
        inti();


        return view;
    }

    // initializer tools
    @SuppressLint("SetTextI18n")
    private void inti() {

        database = AppRoomDatabase.getAppDatabase(getContext());

        /// get date MyItemRestaurantFragment

        apiServerRestaurant = RetrofitSofra.getRestaurant().create(APiSofraResturant.class);
        apiServerClient = RetrofitSofra.getRestaurant().create(APiSofraClint.class);

        showOrderRestaurantFragmentItemProgressBar.setVisibility(View.INVISIBLE);


        Bundle bundle = getArguments();
        if (bundle != null) {
            getTotal = bundle.getInt("getTotal");
            getIdRestaurant = bundle.getInt("getIdRestaurant");
        }


        getDataClient();

        getReviewsRestaurant();

        confirmOrderDetailsRestaurantFragmentClientTvTotal.setText("" + getTotal);

        for (int i = 0; i < database.getItemDAO().getItems().size(); i++) {

            items.add(database.getItemDAO().getItems().get(i).getIdItems());
            quantities.add(database.getItemDAO().getItems().get(i).getQuantity());
            notes.add(database.getItemDAO().getItems().get(i).getDescription());

        }
    }

    private void getDataClient() {

        showOrderRestaurantFragmentItemProgressBar.setVisibility(View.VISIBLE);
//        LoadData(getActivity(),USER_API_TOKEN);

        apiServerClient.getProfileClient(LoadData(getActivity(), USER_API_TOKEN)).enqueue(new Callback<ProfileClient>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ProfileClient> call, Response<ProfileClient> response) {
                try {
                    Log.d("response", response.body().getMsg());

                    if (response.body().getStatus() == 1) {

                        Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_LONG).show();


                        confirmOrderDetailsRestaurantFragmentClientTvAddress.setText(response.body().getData().getUser().getRegion().getCity().getName()
                                + " - " + response.body().getData().getUser().getRegion().getName());

                        getPhone =  response.body().getData().getUser().getPhone();
                        getName =  response.body().getData().getUser().getName();
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
            public void onFailure(Call<ProfileClient> call, Throwable t) {
                Log.d("response", t.getMessage());
            }
        });
    }


    //   get Reviews Restaurant
    private void getReviewsRestaurant() {
        showOrderRestaurantFragmentItemProgressBar.setVisibility(View.VISIBLE);

        apiServerRestaurant.getInformationRestaurantClient(getIdRestaurant)
                .enqueue(new Callback<InformationRestaurant>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(Call<InformationRestaurant> call, Response<InformationRestaurant> response) {
                        try {
                            if (response.body().getStatus() == 1) {

                                confirmOrderDetailsRestaurantFragmentClientTvDeliveryCost.setText("" + response.body().getData().getDeliveryCost());

                                confirmOrderDetailsRestaurantFragmentClientTvTotalAmount.setText(String.valueOf(getTotal + response.body().getData().getDeliveryCost()));

                                Log.d("getDeliveryCost", String.valueOf(getTotal + response.body().getData().getDeliveryCost()));

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


    @OnClick({R.id.confirmOrderDetailsRestaurantFragmentClientRbPayOnline, R.id.confirmOrderDetailsRestaurantFragmentClientBtnConfirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.confirmOrderDetailsRestaurantFragmentClientRbPayOnline:
                // Is the view now checked?
                boolean checked = ((RadioButton) view).isChecked();
                // Check which RadioButton was clicked
                switch (view.getId()) {
                    case R.id.confirmOrderDetailsRestaurantFragmentClientRbPayOnline:
                        if(checked)
                        paymentMethods = 2;
                        break;
                    case R.id.confirmOrderDetailsRestaurantFragmentClientRgContainer:
                        if(checked)

                        paymentMethods = 1;
                        break;
                    // Perform your logic
                }
                break;
            case R.id.confirmOrderDetailsRestaurantFragmentClientBtnConfirm:
                addOrder();
                break;
        }
    }

    //   get add new-order
    private void addOrder() {

        showProgressDialog(getActivity(),"Wait..");
        apiServerClient.newOrder(LoadData(getActivity(), USER_API_TOKEN), getIdRestaurant,confirmOrderDetailsRestaurantFragmentClientEtNote.getText().toString()
                ,confirmOrderDetailsRestaurantFragmentClientTvAddress.getText().toString(), paymentMethods,getPhone,getName,items,quantities,notes )
                .enqueue(new Callback<NewOrder>() {
                    @Override
                    public void onResponse(Call<NewOrder> call, Response<NewOrder> response) {

                        try {

                            if (response.body().getStatus() == 1) {

                                Log.d("response2", response.body().getMsg());

                                Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                database.getItemDAO().deleteAll();


                                /// add data in fragment
                                Bundle bundle = new Bundle();
                                bundle.putInt("getId", response.body().getData().getOrder().getId());

                                // start fragment  Details Order Restaurant Client
                                Fragment fragment = new DetailsOrderRestaurantClientFragment();
                                fragment.setArguments(bundle);

                                assert getFragmentManager() != null;
                                getStartFragments(getFragmentManager(), R.id.mainClientReplaceFragment, fragment);

                                dismissProgressDialog();

                            } else {
                                Log.d("response", response.body().getMsg());
                                dismissProgressDialog();
                            }
                        } catch (Exception e) {
                            e.getMessage();
                            dismissProgressDialog();
                        }

                    }

                    @Override
                    public void onFailure(Call<NewOrder> call, Throwable t) {
                        Log.d("response", t.getMessage());
                        dismissProgressDialog();
                    }
                });

    }
}
