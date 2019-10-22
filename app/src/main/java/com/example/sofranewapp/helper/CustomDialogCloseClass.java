package com.example.sofranewapp.helper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
 import android.widget.ImageView;

import com.example.sofranewapp.R;
import com.example.sofranewapp.data.api.APiSofraClint;
import com.example.sofranewapp.data.api.RetrofitSofra;
import com.example.sofranewapp.data.model.notifyToken.NotifyToken;
import com.example.sofranewapp.ui.activity.MainHomeActivity;
import com.google.firebase.iid.FirebaseInstanceId;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofranewapp.data.local.SharedPreferncesMangerClient.LoadData;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerClient.USER_API_TOKEN;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerClient.clean;


public class CustomDialogCloseClass extends Dialog  {

    public Activity activity;

     @BindView(R.id.closeDialogRestaurantClientBtnNo)
    ImageView closeDialogRestaurantClientBtnNo;
    @BindView(R.id.closeDialogRestaurantClientBtnYes)
    ImageView closeDialogRestaurantClientBtnYes;

   APiSofraClint apiServerClient;

    public CustomDialogCloseClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.activity = a;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_close_dialog_restaurant_client_sofra);
        ButterKnife.bind(this);

        apiServerClient = RetrofitSofra.getRestaurant().create(APiSofraClint.class);

    }



    @OnClick({R.id.closeDialogRestaurantClientBtnNo, R.id.closeDialogRestaurantClientBtnYes})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.closeDialogRestaurantClientBtnNo:
                activity.onBackPressed();
                break;
            case R.id.closeDialogRestaurantClientBtnYes:
                RemoveToken();
                clean(activity);
                activity.startActivity(new Intent(getContext(), MainHomeActivity.class));
                break;
            default:
                break;
        }
        dismiss();
    }



    //send Remove Token
    public void RemoveToken() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        // Remove Token
        apiServerClient.RemoveToken(refreshedToken, LoadData(activity, USER_API_TOKEN)).enqueue(new Callback<NotifyToken>() {
            @Override
            public void onResponse(Call<NotifyToken> call, Response<NotifyToken> response) {

                try {
                    if (response.body().getStatus() == 1) {
//                        Toast.makeText(activity, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    } else {
                        //Toast.makeText(getApplication(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.getMessage();
                }

            }

            @Override
            public void onFailure(Call<NotifyToken> call, Throwable t) {

            }
        });

    }
}