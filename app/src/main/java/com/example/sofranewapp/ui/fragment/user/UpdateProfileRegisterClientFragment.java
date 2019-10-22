package com.example.sofranewapp.ui.fragment.user;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sofranewapp.R;
import com.example.sofranewapp.adapter.SpinnerAdapter;
import com.example.sofranewapp.data.api.APiSofraClint;
import com.example.sofranewapp.data.api.APiSofraResturant;
import com.example.sofranewapp.data.api.RetrofitSofra;
import com.example.sofranewapp.data.model.Generated.GeneratedModelSpinner;
import com.example.sofranewapp.data.model.cities.Cities;
import com.example.sofranewapp.data.model.regions.Regions;
import com.example.sofranewapp.data.model.updateProfileClient.ProfileClient;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static android.app.Activity.RESULT_OK;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerClient.Key_password;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerClient.LoadData;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerClient.USER_API_TOKEN;
import static com.example.sofranewapp.helper.HelperMathod.LodeImageCircle;
import static com.example.sofranewapp.helper.HelperMathod.checkCorrespondPassword;
import static com.example.sofranewapp.helper.HelperMathod.checkLengthPassword;
import static com.example.sofranewapp.helper.HelperMathod.convertFileToMultipart;
import static com.example.sofranewapp.helper.HelperMathod.convertToRequestBody;

public class UpdateProfileRegisterClientFragment extends Fragment {

    private static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 0x3;

    Unbinder unbinder;

    ProgressBar progressBar;

    APiSofraResturant apiServerRestaurant;
    APiSofraClint serverClient;
    // value adapter city
    SpinnerAdapter spinnerAdapter;
    ArrayList<GeneratedModelSpinner> ArrayListSpinnerCity;
    GeneratedModelSpinner cityGeneratedModelSpinner;

    // value adapter Regions
    SpinnerAdapter regionsSpinnerAdapter;
    ArrayList<GeneratedModelSpinner> ArrayListSpinnerRegions;
    GeneratedModelSpinner regionsGeneratedModelSpinner;
    @BindView(R.id.updateProfileFragmentImgNewClient)
    ImageView updateProfileFragmentImgNewClient;
    @BindView(R.id.updateProfileFragmentEditNameClient)
    TextInputEditText updateProfileFragmentEditNameClient;
    @BindView(R.id.updateProfileFragmentEditEmailClient)
    TextInputEditText updateProfileFragmentEditEmailClient;
    @BindView(R.id.updateProfileFragmentEditPhoneClient)
    TextInputEditText updateProfileFragmentEditPhoneClient;
    @BindView(R.id.updateProfileFragmentCiteSpinnerClient)
    Spinner updateProfileFragmentCiteSpinnerClient;
    @BindView(R.id.updateProfileFragmentRegionsSpinnerClient)
    Spinner updateProfileFragmentRegionsSpinnerClient;
    @BindView(R.id.updateProfileFragmentEditPasswordClient)
    TextInputEditText updateProfileFragmentEditPasswordClient;
    @BindView(R.id.updateProfileFragmentEmphasisEditPasswordClient)
    TextInputEditText updateProfileFragmentEmphasisEditPasswordClient;
    @BindView(R.id.updateProfileFragmentSaveBtnClient)
    Button updateProfileFragmentSaveBtnClient;
    @BindView(R.id.progressBarClient)
    ProgressBar progressBarClient;


