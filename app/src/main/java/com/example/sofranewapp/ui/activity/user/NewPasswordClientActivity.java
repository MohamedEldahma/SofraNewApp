package com.example.sofranewapp.ui.activity.user;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.sofranewapp.R;
import com.example.sofranewapp.data.api.APiSofraClint;
import com.example.sofranewapp.data.api.RetrofitSofra;
import com.example.sofranewapp.data.model.newPassword.NewPassword;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofranewapp.data.local.SharedPreferncesMangerClient.clean;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerClient.setSharedPreferencesClient;
import static com.example.sofranewapp.helper.HelperMathod.checkCorrespondPassword;
import static com.example.sofranewapp.helper.HelperMathod.checkLengthPassword;

public class NewPasswordClientActivity extends Activity {


    Unbinder unbinder;

    APiSofraClint apiServerClient;
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
        setSharedPreferencesClient(NewPasswordClientActivity.this);

        // class login retrofit
        ClassLoginRetrofit();


    }


    private void inti() {

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


                    if (checkCorrespondPassword(newPassword, ConfirmPassword) && checkLengthPassword(newPassword)) {

                        apiServerClient = RetrofitSofra.getRestaurant().create(APiSofraClint.class);
                        apiServerClient.newPassword( CodePin,newPassword, ConfirmPassword).enqueue(new Callback<NewPassword>() {
                            @Override
                            public void onResponse(Call<NewPassword> call, Response<NewPassword> response) {

                                try {
                                    if (response.body().getStatus() == 1) {

                                        clean(NewPasswordClientActivity.this);

                                        startActivity(new Intent(NewPasswordClientActivity.this, LoginClientActivity.class));

                                        progressBarNewPassword.setVisibility(View.INVISIBLE);

                                    } else {
                                        progressBarNewPassword.setVisibility(View.INVISIBLE);

                                    }

                                }catch (Exception e){
                                    e.getMessage();
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
        });

    }

}

