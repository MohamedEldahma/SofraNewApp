package com.example.sofranewapp.ui.fragment.resturant;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sofranewapp.R;
import com.example.sofranewapp.adapter.MoreAdapter;
import com.example.sofranewapp.data.api.APiSofraResturant;
import com.example.sofranewapp.data.api.RetrofitSofra;
import com.example.sofranewapp.data.model.Generated.GeneratedModelMore;
import com.example.sofranewapp.data.model.notifyToken.NotifyToken;
import com.example.sofranewapp.ui.activity.MainHomeActivity;
import com.example.sofranewapp.ui.fragment.ContactMeRestaurantFragment;
import com.example.sofranewapp.ui.fragment.user.AboutRestaurantClientFragment;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.LoadData;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.USER_API_TOKEN;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.clean;
import static com.example.sofranewapp.helper.HelperMathod.getStartFragments;

public class MoreRestaurantFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.fragmentMoreListViewRestaurant)
    ListView fragmentMoreListViewRestaurant;

    private View view;
    APiSofraResturant apiServerRestaurant;

    List<GeneratedModelMore> generatedModelMores = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_more_restaurant_sofra, container, false);

        unbinder = ButterKnife.bind(this, view);
        // initializer tools
        inti();


        return view;
    }

    // initializer tools
    private void inti() {
        apiServerRestaurant = RetrofitSofra.getRestaurant().create(APiSofraResturant.class);

        generatedModelMores.add(new GeneratedModelMore(R.drawable.icon_offer, "Offer"));
        generatedModelMores.add(new GeneratedModelMore(R.drawable.icon_connect, "Connect with us"));
        generatedModelMores.add(new GeneratedModelMore(R.drawable.icon_about, "About App"));
        generatedModelMores.add(new GeneratedModelMore(R.drawable.icon_rate, "Rate And comments"));
        generatedModelMores.add(new GeneratedModelMore(R.drawable.icon_logout, "logout"));

        MoreAdapter moreAdapter = new MoreAdapter(getContext(), generatedModelMores);
        fragmentMoreListViewRestaurant.setAdapter(moreAdapter);

        fragmentMoreListViewRestaurant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if (getFragmentManager() != null) {
                        getStartFragments(getFragmentManager(), R.id.mainRestaurantReplaceFragment, new OfferRestaurantFragment());
                    }
                }
                else  if (position == 1) {
                    if (getFragmentManager() != null) {
                        getStartFragments(getFragmentManager(), R.id.mainRestaurantReplaceFragment, new ContactMeRestaurantFragment());
                    }
                }
                else  if (position == 2) {
                    if (getFragmentManager() != null) {
                        getStartFragments(getFragmentManager(), R.id.mainRestaurantReplaceFragment, new AboutRestaurantClientFragment());
                    }
                }else if (position == 4) {
                    clean(getActivity());
                    RemoveToken();
                    startActivity(new Intent(getActivity(), MainHomeActivity.class));
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    //send Remove Token
    public void RemoveToken() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        // Remove Token
        apiServerRestaurant.RemoveToken(refreshedToken,
                LoadData(getActivity(), USER_API_TOKEN)).enqueue(new Callback<NotifyToken>() {
            @Override
            public void onResponse(Call<NotifyToken> call, Response<NotifyToken> response) {

                try {
                    if (response.body().getStatus() == 1) {
                        Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    } else {
                        //Toast.makeText(getApplication(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.getMessage();
                }

            }

            @Override
            public void onFailure(Call<NotifyToken> call, Throwable t) {

            }
        });

    }
}
