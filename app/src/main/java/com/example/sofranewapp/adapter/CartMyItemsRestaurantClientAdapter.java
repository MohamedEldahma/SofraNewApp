package com.example.sofranewapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sofranewapp.R;
import com.example.sofranewapp.data.local.AppRoomDatabase;
import com.example.sofranewapp.data.model.Item;
import com.example.sofranewapp.ui.activity.user.MainClient;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.sofranewapp.helper.HelperMathod.LodeImage;

public class CartMyItemsRestaurantClientAdapter extends RecyclerView.Adapter<CartMyItemsRestaurantClientAdapter.ViewHolder> {

   public List<Item> ordersArrayList;

    Activity context;
    private MainClient mainClient;

    private AppRoomDatabase database;
    String keyRequest;
    private View itemLayoutView;
    private int quantity;
    private ViewHolder viewHolder;
    private int ii;
    public static Integer IdRestauran;

    public CartMyItemsRestaurantClientAdapter(List<Item> ordersArrayList, Activity context, MainClient mainClient) {
        this.ordersArrayList = ordersArrayList;
        this.context = context;
        this.mainClient= mainClient;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        // create a new view layout pending
        itemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.set_adapter_cart_item_restaurant_client_sofra, null);
        // create ViewHolder
        viewHolder = new ViewHolder(itemLayoutView);


         database = AppRoomDatabase.getAppDatabase(context);


        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        // set title
        viewHolder.setAdapterCartTvNameItem.setText(ordersArrayList.get(i).getName());
        viewHolder.setAdapterCartTvDetailsItem.setText(ordersArrayList.get(i).getDescription());
        viewHolder.setAdapterCartTvQuantityItem.setText("" + ordersArrayList.get(i).getQuantity());
        viewHolder.setAdapterCartTvPriceItems.setText(ordersArrayList.get(i).getPrice());
        viewHolder.setAdapterCartTvTotalPriceItems.setText("" + Double.valueOf(ordersArrayList.get(i).getPrice()) * Double.valueOf(ordersArrayList.get(i).getQuantity()));
        LodeImage(context, ordersArrayList.get(i).getImageUrl(), viewHolder.setAdapterCartImgPhotoItem);

        IdRestauran=  ordersArrayList.get(i).getIdRestaurant();

        Log.d("getIdRestaurant", String.valueOf(IdRestauran));

        viewHolder.setAdapterCartImgSubtractItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity = Integer.parseInt(viewHolder.setAdapterCartTvQuantityItem.getText().toString());
                if (quantity != 1) {
                    quantity--;
                    viewHolder.setAdapterCartTvQuantityItem.setText("" + quantity);
                    // update quantity items
                    database.getItemDAO().update(ordersArrayList.get(i).getIdItems(), quantity);

                    viewHolder.setAdapterCartTvTotalPriceItems.setText("" + Double.valueOf(ordersArrayList.get(i).getPrice()) * quantity);

//                    mainClient.cartItemRestaurantClientFragment.cartItemRestaurantClientFragmentTvTotalCart.setText(""+getTotal(quantity));
                 }

            }
        });
        viewHolder.setAdapterCartImgPlusItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity = Integer.parseInt(viewHolder.setAdapterCartTvQuantityItem.getText().toString());
                quantity++;
                viewHolder.setAdapterCartTvQuantityItem.setText("" + quantity);


                // update quantity items
                database.getItemDAO().update(ordersArrayList.get(i).getIdItems(), quantity);

                viewHolder.setAdapterCartTvTotalPriceItems.setText("" + Double.valueOf(ordersArrayList.get(i).getPrice()) *quantity);

//                mainClient.cartItemRestaurantClientFragment.cartItemRestaurantClientFragmentTvTotalCart.setText(""+getTotal(quantity));

            }
        });
        viewHolder.setAdapterCartImgDeleteItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.getItemDAO().delete(ordersArrayList.get(i).getIdItems());

                ordersArrayList.remove(i);

//                mainClient.cartItemRestaurantClientFragment.cartItemRestaurantClientFragmentTvTotalCart.setText(""+getTotal(quantity));
                mainClient.cartItemRestaurantClientFragment.cartMyItemsRestaurantClientAdapter.notifyDataSetChanged();
                if (ordersArrayList.isEmpty()){
                    database.getItemDAO().deleteAll();
                }
            }
        });
    }
    // total item list;

    public double getTotal() {
        double AllTotal = 0;
        try {

            if (ordersArrayList.isEmpty())
                return AllTotal;

            for (int i = 0; i <  database.getItemDAO().getItems().size(); i++) {
                AllTotal = AllTotal + Double.valueOf( database.getItemDAO().getItems().get(i).getPrice()) * Double.valueOf( database.getItemDAO().getItems().get(i).getQuantity());
            }

        }catch (Exception e){
            e.getMessage();
        }

        return AllTotal;
    }


     public double getTotal(double quantity) {
         double AllTotal = 0;

        try {

            for (int i = 0; i < ordersArrayList.size(); i++) {

                AllTotal = AllTotal + Double.valueOf(ordersArrayList.get(i).getPrice()) * quantity ;
            }

        }catch (Exception e){
            e.getMessage();
        }

        return AllTotal;
    }

    @Override
    public int getItemCount() {
        return ordersArrayList.size();
    }


    // inner class to hold a reference to each item of RecyclerView
    class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        @BindView(R.id.setAdapterCartImgPhotoItem)
        ImageView setAdapterCartImgPhotoItem;
        @BindView(R.id.setAdapterCartTvNameItem)
        TextView setAdapterCartTvNameItem;
        @BindView(R.id.setAdapterCartTvDetailsItem)
        TextView setAdapterCartTvDetailsItem;
        @BindView(R.id.setAdapterCartImgSubtractItem)
        ImageView setAdapterCartImgSubtractItem;
        @BindView(R.id.setAdapterCartTvQuantityItem)
        TextView setAdapterCartTvQuantityItem;
        @BindView(R.id.setAdapterCartImgPlusItem)
        ImageView setAdapterCartImgPlusItem;
        @BindView(R.id.setAdapterCartTvPriceItems)
        TextView setAdapterCartTvPriceItems;
        @BindView(R.id.setAdapterCartTvTotalPriceItems)
        TextView setAdapterCartTvTotalPriceItems;
        @BindView(R.id.setAdapterCartImgDeleteItems)
        ImageView setAdapterCartImgDeleteItems;

        ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            view = itemLayoutView;
            ButterKnife.bind(this, view);
        }
    }

}
