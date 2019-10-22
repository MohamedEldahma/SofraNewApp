package com.example.sofranewapp.ui.activity.resturant;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sofranewapp.R;
import com.example.sofranewapp.data.api.APiSofraResturant;
import com.example.sofranewapp.data.api.RetrofitSofra;
import com.example.sofranewapp.data.model.newPassword.NewPassword;
import com.example.sofranewapp.ui.activity.user.MainClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.clean;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.setSharedPreferencesRestaurant;
import static com.example.sofranewapp.helper.HelperMathod.checkCorrespondPassword;
import static com.example.sofranewapp.helper.HelperMathod.checkLengthPassword;

public class NewPasswordRestaurantActivity extends Activity {


    Unbinder unbinder;

    APiSofraResturant apiServerRestaurant;
    @BindView(R.id.NewPasswordFragmentEditUserNewPassword)
    EditText NewPasswordFragmentEditUserNewPassword;
    @BindView(R.id.newPasswordFragmentEditUserCodePin)
    EditText newPasswordFragmentEditUserCodePin;
    @BindView(R.id.NewPasswordFragmentEditUserConfirmPassword)
    EditText NewPasswordFragmentEditUserConfirmPassword;
    @BindView(R.id.NewPasswordFragmentBtnNewPassword)
    Button NewPasswordFragmentBtnNewPassword;
    @BindView(R.id.progressBarNewPassword)
    ProgressBar progressBarNewPassword;


    private String getPinCodeForTest;
    private String newPassword, CodePin, ConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password_sofra);
        ButterKnife.bind(this);


        inti();

        // initialize ShardPreferences
        setSharedPreferencesRestaurant(NewPasswordRestaurantActivity.this);

        // class login retrofit
        ClassLoginRetrofit();


    }


    private void inti() {
        Intent intent = getIntent();
        if (intent != null) {
            getPinCodeForTest = intent.getStringExtra("getPinCodeForTest");
        }

        progressBarNewPassword.setVisibility(View.INVISIBLE);
    }


    // class login retrofit
    private void ClassLoginRetrofit() {

        NewPasswordFragmentBtnNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarNewPassword.setVisibility(View.VISIBLE);

                newPassword = NewPasswordFragmentEditUserNewPassword.getText().toString();
                CodePin = newPasswordFragmentEditUserCodePin.getText().toString();
                ConfirmPassword = NewPasswordFragmentEditUserConfirmPassword.getText().toString();

                Toast.makeText(NewPasswordRestaurantActivity.this, getPinCodeForTest + "--" + CodePin, Toast.LENGTH_SHORT).show();

                if (getPinCodeForTest.equals(CodePin)) {

                    if (checkCorrespondPassword(newPassword, ConfirmPassword) && checkLengthPassword(newPassword)) {

                        apiServerRestaurant = RetrofitSofra.getRestaurant().create(APiSofraResturant.class);
                        apiServerRestaurant.newPassword(newPassword, ConfirmPassword, CodePin).enqueue(new Callback<NewPassword>() {
                            @Override
                            public void onResponse(Call<NewPassword> call, Response<NewPassword> response) {

                                if (response.body().getStatus() == 1) {

                                    clean(NewPasswordRestaurantActivity.this);

                                    if (getFragmentManager() != null) {
                                        startActivity(new Intent(NewPasswordRestaurantActivity.this, MainClient.class));
                                    }
                                    progressBarNewPassword.setVisibility(View.INVISIBLE);

                                } else {
                                    progressBarNewPassword.setVisibility(View.INVISIBLE);

                                }

                            }

                            @Override
                            public void onFailure(Call<NewPassword> call, Throwable t) {
                                newPasswordFragmentEditUserCodePin.setError(getResources().getString(R.string.confirmation_code_wrong));
                                progressBarNewPassword.setVisibility(View.INVISIBLE);

                            }
                        });


                    } else {
                        if (!checkCorrespondPassword(newPassword, ConfirmPassword) && !checkLengthPassword(newPassword)) {
                            NewPasswordFragmentEditUserConfirmPassword.setError(getResources().getString(R.string.confirmation_number_error));
                        }

                    }

                }
            }
        });

    }

}

