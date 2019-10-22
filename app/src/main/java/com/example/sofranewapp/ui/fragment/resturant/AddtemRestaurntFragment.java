package com.example.sofranewapp.ui.fragment.resturant;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sofranewapp.R;
import com.example.sofranewapp.data.api.APiSofraResturant;
import com.example.sofranewapp.data.api.RetrofitSofra;
import com.example.sofranewapp.data.model.updateItem.UpdateItem;

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
import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.LoadData;
import static com.example.sofranewapp.data.local.SharedPreferncesMangerRestaurant.USER_API_TOKEN;
import static com.example.sofranewapp.helper.HelperMathod.convertFileToMultipart;
import static com.example.sofranewapp.helper.HelperMathod.convertToRequestBody;

public class AddtemRestaurntFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.addItemsRestaurantFragmentImgItem)
    ImageView addItemsRestaurantFragmentImgItem;
    @BindView(R.id.addItemsRestaurantFragmentEditNameItem)
    EditText addItemsRestaurantFragmentEditNameItem;
    @BindView(R.id.addItemsRestaurantFragmentEditDescriptionItem)
    EditText addItemsRestaurantFragmentEditDescriptionItem;
    @BindView(R.id.addItemsRestaurantFragmentEditPriceItem)
    EditText addItemsRestaurantFragmentEditPriceItem;
    @BindView(R.id.addItemsRestaurantFragmentEditTimeItem)
    EditText addItemsRestaurantFragmentEditTimeItem;

    @BindView(R.id.addItemsRestaurantFragmentBtnUpdateItems)
    Button addItemsRestaurantFragmentBtnUpdateItems;
    private static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 0x3;
    @BindView(R.id.addItemsRestaurantFragmentItemProgressBar)
    ProgressBar addItemsRestaurantFragmentItemProgressBar;
    @BindView(R.id.addItemsRestaurantFragmentEditPriceOfferItem)
    EditText addItemsRestaurantFragmentEditPriceOfferItem;

    private APiSofraResturant apiServerRestaurant;

    private View view;

    private String filePath;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_additem_restaurnt_sofra, container, false);

        unbinder = ButterKnife.bind(this, view);
        // initializer tools
        inti();


        return view;
    }

    // initializer tools
    private void inti() {
        /// get date MyItemRestaurantFragment

        apiServerRestaurant = RetrofitSofra.getRestaurant().create(APiSofraResturant.class);

        addItemsRestaurantFragmentItemProgressBar.setVisibility(View.INVISIBLE);

    }


    // get   GeneratedDataOrder cities
    private void AddItemRestaurant() {
        addItemsRestaurantFragmentItemProgressBar.setVisibility(View.VISIBLE);
//        LoadData(getActivity(),USER_API_TOKEN);

        apiServerRestaurant.NewItem(
                convertToRequestBody(addItemsRestaurantFragmentEditDescriptionItem.getText().toString()),
                convertToRequestBody(addItemsRestaurantFragmentEditPriceItem.getText().toString()),
                convertToRequestBody(addItemsRestaurantFragmentEditPriceOfferItem.getText().toString()),
                convertToRequestBody(addItemsRestaurantFragmentEditTimeItem.getText().toString()),
                convertToRequestBody(addItemsRestaurantFragmentEditNameItem.getText().toString()),
                convertToRequestBody( LoadData(getActivity(),USER_API_TOKEN))
                , convertFileToMultipart(filePath, "photo")).enqueue(new Callback<UpdateItem>() {
            @Override
            public void onResponse(Call<UpdateItem> call, Response<UpdateItem> response) {
                try {
                    Log.d("response", response.body().getMsg());

                    if (response.body().getStatus() == 1) {

                        Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_LONG).show();

                        addItemsRestaurantFragmentItemProgressBar.setVisibility(View.INVISIBLE);

                    } else {
                        Log.d("response", response.body().getMsg());
                        addItemsRestaurantFragmentItemProgressBar.setVisibility(View.INVISIBLE);

                    }
                }catch (Exception e){
                    e.getMessage();
                }

            }

            @Override
            public void onFailure(Call<UpdateItem> call, Throwable t) {
                Log.d("response", t.getMessage());
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        if (requestCode == 0 && resultCode == RESULT_OK && null != data) {

            //data.getDataCode return the content URI for the selected Image
            Uri uri = data.getData();

            filePath = getRealPathFromURIPath(uri, getActivity());

            // Set the Image in ImageView after decoding the String
            addItemsRestaurantFragmentImgItem.setImageBitmap(BitmapFactory.decodeFile(filePath)
            );

            Toast.makeText(getActivity(), "Something went wrong" + filePath, Toast.LENGTH_LONG).show();

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

    @OnClick({R.id.addItemsRestaurantFragmentImgItem, R.id.addItemsRestaurantFragmentBtnUpdateItems})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addItemsRestaurantFragmentImgItem:
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
            case R.id.addItemsRestaurantFragmentBtnUpdateItems:
                AddItemRestaurant();
                break;
        }
    }
}
