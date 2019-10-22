package com.example.sofranewapp.ui.fragment.user;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sofranewapp.R;
import com.example.sofranewapp.data.local.AppRoomDatabase;
import com.example.sofranewapp.data.model.Item;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import static com.example.sofranewapp.adapter.ShowRestaurantClientRecyclerAdapter.idRestaurant;

import static com.example.sofranewapp.helper.HelperMathod.LodeImage;


public class AddItemCartRestaurantClientFragment extends Fragment {

    View view;
    @BindView(R.id.showItemRestaurantFragmentClientIvPhotoItems)
    ImageView showItemRestaurantFragmentClientIvPhotoItems;
    @BindView(R.id.showItemRestaurantFragmentClientTxvNameItems)
    TextView showItemRestaurantFragmentClientTxvNameItems;
    @BindView(R.id.showItemRestaurantFragmentClientTxvDetailsItems)
    TextView showItemRestaurantFragmentClientTxvDetailsItems;
    @BindView(R.id.showItemRestaurantFragmentClientTxvPriceItems)
    TextView showItemRestaurantFragmentClientTxvPriceItems;
    @BindView(R.id.showItemRestaurantFragmentClientTxvOffersPriceItems)
    TextView showItemRestaurantFragmentClientTxvOffersPriceItems;
    @BindView(R.id.showItemRestaurantFragmentClientTvPreparingTimeItems)
    TextView showItemRestaurantFragmentClientTvPreparingTimeItems;
    @BindView(R.id.showItemRestaurantFragmentClientEtPrivateOrderItems)
    EditText showItemRestaurantFragmentClientEtPrivateOrderItems;
    @BindView(R.id.showItemRestaurantFragmentClientImgSubtractItems)
    ImageView showItemRestaurantFragmentClientImgSubtractItems;
    @BindView(R.id.showItemRestaurantFragmentClientTvQuantityItems)
    TextView showItemRestaurantFragmentClientTvQuantityItems;
    @BindView(R.id.showItemRestaurantFragmentClientImgPlusItems)
    ImageView showItemRestaurantFragmentClientImgPlusItems;
    @BindView(R.id.showItemRestaurantFragmentClientImgAddItems)
    ImageView showItemRestaurantFragmentClientImgAddItems;
    Unbinder unbinder;

