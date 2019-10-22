package com.example.sofranewapp.helper;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;


import com.example.sofranewapp.R;
import com.example.sofranewapp.data.api.APiSofraClint;
import com.example.sofranewapp.data.api.RetrofitSofra;
import com.example.sofranewapp.data.model.addReview.AddReview;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofranewapp.data.local.SharedPreferncesMangerClient.LoadData;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerClient.USER_API_TOKEN;
import static com.example.sofranewapp.helper.HelperMathod.dismissProgressDialog;
import static com.example.sofranewapp.helper.HelperMathod.showProgressDialog;


public class CustomDialogAddCommentClass extends Dialog {

    public Activity activity;


    @BindView(R.id.commentDialogRestaurantClientEtDetailsComment)
    EditText commentDialogRestaurantClientEtDetailsComment;

    @BindView(R.id.commentDialogRestaurantClientRatingBarComment)
    EmojiRatingBar commentDialogRestaurantClientRatingBarComment;
    private APiSofraClint apiServerClient;
    private float isRation;
    private int idRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_comment_dialog_restaurant_clientsofra);
        ButterKnife.bind(this);

        inti();

    }

    public CustomDialogAddCommentClass(Activity a, int idRestaurant) {
        super(a);
        this.activity = a;
        this.idRestaurant = idRestaurant;
    }

    private void inti() {
        apiServerClient = RetrofitSofra.getRestaurant().create(APiSofraClint.class);

        commentDialogRestaurantClientRatingBarComment.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                isRation = rating;
                ratingBar.setRating(rating);


            }
        });

        commentDialogRestaurantClientRatingBarComment.setRating(1);

    }

    //   get add Restaurant
    private void getAddRestaurant() {
        showProgressDialog(activity, "Wait..");
        apiServerClient.AddReview(isRation, commentDialogRestaurantClientEtDetailsComment.getText().toString(), idRestaurant, LoadData(activity, USER_API_TOKEN))
                .enqueue(new Callback<AddReview>() {
                    @Override
                    public void onResponse(Call<AddReview> call, Response<AddReview> response) {
                        try {
                            if (response.body().getStatus() == 1) {

                                Log.d("response", response.body().getMsg());
                                Toast.makeText(activity, response.body().getMsg() , Toast.LENGTH_SHORT).show();

                                dismissProgressDialog();
                            } else {
                                Toast.makeText(activity, response.body().getMsg() , Toast.LENGTH_SHORT).show();
                                Log.d("response", response.body().getMsg());
                                dismissProgressDialog();
                            }
                        } catch (Exception e) {
                            e.getMessage();
                            dismissProgressDialog();
                        }

                    }

                    @Override
                    public void onFailure(Call<AddReview> call, Throwable t) {
                        Log.d("response", t.getMessage());
                        dismissProgressDialog();
                    }
                });

    }

    @OnClick(R.id.commentDialogRestaurantClientBtnAddComment)
    public void onViewClicked() {
        getAddRestaurant();
        dismiss();
    }


}