package com.example.sofranewapp.ui.activity.resturant;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.sofranewapp.R;
import com.example.sofranewapp.ui.fragment.resturant.CommissionRestaurntFragment;
import com.example.sofranewapp.ui.fragment.resturant.EditProfileRegisterRestaurantFragment;
import com.example.sofranewapp.ui.fragment.resturant.MoreRestaurantFragment;
import com.example.sofranewapp.ui.fragment.resturant.MyItemRestaurantFragment;
import com.example.sofranewapp.ui.fragment.resturant.RequestsRestaurantComponentFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.LoadData;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.USER_NAME;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.setSharedPreferencesRestaurant;
import static com.example.sofranewapp.helper.HelperMathod.ToolBarRestaurant;
import static com.example.sofranewapp.helper.HelperMathod.getStartFragments;

public class MainRestaurant extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    getStartFragments(getSupportFragmentManager(), R.id.mainRestaurantReplaceFragment, new MyItemRestaurantFragment());
                    return true;
                case R.id.navigation_User:
                    getStartFragments(getSupportFragmentManager()
                            , R.id.mainRestaurantReplaceFragment, new EditProfileRegisterRestaurantFragment());
                    return true;
                case R.id.navigation_requests:
                    getStartFragments(getSupportFragmentManager(), R.id.mainRestaurantReplaceFragment, new RequestsRestaurantComponentFragment());

                    return true;
                case R.id.sitting:
                    getStartFragments(getSupportFragmentManager(), R.id.mainRestaurantReplaceFragment, new MoreRestaurantFragment());



                    return true;
            }
            return false;
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_restaurant_sofra);
        ButterKnife.bind(this);

        BottomNavigationView navView = findViewById(R.id.btn_nav_view);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //initialize ShardPreferences
        setSharedPreferencesRestaurant(MainRestaurant.this);
        // initialize toolbar
        ToolBarRestaurant(getSupportFragmentManager(), this, toolbar, LoadData(MainRestaurant.this, USER_NAME));
        toolbar.setNavigationIcon(getDrawable(R.drawable.icon_calculator));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStartFragments(getSupportFragmentManager(), R.id.mainRestaurantReplaceFragment
                        , new CommissionRestaurntFragment());
            }
        });
        getStartFragments(getSupportFragmentManager(), R.id.mainRestaurantReplaceFragment, new MyItemRestaurantFragment());

    }

//
//    // get Notifications Count
//    public void getNotificationsCount() {
//        try {
//            ApiServer apiServer = getClient().create(ApiServer.class);
//            // get  PaginationData  post
//            apiServer.getNotificationsCount(LoadData(HomeNavgation.this, USER_API_TOKEN)).enqueue(new Callback<NotificationsCount>() {
//
//                @Override
//                public void onResponse(Call<NotificationsCount> call, Response<NotificationsCount> response) {
//
////                    Log.d(" notifications 5", response.body().getMsg());
//
//                    if (response.body().getStatus() == 1) {
//
//                        getNotificationsCount = String.valueOf(response.body().getDataItem().getNotificationsCount());
//
//                        textCartItemCount.setText(getNotificationsCount);
//
//
//                    } else {
//                        Toast.makeText(HomeNavgation.this, "Not PaginationData ", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<NotificationsCount> call, Throwable t) {
//                    Toast.makeText(HomeNavgation.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        } catch (Exception e) {
//            e.getMessage();
//        }
//    }
}
