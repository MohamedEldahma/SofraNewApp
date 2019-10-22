package com.example.sofranewapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.sofranewapp.R;
import com.example.sofranewapp.ui.activity.resturant.LoginRestaurantActivity;
import com.example.sofranewapp.ui.activity.resturant.MainRestaurant;
import com.example.sofranewapp.ui.activity.user.LoginClientActivity;
import com.example.sofranewapp.ui.activity.user.MainClient;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


import static com.example.sofranewapp.data.local.SharedPreferncesMangerClient.KEY_IS_CHECK_BOX1;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerClient.LoadBooleanClient;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerClient.setSharedPreferencesClient;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.LoadBooleanRestaurant;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.setSharedPreferencesRestaurant;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.KEY_IS_CHECK_BOX;


public class MainHomeActivity extends AppCompatActivity {

    @BindView(R.id.mainLogoImg)
    ImageView mainLogoImg;
    @BindView(R.id.mainOrderFoodBtn)
    Button mainOrderFoodBtn;
    @BindView(R.id.mainBuyFoodBtn)
    Button mainBuyFoodBtn;
    @BindView(R.id.imageView3)
    ImageView imageView3;
    @BindView(R.id.imageView5)
    ImageView imageView5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        ButterKnife.bind(this);
//        getSupportFragmentManager().beginTransaction().add(R.id.main_hom_login_framactivity,new HomeSofraFragment()).commit();

        // initialize ShardPreferences
        setSharedPreferencesRestaurant(MainHomeActivity.this);
        setSharedPreferencesClient(MainHomeActivity.this);
    }

    @OnClick({R.id.mainOrderFoodBtn, R.id.mainBuyFoodBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mainOrderFoodBtn:
//                // check is checkBox is Checked
//                if (LoadBooleanClient(MainHomeActivity.this, KEY_IS_CHECK_BOX1)) {
                    startActivity(new Intent(MainHomeActivity.this, MainClient.class));
//                } else {
//                    startActivity(new Intent(MainHomeActivity.this, LoginClientActivity.class));
//                }

                break;
            case R.id.mainBuyFoodBtn:
                // check is checkBox is Checked
                if (LoadBooleanRestaurant(MainHomeActivity.this, KEY_IS_CHECK_BOX)) {
                    startActivity(new Intent(MainHomeActivity.this, MainRestaurant.class));
                } else {
                    startActivity(new Intent(MainHomeActivity.this, LoginRestaurantActivity.class));
                }

                break;
        }
    }
}
