package com.example.sofranewapp.ui.fragment.user;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.sofranewapp.R;
import com.example.sofranewapp.adapter.MoreAdapter;
import com.example.sofranewapp.data.api.APiSofraClint;
import com.example.sofranewapp.data.api.RetrofitSofra;
import com.example.sofranewapp.data.model.Generated.GeneratedModelMore;
import com.example.sofranewapp.helper.CustomDialogCloseClass;
import com.example.sofranewapp.ui.fragment.ContactMeRestaurantFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.sofranewapp.helper.HelperMathod.getStartFragments;

public class MoreRestaurantClientFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.fragmentMoreListViewRestaurant)
    ListView fragmentMoreListViewRestaurant;

    private View view;
    APiSofraClint apiServerClient;

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
        apiServerClient = RetrofitSofra.getRestaurant().create(APiSofraClint.class);

        generatedModelMores.clear();

        generatedModelMores.add(new GeneratedModelMore(R.drawable.icon_offer, "Offer"));
        generatedModelMores.add(new GeneratedModelMore(R.drawable.icon_connect, "Connect with us"));
        generatedModelMores.add(new GeneratedModelMore(R.drawable.icon_about, "About App"));
        generatedModelMores.add(new GeneratedModelMore(R.drawable.icon_logout, "logout"));

        MoreAdapter moreAdapter = new MoreAdapter(getContext(), generatedModelMores);
        fragmentMoreListViewRestaurant.setAdapter(moreAdapter);

        fragmentMoreListViewRestaurant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if (getFragmentManager() != null) {
                        getStartFragments(getFragmentManager(), R.id.mainClientReplaceFragment, new GetOfferRestaurantClientFragment());
                    }
                }else  if (position == 1) {
                    if (getFragmentManager() != null) {
                        getStartFragments(getFragmentManager(), R.id.mainClientReplaceFragment, new ContactMeRestaurantFragment());
                    }
                }
                else  if (position == 2) {
                    if (getFragmentManager() != null) {
                        getStartFragments(getFragmentManager(), R.id.mainClientReplaceFragment, new AboutRestaurantClientFragment());
                    }
                }
                else if (position == 3) {
                    CustomDialogCloseClass cdd=new CustomDialogCloseClass(getActivity());
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    cdd.show();
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