    private String getName, getDescription, getPrice, getPreparingTime, getPhotoUrl;
    private int idItem;
    private int quantity;
    private AppRoomDatabase database;
    private boolean checkItems = true;
    private boolean checkIdResruarant = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add_items_cart_restaurant_client_sofra, container, false);

        unbinder = ButterKnife.bind(this, view);

        database = AppRoomDatabase.getAppDatabase(getContext());

        // initializer tools
        inti();

        return view;
    }

    // initializer tools
    @SuppressLint("RestrictedApi")
    private void inti() {
        // get data Menu Restaurant Client Recycler Adapter
        Bundle bundle = getArguments();
        if (bundle != null) {

            idItem = bundle.getInt("getIdItem");
            getName = bundle.getString("getName");
            getDescription = bundle.getString("getDescription");
            getPrice = bundle.getString("getPrice");
            getPreparingTime = bundle.getString("getPreparingTime");
            getPhotoUrl = bundle.getString("getPhotoUrl");
        }

        showItemRestaurantFragmentClientTxvNameItems.setText(getName);
        showItemRestaurantFragmentClientTxvDetailsItems.setText(getDescription);
        showItemRestaurantFragmentClientTxvPriceItems.setText(getPrice);
        showItemRestaurantFragmentClientTvPreparingTimeItems.setText(getPreparingTime);

        LodeImage(getContext(), getPhotoUrl, showItemRestaurantFragmentClientIvPhotoItems);


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.showItemRestaurantFragmentClientImgSubtractItems, R.id.showItemRestaurantFragmentClientImgPlusItems, R.id.showItemRestaurantFragmentClientImgAddItems})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.showItemRestaurantFragmentClientImgSubtractItems:
                quantity = Integer.parseInt(showItemRestaurantFragmentClientTvQuantityItems.getText().toString());
                if (quantity != 1) {
                    quantity--;
                    showItemRestaurantFragmentClientTvQuantityItems.setText("" + quantity);
                }

                break;
            case R.id.showItemRestaurantFragmentClientImgPlusItems:

                quantity = Integer.parseInt(showItemRestaurantFragmentClientTvQuantityItems.getText().toString());
                quantity++;
                showItemRestaurantFragmentClientTvQuantityItems.setText("" + quantity);
                break;
            case R.id.showItemRestaurantFragmentClientImgAddItems:

                database = AppRoomDatabase.getAppDatabase(getContext());

                final Item item = new Item(idRestaurant, idItem, getName, getDescription, Integer.valueOf(showItemRestaurantFragmentClientTvQuantityItems.getText().toString()), getPhotoUrl, getPrice, 0);

                if (database.getItemDAO().getItems().size() == 0) {
                    database.getItemDAO().insert(item);
                    return;
                }

                if (database.getItemDAO().getItemByIdRestaurant().size() == 0) {
                    database.getItemDAO().insert(item);
                    return;
                }

                for (int i = 0; i < database.getItemDAO().getItems().size(); i++) {

                    Log.d("d", "Not " + idRestaurant + " == " + database.getItemDAO().getItems().get(i).getIdRestaurant());

                    if (database.getItemDAO().getItems().get(i).getIdRestaurant().equals(idRestaurant)) {


                        Log.d("******", "" + idItem + " == " + database.getItemDAO().getItems().get(i).getIdItems());

                        // change  check Id Restaurant ==  true
                        checkIdResruarant = true;
                    }
                }

                // if check id restaurant is find
                if (checkIdResruarant) {

                    for (int j = 0; j < database.getItemDAO().getItems().size(); j++) {
                        Log.d("getItems", "" + idItem +
                                " == " + database.getItemDAO().getItems().get(j).getIdItems());

                        // is id item is find from database == false = update  > true == insert
                        if (database.getItemDAO().getItems().get(j).getIdItems().equals(idItem)) {
                            // change  check  Items ==  false
                            checkItems = false;
                        }
                    }
                    // change  check Id Restaurant ==  false
                    checkIdResruarant = false;
                } else {
                    // change  check  Items ==  false
                    checkItems = false;

                    // if is table item is not null
                    if (database.getItemDAO().getItems().size() != 0) {

                        Toast.makeText(getContext(), "Not choose anther restaurant", Toast.LENGTH_LONG).show();

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("هل تريد استبدال بهذا المطعم");
                        builder.setMessage("هيتم مسح الطلبات الموجوده في سله");
                        builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                            Integer getIdRestaurant = database.getItemDAO().getItemByIdRestaurant().getIdRestaurant();

                                database.getItemDAO().deleteAll();

                                database.getItemDAO().insert(item);

                                //  getActivity().onBackPressed();
                                return;
                            }
                        });
                        builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                        builder.show();
                    } else {
                        database.getItemDAO().insert(item);
                        Log.d("insert3", "getItems() == null" + idItem);
                    }
                }
                // if check item true == insert or false == update
                if (checkItems) {

                    database.getItemDAO().insert(item);
                    Log.d("insert666", "" + idItem);
                    checkItems = false;

                    getActivity().onBackPressed();

                } else {

                    Log.d("update", "" + idItem);

                    // update quantity items
                    database.getItemDAO().update(idItem, Integer.valueOf(showItemRestaurantFragmentClientTvQuantityItems.getText().toString()));
                    // close fragment

                    Toast.makeText(getContext(),"done",Toast.LENGTH_LONG).show();
                    checkItems = true;
                }
                break;
        }
    }
}
