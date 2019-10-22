package com.example.sofranewapp.ui.activity.user;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.sofranewapp.R;
import com.example.sofranewapp.helper.HelperMathod;
import com.example.sofranewapp.ui.fragment.user.AllRestaurantClientFragment;
import com.example.sofranewapp.ui.fragment.user.CartItemRestaurantClientFragment;
import com.example.sofranewapp.ui.fragment.user.MoreRestaurantClientFragment;
import com.example.sofranewapp.ui.fragment.user.RequestsOrderRestaurantClientComponentFragment;
import com.example.sofranewapp.ui.fragment.user.UpdateProfileRegisterClientFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.sofranewapp.data.local.SharedPreferncesMangerClient.LoadData;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerClient.USER_NAME;
import static com.example.sofranewapp.helper.HelperMathod.getStartFragments;


public class MainClient extends AppCompatActivity {

    int checkopenLogin = 0;
    public CartItemRestaurantClientFragment cartItemRestaurantClientFragment;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getStartFragments(getSupportFragmentManager(), R.id.mainClientReplaceFragment, new AllRestaurantClientFragment());
                    return true;
                case R.id.navigation_requests:
                    getStartFragments(getSupportFragmentManager(), R.id.mainClientReplaceFragment, new RequestsOrderRestaurantClientComponentFragment());

                    return true;
                case R.id.navigation_User:
                    getStartFragments(getSupportFragmentManager(), R.id.mainClientReplaceFragment, new UpdateProfileRegisterClientFragment());

                    return true;
                case R.id.sitting:
                    getStartFragments(getSupportFragmentManager(), R.id.mainClientReplaceFragment, new MoreRestaurantClientFragment());

                    return true;
            }
            return false;
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_client_sofra);
        ButterKnife.bind(this);

        BottomNavigationView navView = findViewById(R.id.btn_nav_view);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        HelperMathod.ToolBarClient(getSupportFragmentManager(), this, toolbar, LoadData(MainClient.this, USER_NAME));
        toolbar.setNavigationIcon(getDrawable(R.drawable.shopping_cart));

        cartItemRestaurantClientFragment = new CartItemRestaurantClientFragment();
        cartItemRestaurantClientFragment.mainClient = this;

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStartFragments(getSupportFragmentManager(), R.id.mainClientReplaceFragment
                        , cartItemRestaurantClientFragment);
            }
        });

        Intent openLogin = getIntent();
            if (openLogin != null) {
                checkopenLogin  = openLogin.getIntExtra("openLogin",0);
            }

        if (checkopenLogin == 0) {
            getStartFragments(getSupportFragmentManager(), R.id.mainClientReplaceFragment, new AllRestaurantClientFragment());
        } else {
            getStartFragments(getSupportFragmentManager(), R.id.mainClientReplaceFragment, new CartItemRestaurantClientFragment());
            checkopenLogin = 0;
        }


    }

}
