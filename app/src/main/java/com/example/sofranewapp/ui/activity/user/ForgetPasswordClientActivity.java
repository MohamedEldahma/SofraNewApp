package com.example.sofranewapp.ui.activity.user;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.example.sofranewapp.R;
import com.example.sofranewapp.data.api.APiSofraClint;
import com.example.sofranewapp.data.api.RetrofitSofra;
import com.example.sofranewapp.data.model.resetPassword.ResetPassword;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofranewapp.data.local.SharedPreferncesMangerClient.setSharedPreferencesClient;
import static com.example.sofranewapp.helper.HelperMathod.dismissProgressDialog;
import static com.example.sofranewapp.helper.HelperMathod.showProgressDialog;


public class ForgetPasswordClientActivity extends Activity {
    @BindView(R.id.forgetPasswordActivityEditUserEmail)
    EditText forgetPasswordActivityEditUserEmail;

    @BindView(R.id.forgetPasswordActivityBtnSend)
    Button forgetPasswordActivityBtnSend;

    Unbinder unbinder;

    APiSofraClint apiServerClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_sofra);
        ButterKnife.bind(this);


        // initialize ShardPreferences
        setSharedPreferencesClient(ForgetPasswordClientActivity.this);

        // class login retrofit
        ForgetPassword();

    }


    // class login retrofit
    private void ForgetPassword() {

        forgetPasswordActivityBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgressDialog(ForgetPasswordClientActivity.this,"please wait");

                apiServerClient = RetrofitSofra.getRestaurant().create(APiSofraClint.class);
                apiServerClient.resetPasswordClient(forgetPasswordActivityEditUserEmail.getText().toString()).enqueue(new Callback<ResetPassword>() {
                    @Override
                    public void onResponse(Call<ResetPassword> call, Response<ResetPassword> response) {
                        dismissProgressDialog();

                        if (response.body().getStatus() == 1) {

                            Intent intent = new Intent(
                                    ForgetPasswordClientActivity.this, NewPasswordClientActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResetPassword> call, Throwable t) {

                    }
                });
            }
        });
    }

}
