package com.example.sofranewapp.ui.activity.user;

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
import android.view.View;
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
import com.example.sofranewapp.data.model.clientRegister.ClientRegister;
import com.example.sofranewapp.data.model.regions.Regions;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofranewapp.helper.HelperMathod.checkCorrespondPassword;
import static com.example.sofranewapp.helper.HelperMathod.checkLengthPassword;
import static com.example.sofranewapp.helper.HelperMathod.convertFileToMultipart;
import static com.example.sofranewapp.helper.HelperMathod.convertToRequestBody;

public class RegisterClientActivity extends Activity {

    private static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 0x3;

    Unbinder unbinder;

    ProgressBar progressBar;

    APiSofraResturant apiServerRestaurant;
    APiSofraClint serverClient;
    // value adapter city
    SpinnerAdapter spinnerAdapter;
    ArrayList<GeneratedModelSpinner> generatedModelSpinnerArrayListCity;
    GeneratedModelSpinner cityGeneratedModelSpinner;

    // value adapter Regions
    SpinnerAdapter regionsSpinnerAdapter;
    ArrayList<GeneratedModelSpinner> generatedModelSpinnerArrayListRegions;
    GeneratedModelSpinner regionsGeneratedModelSpinner;
    @BindView(R.id.registerActivityImgNewClient)
    ImageView registerActivityImgNewClient;
    @BindView(R.id.registerActivityEditNameClient)
    TextInputEditText registerActivityEditNameClient;
    @BindView(R.id.registerActivityEditEmailClient)
    TextInputEditText registerActivityEditEmailClient;
    @BindView(R.id.registerActivityEditPhoneClient)
    TextInputEditText registerActivityEditPhoneClient;
    @BindView(R.id.registerActivityCiteSpinnerClient)
    Spinner registerActivityCiteSpinnerClient;
    @BindView(R.id.registerActivityRegionsSpinnerClient)
    Spinner registerActivityRegionsSpinnerClient;
    @BindView(R.id.registerActivityEditPasswordClient)
    TextInputEditText registerActivityEditPasswordClient;
    @BindView(R.id.registerActivityEmphasisEditPasswordClient)
    TextInputEditText registerActivityEmphasisEditPasswordClient;
    @BindView(R.id.registerActivitySaveBtnClient)
    Button registerActivitySaveBtnClient;
    @BindView(R.id.progressBarClient)
    ProgressBar progressBarClient;