    private int idCity;
    private int idRegions;
    private String filePath;
    private View view;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_update_profile_register_client_sofra, container, false);

        unbinder = ButterKnife.bind(this, view);
        // initializer tools
        inti();


        getDataCity();
        return view;
    }

    private void inti() {
        progressBar = view.findViewById(R.id.progressBarClient);
        progressBar.setVisibility(View.INVISIBLE);

        apiServerRestaurant = RetrofitSofra.getRestaurant().create(APiSofraResturant.class);
        serverClient = RetrofitSofra.getRestaurant().create(APiSofraClint.class);

        ArrayListSpinnerRegions = new ArrayList<>();
        ArrayListSpinnerCity = new ArrayList<>();

        getProfileClient();
    }


    private void getProfileClient() {

        progressBarClient.setVisibility(View.VISIBLE);
//        LoadData(getActivity(),USER_API_TOKEN);

        serverClient.getProfileClient(LoadData(getActivity(),USER_API_TOKEN)).enqueue(new Callback<ProfileClient>() {
            @Override
            public void onResponse(Call<ProfileClient> call, Response<ProfileClient> response) {
                try {
                    Log.d("response", response.body().getMsg());

                    if (response.body().getStatus() == 1) {

                        Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_LONG).show();

                        LodeImageCircle(getContext(), response.body().getData().getUser().getPhotoUrl(), updateProfileFragmentImgNewClient);

                        updateProfileFragmentEditNameClient.setText(response.body().getData().getUser().getName());
                        updateProfileFragmentEditEmailClient.setText(response.body().getData().getUser().getEmail());
                        updateProfileFragmentEditPhoneClient.setText(response.body().getData().getUser().getPhone());
                        updateProfileFragmentEditPasswordClient.setText(LoadData(getActivity(),Key_password));
                        updateProfileFragmentEmphasisEditPasswordClient.setText(LoadData(getActivity(),Key_password));


                        for (int i = 0; i < ArrayListSpinnerCity.size(); i++) {
                            if (ArrayListSpinnerCity.get(i).getId()== Integer.parseInt(response.body().getData().getUser().getRegion().getCityId())){
                                updateProfileFragmentCiteSpinnerClient.setSelection(i);
                            }
                        }

                        for (int i = 0; i < ArrayListSpinnerRegions.size(); i++) {
                            if (ArrayListSpinnerRegions.get(i).getId()== Integer.parseInt(response.body().getData().getUser().getRegionId())){
                                updateProfileFragmentRegionsSpinnerClient.setSelection(i);
                            }
                        }


                        progressBarClient.setVisibility(View.INVISIBLE);

                    } else {
                        Log.d("response", response.body().getMsg());
                        progressBarClient.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            }

            @Override
            public void onFailure(Call<ProfileClient> call, Throwable t) {
                Log.d("response", t.getMessage());
            }
        });
    }


    // get   data cities
    private void getDataCity() {

        apiServerRestaurant.getCities().enqueue(new Callback<Cities>() {
            @Override
            public void onResponse(Call<Cities> call, Response<Cities> response) {

                if (response.body().getStatus() == 1) {
                    progressBar.setVisibility(View.INVISIBLE);

                    // clear list
                    ArrayListSpinnerCity.clear();
                    // add new data from list
                    ArrayListSpinnerCity.add(new GeneratedModelSpinner(0
                            , getResources().getString(R.string.city)));

                    for (int i = 0; i < response.body().getData().getData().size(); i++) {

                        cityGeneratedModelSpinner = new GeneratedModelSpinner(response.body().getData().getData().get(i).getId(),
                                response.body().getData().getData().get(i).getName());

                        ArrayListSpinnerCity.add(cityGeneratedModelSpinner);
                    }

                    spinnerAdapter = new SpinnerAdapter(getContext(), ArrayListSpinnerCity);
                    updateProfileFragmentCiteSpinnerClient.setAdapter(spinnerAdapter);
                    updateProfileFragmentCiteSpinnerClient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position != 0) {
                                idCity = ArrayListSpinnerCity.get(position).getId();
                                getDataGetRegions(idCity);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }


            @Override
            public void onFailure(Call<Cities> call, Throwable t) {

            }
        });
    }

    public void getDataGetRegions(int idCity) {
        progressBar.setVisibility(View.VISIBLE);
        // get  PaginationData getRegions
        apiServerRestaurant.getRegions(idCity).enqueue(new Callback<Regions>() {
            @Override
            public void onResponse(Call<Regions> call, Response<Regions> response) {
                Log.d("response", response.body().getMsg());

                if (response.body().getStatus() == 1) {
                    progressBar.setVisibility(View.INVISIBLE);

                    // clear list
                    ArrayListSpinnerRegions.clear();
                    // add new data
                    ArrayListSpinnerRegions.add(new GeneratedModelSpinner(0, "الحي"));

                    for (int i = 0; i < response.body().getDataPagination().getData().size(); i++) {
                        regionsGeneratedModelSpinner =
                                new GeneratedModelSpinner(response.body().getDataPagination().getData().get(i).getId(),
                                        response.body().getDataPagination().getData().get(i).getName());
                        ArrayListSpinnerRegions.add(regionsGeneratedModelSpinner);
                    }

                    regionsSpinnerAdapter = new SpinnerAdapter(getContext(), ArrayListSpinnerRegions);
                    updateProfileFragmentRegionsSpinnerClient.setAdapter(regionsSpinnerAdapter);
                    updateProfileFragmentRegionsSpinnerClient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position != 0) {

                                idRegions = ArrayListSpinnerRegions.get(position).getId();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else {
                    Log.d("response", response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<Regions> call, Throwable t) {
                Log.d("response", t.getMessage());
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        if (requestCode == 0 && resultCode == RESULT_OK && null != data) {

            //data.getDataCode return the content URI for the selected Image
            Uri uri = data.getData();

            filePath = getRealPathFromURIPath(uri, getActivity());

            // Set the Image in ImageView after decoding the String
            updateProfileFragmentImgNewClient.setImageBitmap(BitmapFactory.decodeFile(filePath)
            );

            Toast.makeText(getContext(), "Something went wrong" + filePath, Toast.LENGTH_LONG).show();

        }

    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    public boolean checkPermissionForReadExtertalStorage() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void requestPermissionForReadExtertalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    @OnClick({R.id.updateProfileFragmentImgNewClient, R.id.updateProfileFragmentSaveBtnClient})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.updateProfileFragmentImgNewClient:
                if (checkPermissionForReadExtertalStorage()) {

                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, 0);

                } else {
                    try {
                        requestPermissionForReadExtertalStorage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.updateProfileFragmentSaveBtnClient:

                if (checkLengthPassword(updateProfileFragmentEditPasswordClient.getText().toString())
                        && checkCorrespondPassword(updateProfileFragmentEditPasswordClient.getText().toString()
                        , updateProfileFragmentEmphasisEditPasswordClient.getText().toString())) {

                    progressBar.setVisibility(View.VISIBLE);

                    serverClient.editProfileClient( convertToRequestBody(LoadData(getActivity(), USER_API_TOKEN)),
                            convertToRequestBody(updateProfileFragmentEditNameClient.getText().toString())
                            , convertToRequestBody(updateProfileFragmentEditEmailClient.getText().toString())
                            , convertToRequestBody(updateProfileFragmentEditPasswordClient.getText().toString())
                            , convertToRequestBody(updateProfileFragmentEmphasisEditPasswordClient.getText().toString())
                            , convertToRequestBody(updateProfileFragmentEditPhoneClient.getText().toString())
                            , convertToRequestBody(".")
                            , convertToRequestBody(String.valueOf(idRegions))
                            , convertFileToMultipart(filePath, "profile_image"))
                            .enqueue(new Callback<ProfileClient>() {
                                @Override
                                public void onResponse(Call<ProfileClient> call, Response<ProfileClient> response) {

                                    try {
                                        Log.d("response", response.body().getMsg());

                                        Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                        if (response.body().getStatus() == 1) {

                                            progressBar.setVisibility(View.VISIBLE);

                                            getActivity().onBackPressed();
                                        } else {
                                            progressBar.setVisibility(View.INVISIBLE);

                                        }
                                    }catch (Exception e){
                                        e.getMessage();
                                    }

                                }

                                @Override
                                public void onFailure(Call<ProfileClient> call, Throwable t) {

                                }
                            });
                } else {


                    if (!checkLengthPassword(updateProfileFragmentEditPasswordClient.getText().toString())) {
                        updateProfileFragmentEmphasisEditPasswordClient.setError(getResources().getString(R.string.It_should_be_the_largest6));
                    }
                    if (!checkCorrespondPassword(updateProfileFragmentEditPasswordClient.getText().toString(), updateProfileFragmentEmphasisEditPasswordClient.getText().toString())) {
                        updateProfileFragmentEmphasisEditPasswordClient.setError(getResources().getString(R.string.number_does_not_correspond));
                    }
                }
                break;
        }
    }
}
