package com.example.sofranewapp.ui.activity.resturant;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sofranewapp.R;
import com.example.sofranewapp.data.api.APiSofraResturant;
import com.example.sofranewapp.data.api.RetrofitSofra;
import com.example.sofranewapp.data.model.loginresturant.LoginResturant;
import com.example.sofranewapp.data.model.notifyToken.NotifyToken;
import com.google.firebase.iid.FirebaseInstanceId;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.KEY_IS_CHECK_BOX;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.Key_password;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.LoadBooleanRestaurant;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.LoadData;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.SaveData;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.USER_API_TOKEN;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.USER_EMAIL;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.USER_ID;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.USER_NAME;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.USER_PHONE;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.setSharedPreferencesRestaurant;

public class LoginRestaurantActivity extends Activity {


    @BindView(R.id.loginActivityEditUserPassword)
    EditText loginFragmentEditUserPassword;
    @BindView(R.id.loginActivityTxtForgetPassword)
    TextView loginFragmentTxtForgetPassword;
    @BindView(R.id.loginActivityBtnLogin)
    Button loginFragmentBtnLogin;
    TextView loginActivityTvRegister;
    Unbinder unbinder;
    @BindView(R.id.loginActivityChkBox)
    CheckBox loginFragmentChkBox;

    APiSofraResturant apiServerRestaurant;
    ProgressBar loginActivityProgressBar;
    @BindView(R.id.loginActivityEditUserEmail)
    EditText loginFragmentEditUserEmail;

    private boolean Checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sofra);
        ButterKnife.bind(this);

        //initializer
        inti();
        // initialize ShardPreferences
        setSharedPreferencesRestaurant(this);
        // Class All OnClick
        ClassAllOnClick();
        // get data all GeneratedDataUser
        getDataUserShrPreferences();

        // class login retrofit
        ClassLogin();
    }


    private void inti() {
        loginActivityProgressBar = findViewById(R.id.loginActivityProgressBar);
        loginActivityTvRegister = findViewById(R.id.loginActivityTvRegister);
        apiServerRestaurant = RetrofitSofra.getRestaurant().create(APiSofraResturant.class);

    }

    private void ClassAllOnClick() {

        /// on click register
        loginActivityTvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getFragmentManager() != null) {
                    startActivity(new Intent(LoginRestaurantActivity.this, RegisterRestaurantActivity.class));
                }

            }
        });

        loginFragmentChkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Checked = isChecked;
            }
        });
        loginFragmentTxtForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null) {
                    Intent intent = new Intent(LoginRestaurantActivity.this, ForgetPasswordRestaurantActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


    // class login retrofit
    private void ClassLogin() {
        loginFragmentBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!loginFragmentEditUserEmail.getText().toString().isEmpty()
                        && !loginFragmentEditUserPassword.getText().toString().isEmpty()) {

                    loginActivityProgressBar.setVisibility(View.VISIBLE);

              apiServerRestaurant.onLogin(loginFragmentEditUserEmail.getText().toString()
                            , loginFragmentEditUserPassword.getText().toString())
                      .enqueue(new Callback<LoginResturant>() {
                        @Override
                        public void onResponse(Call<LoginResturant> call, Response<LoginResturant> response) {
                            try {

                                Toast.makeText(LoginRestaurantActivity.this, response.body().getMsg() + "dss", Toast.LENGTH_SHORT).show();
                                Log.d("response", response.body().getData().getUser().getEmail());
                                Log.d("getStatus", String.valueOf(response.body().getStatus()));

                                if (response.body().getStatus() == 1) {
                                    Log.d("response", response.body().getData().getUser().getEmail());
                                    Intent intent = new Intent(LoginRestaurantActivity.this, MainRestaurant.class);
                                    startActivity(intent);

                                    // check is checkBox is Checked
                                    if (loginFragmentChkBox.isChecked()) {
                                        // save data login user
                                        ClassSharedPreferences(response.body().getData().getUser().getId()
                                                , response.body().getData().getApiToken()
                                                , response.body().getData().getUser().getName(),
                                                response.body().getData().getUser().getEmail(),
                                                response.body().getData().getUser().getPhone()
                                                , loginFragmentEditUserPassword.getText().toString());

                                        loginActivityProgressBar.setVisibility(View.INVISIBLE);

                                    } else {
                                        loginActivityProgressBar.setVisibility(View.INVISIBLE);
                                    }

                                    Intent intent1 = new Intent(LoginRestaurantActivity.this, MainRestaurant.class);
                                    startActivity(intent1);
                                    //send Register Token
                                  RegisterToken();

                                }
                            } catch (Exception e) {
                                e.getMessage();
                                Toast.makeText(LoginRestaurantActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        }
                        @Override
                        public void onFailure(Call<LoginResturant> call, Throwable t) {
                            Toast.makeText(LoginRestaurantActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {

                    if (loginFragmentEditUserEmail.getText().toString().isEmpty()) {
                        loginFragmentEditUserEmail.setError(getResources().getString(R.string.is_not_null));
                    }
                    if (!loginFragmentEditUserPassword.getText().toString().isEmpty()) {
                        loginFragmentEditUserPassword.setError(getResources().getString(R.string.is_not_null));
                    }
                }

            }
        });
    }

    private void getDataUserShrPreferences() {

        loginFragmentEditUserEmail.setText(LoadData(LoginRestaurantActivity.this, USER_PHONE));
        loginFragmentEditUserPassword.setText(LoadData(LoginRestaurantActivity.this, Key_password));

        // check is checkBox is Checked
        if (LoadBooleanRestaurant(LoginRestaurantActivity.this, KEY_IS_CHECK_BOX)) {
            Log.d("response", "true");
            loginFragmentChkBox.setChecked(true);
        } else {
            loginFragmentChkBox.setChecked(false);
            Log.d("response", "false");

        }
    }

    private void ClassSharedPreferences(int id, String ApiToken, String name, String email, String phone, String password) {

        SaveData(LoginRestaurantActivity.this, USER_API_TOKEN, String.valueOf(ApiToken));
        SaveData(LoginRestaurantActivity.this, USER_ID, String.valueOf(id));
        SaveData(LoginRestaurantActivity.this, USER_NAME, String.valueOf(name));
        SaveData(LoginRestaurantActivity.this, USER_EMAIL, String.valueOf(email));
        SaveData(LoginRestaurantActivity.this, USER_PHONE, String.valueOf(phone));
        SaveData(LoginRestaurantActivity.this, Key_password, String.valueOf(password));
        SaveData(LoginRestaurantActivity.this, KEY_IS_CHECK_BOX, Checked);
    }

    //send Register Token
    public void RegisterToken() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        SharedPreferences preferences = getSharedPreferences("Blood", 0);
        Log.d("USER_API_TOKEN",refreshedToken);
        Log.d("refreshedToken", refreshedToken);

        // get Register Token




        apiServerRestaurant.RegisterToken(refreshedToken, LoadData(LoginRestaurantActivity.this, USER_API_TOKEN), "android").enqueue(new Callback<NotifyToken>() {
            @Override
            public void onResponse(Call<NotifyToken> call, Response<NotifyToken> response) {

                try {
                    if (response.body().getStatus() == 1) {
                        Toast.makeText(getApplication(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplication(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            }

            @Override
            public void onFailure(Call<NotifyToken> call, Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
