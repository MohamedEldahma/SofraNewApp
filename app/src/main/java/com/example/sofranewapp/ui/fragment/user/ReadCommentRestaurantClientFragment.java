package com.example.sofranewapp.ui.fragment.user;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.sofranewapp.R;
import com.example.sofranewapp.adapter.CommentRestaurantClientRecyclerAdapter;
import com.example.sofranewapp.data.api.APiSofraResturant;
import com.example.sofranewapp.data.api.RetrofitSofra;
import com.example.sofranewapp.data.model.showReviews.DataComment;
import com.example.sofranewapp.data.model.showReviews.ShowReviews;
import com.example.sofranewapp.helper.CustomDialogAddCommentClass;
import com.example.sofranewapp.helper.OnEndless;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofranewapp.data.local.SharedPreferncesMangerClient.LoadData;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerClient.USER_API_TOKEN;


public class ReadCommentRestaurantClientFragment extends Fragment {

    CommentRestaurantClientRecyclerAdapter commentRestaurantClientRecyclerAdapter;
    public static List<DataComment> userArrayList;

    Unbinder unbinder;
    @BindView(R.id.fragmentCommentRestaurantClientRvComment)
    RecyclerView fragmentCommentRestaurantClientRvComment;
    @BindView(R.id.fragmentCommentRestaurantClientPbWait)
    ProgressBar fragmentCommentRestaurantClientPbWait;
    @BindView(R.id.linearLayout7)
    LinearLayout linearLayout7;

    private boolean checkFilterPost = true;

    private View view;
    private APiSofraResturant apiServerRestaurant;
    private int max = 0;

    public int current_page = 1;

    private OnEndless onEndless;
    private int idRestaurant;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_comment_restaurant_client_sofra, container, false);

        unbinder = ButterKnife.bind(this, view);
        // initializer tools
        inti();

        // this is scroll list view
        onEndless();

        return view;
    }

    // initializer tools
    @SuppressLint("RestrictedApi")
    private void inti() {
        apiServerRestaurant = RetrofitSofra.getRestaurant().create(APiSofraResturant.class);
        userArrayList = new ArrayList<>();
        // get id restaurant from return    Content Component

        Bundle arguments =getArguments();
        if (arguments != null) {
            idRestaurant = arguments.getInt("idRestaurantContentComponent");
        }
    }

    //   get Reviews Restaurant
    private void getReviewsRestaurant(int Page) {
        fragmentCommentRestaurantClientPbWait.setVisibility(View.VISIBLE);
        apiServerRestaurant.getReviewsRestaurantClient(LoadData(getActivity(), USER_API_TOKEN), idRestaurant, Page)
                .enqueue(new Callback<ShowReviews>() {
                    @Override
                    public void onResponse(Call<ShowReviews> call, Response<ShowReviews> response) {
                        try {
                            if (response.body().getStatus() == 1) {

                                max = response.body().getData().getLastPage();

                                userArrayList.addAll(response.body().getData().getData());

                                commentRestaurantClientRecyclerAdapter.notifyDataSetChanged();

                                fragmentCommentRestaurantClientPbWait.setVisibility(View.INVISIBLE);

                            } else {
                                Log.d("response", response.body().getMsg());
                                fragmentCommentRestaurantClientPbWait.setVisibility(View.INVISIBLE);
                            }
                        } catch (Exception e) {
                            e.getMessage();
                        }

                    }

                    @Override
                    public void onFailure(Call<ShowReviews> call, Throwable t) {
                        Log.d("response", t.getMessage());
                    }
                });

    }

    // listener from count items  recyclerView
    private void onEndless() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        fragmentCommentRestaurantClientRvComment.setLayoutManager(linearLayoutManager);

        onEndless = new OnEndless(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= max || max != 0 || current_page == 1) {
                    getReviewsRestaurant(current_page);
                }
            }
        };

        fragmentCommentRestaurantClientRvComment.addOnScrollListener(onEndless);
        commentRestaurantClientRecyclerAdapter = new CommentRestaurantClientRecyclerAdapter(userArrayList, getActivity());
        fragmentCommentRestaurantClientRvComment.setAdapter(commentRestaurantClientRecyclerAdapter);

        getReviewsRestaurant(1);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick(R.id.fragmentCommentRestaurantClientBtnAddComment)
    public void onViewClicked() {
        CustomDialogAddCommentClass cdd=new CustomDialogAddCommentClass(getActivity(),idRestaurant);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.show();

    }
}
