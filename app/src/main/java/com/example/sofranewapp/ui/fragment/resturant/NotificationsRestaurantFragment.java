package com.example.sofranewapp.ui.fragment.resturant;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sofranewapp.R;
import com.example.sofranewapp.adapter.NotificationRecyclerAdapter;
import com.example.sofranewapp.data.api.APiSofraResturant;
import com.example.sofranewapp.data.api.RetrofitSofra;
import com.example.sofranewapp.data.model.notifications.DataNotify;
import com.example.sofranewapp.data.model.notifications.Notifications;
import com.example.sofranewapp.helper.OnEndless;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.LoadData;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.USER_API_TOKEN;


public class NotificationsRestaurantFragment extends Fragment {

    @BindView(R.id.notificationsFragmentShowPostRecyclerView)
    RecyclerView notificationsFragmentShowPostRecyclerView;
    ProgressBar notificationsFragmentFavouriteFragmentProgBar;
    Unbinder unbinder;

    private NotificationRecyclerAdapter notificationAdapterRecycler;

    private APiSofraResturant apiServer;
    private View view;
    private ArrayList<DataNotify> notificationsArrayList;
    private OnEndless onEndless;
    private Integer max;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_notifications_sofra, container, false);

        unbinder = ButterKnife.bind(this, view);

        inti();

        onEndless();



        return view;

    }

    // initialize tools
    private void inti() {
        notificationsFragmentFavouriteFragmentProgBar = view.findViewById(R.id.notificationsFragmentFavouriteFragmentProgBar);
        notificationsFragmentFavouriteFragmentProgBar.setVisibility(View.INVISIBLE);

        notificationsArrayList = new ArrayList<>();
        apiServer = RetrofitSofra.getRestaurant().create(APiSofraResturant.class);



    }

    // listener from count items  recyclerView
    private void onEndless() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        notificationsFragmentShowPostRecyclerView.setLayoutManager(linearLayoutManager);

        onEndless = new OnEndless(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= max || max != 0 || current_page == 1) {

                    getNotifications(current_page);

                }
            }
        };

        notificationsFragmentShowPostRecyclerView.addOnScrollListener(onEndless);
        notificationAdapterRecycler = new NotificationRecyclerAdapter(notificationsArrayList, getActivity());
        notificationsFragmentShowPostRecyclerView.setAdapter(notificationAdapterRecycler);

        getNotifications(1);

    }

    // get all  post
    private void getNotifications(int i) {
        try {
            // get  PaginationData  post
            apiServer.getNotifications(LoadData(getActivity(), USER_API_TOKEN),i).enqueue(new Callback<Notifications>() {
                @Override
                public void onResponse(Call<Notifications> call, Response<Notifications> response) {
                    try {
                        notificationsFragmentFavouriteFragmentProgBar.setVisibility(View.VISIBLE);

                        if (response.body().getStatus() == 1) {

                            max = response.body().getData().getLastPage();

                            notificationsArrayList.addAll(response.body().getData().getData());

                            notificationAdapterRecycler.notifyDataSetChanged();

                            notificationsFragmentFavouriteFragmentProgBar.setVisibility(View.INVISIBLE);

                        } else {

                            notificationsFragmentFavouriteFragmentProgBar.setVisibility(View.INVISIBLE);

                            Toast.makeText(getContext(), "Not PaginationData ", Toast.LENGTH_SHORT).show();

                        }

                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

                @Override
                public void onFailure(Call<Notifications> call, Throwable t) {
                    Log.d("Throwable", t.getMessage());

                }
            });

        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