    private int idCity;
    private int idRegions;
    private String filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client_sofra);

        ButterKnife.bind(this);


        inti();


        getDataCity();


    }

    private void inti() {
        progressBar = findViewById(R.id.progressBarClient);
        progressBar.setVisibility(View.INVISIBLE);

        apiServerRestaurant = RetrofitSofra.getRestaurant().create(APiSofraResturant.class);
        serverClient = RetrofitSofra.getRestaurant().create(APiSofraClint.class);

        generatedModelSpinnerArrayListRegions = new ArrayList<>();
        generatedModelSpinnerArrayListCity = new ArrayList<>();
    }

    // get   data cities
    private void getDataCity() {

        apiServerRestaurant.getCities().enqueue(new Callback<Cities>() {
            @Override
            public void onResponse(Call<Cities> call, Response<Cities> response) {

                if (response.body().getStatus() == 1) {
                    progressBar.setVisibility(View.INVISIBLE);

                    // clear list
                    generatedModelSpinnerArrayListCity.clear();
                    // add new data from list
                    generatedModelSpinnerArrayListCity.add(new GeneratedModelSpinner(0
                            , getResources().getString(R.string.city)));

                    for (int i = 0; i < response.body().getData().getData().size(); i++) {

                        cityGeneratedModelSpinner = new GeneratedModelSpinner(response.body().getData().getData().get(i).getId(),
                                response.body().getData().getData().get(i).getName());

                        generatedModelSpinnerArrayListCity.add(cityGeneratedModelSpinner);
                    }

                    spinnerAdapter = new SpinnerAdapter(RegisterClientActivity.this, generatedModelSpinnerArrayListCity);
                    registerActivityCiteSpinnerClient.setAdapter(spinnerAdapter);
                    registerActivityCiteSpinnerClient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position != 0) {
                                idCity = generatedModelSpinnerArrayListCity.get(position).getId();
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
                    generatedModelSpinnerArrayListRegions.clear();
                    // add new data
                    generatedModelSpinnerArrayListRegions.add(new GeneratedModelSpinner(0, "الحي"));

                    for (int i = 0; i < response.body().getDataPagination().getData().size(); i++) {
                        regionsGeneratedModelSpinner =
                                new GeneratedModelSpinner(response.body().getDataPagination().getData().get(i).getId(),
                                        response.body().getDataPagination().getData().get(i).getName());
                        generatedModelSpinnerArrayListRegions.add(regionsGeneratedModelSpinner);
                    }

                    regionsSpinnerAdapter = new SpinnerAdapter(RegisterClientActivity.this, generatedModelSpinnerArrayListRegions);
                    registerActivityRegionsSpinnerClient.setAdapter(regionsSpinnerAdapter);
                    registerActivityRegionsSpinnerClient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position != 0) {

                                idRegions = generatedModelSpinnerArrayListRegions.get(position).getId();
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

            filePath = getRealPathFromURIPath(uri, RegisterClientActivity.this);

            // Set the Image in ImageView after decoding the String
            registerActivityImgNewClient.setImageBitmap(BitmapFactory.decodeFile(filePath)
            );

            Toast.makeText(this, "Something went wrong" + filePath, Toast.LENGTH_LONG).show();

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
            int result = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void requestPermissionForReadExtertalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions(RegisterClientActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    @OnClick({R.id.registerActivityImgNewClient, R.id.registerActivitySaveBtnClient})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.registerActivityImgNewClient:
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
            case R.id.registerActivitySaveBtnClient:

                if (checkLengthPassword(registerActivityEditPasswordClient.getText().toString())
                        && checkCorrespondPassword(registerActivityEditPasswordClient.getText().toString()
                        , registerActivityEmphasisEditPasswordClient.getText().toString())) {

                    progressBar.setVisibility(View.VISIBLE);

                    serverClient.addClientRegister(
                            convertToRequestBody(registerActivityEditNameClient.getText().toString())
                            , convertToRequestBody(registerActivityEditEmailClient.getText().toString())
                            , convertToRequestBody(registerActivityEditPasswordClient.getText().toString())
                            , convertToRequestBody(registerActivityEmphasisEditPasswordClient.getText().toString())
                            , convertToRequestBody(registerActivityEditPhoneClient.getText().toString())
                            , convertToRequestBody(".")
                            , convertToRequestBody(String.valueOf(idRegions))
                            , convertFileToMultipart(filePath, "profile_image"))
                            .enqueue(new Callback<ClientRegister>() {
                                @Override
                                public void onResponse(Call<ClientRegister> call, Response<ClientRegister> response) {

                                    Log.d("response", response.body().getMsg());

                                    Toast.makeText(RegisterClientActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                    if (response.body().getStatus() == 1) {

                                        progressBar.setVisibility(View.VISIBLE);

                                        Intent intent = new Intent(RegisterClientActivity.this, LoginClientActivity.class);

                                        startActivity(intent);


                                    } else {
                                        progressBar.setVisibility(View.INVISIBLE);

                                    }

                                }

                                @Override
                                public void onFailure(Call<ClientRegister> call, Throwable t) {

                                }
                            });
                } else {


                    if (!checkLengthPassword(registerActivityEditPasswordClient.getText().toString())) {
                        registerActivityEmphasisEditPasswordClient.setError(getResources().getString(R.string.It_should_be_the_largest6));
                    }
                    if (!checkCorrespondPassword(registerActivityEditPasswordClient.getText().toString(), registerActivityEmphasisEditPasswordClient.getText().toString())) {
                        registerActivityEmphasisEditPasswordClient.setError(getResources().getString(R.string.number_does_not_correspond));
                    }
                }
                break;
        }
    }
}
