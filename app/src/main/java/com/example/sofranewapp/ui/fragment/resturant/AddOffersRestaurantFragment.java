package com.example.sofranewapp.ui.fragment.resturant;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sofranewapp.R;
import com.example.sofranewapp.data.api.APiSofraResturant;
import com.example.sofranewapp.data.api.RetrofitSofra;
import com.example.sofranewapp.data.model.addOffer.AddOffer;

import java.text.DecimalFormat;
import java.util.Calendar;

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

public class AddOffersRestaurantFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.product_name)
    TextView productName;
    @BindView(R.id.addOffersRestaurantFragmentImgItem)
    ImageView addOffersRestaurantFragmentImgItem;
    @BindView(R.id.addOffersRestaurantFragmentEditNameItem)
    EditText addOffersRestaurantFragmentEditNameItem;
    @BindView(R.id.addOffersRestaurantFragmentEditDescriptionItem)
    EditText addOffersRestaurantFragmentEditDescriptionItem;
    @BindView(R.id.addOffersRestaurantFragmentEditFromOfferItem)
    EditText addOffersRestaurantFragmentEditFromOfferItem;
    @BindView(R.id.addOffersRestaurantFragmentEditToItem)
    EditText addOffersRestaurantFragmentEditToItem;
    @BindView(R.id.addOffersRestaurantFragmentBtnUpdateItems)
    Button addOffersRestaurantFragmentBtnUpdateItems;
    @BindView(R.id.addOffersRestaurantFragmentItemProgressBar)
    ProgressBar addOffersRestaurantFragmentItemProgressBar;

    private static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 0x3;
    private int YEAR;
    private int MONTH;
    private int DAY_OF_MONTH;
    private Calendar calendar;
    private String DataOfBirth, DataLastDonation;
    private APiSofraResturant apiServerRestaurant;

    private View view;

    private String filePath;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add_offers_restaurnt_sofra, container, false);

        unbinder = ButterKnife.bind(this, view);
        // initializer tools
        inti();

        decimalFormatCalendar();
        return view;
    }

    // initializer tools
    private void inti() {
        /// get date MyItemRestaurantFragment
        apiServerRestaurant = RetrofitSofra.getRestaurant().create(APiSofraResturant.class);

        addOffersRestaurantFragmentItemProgressBar.setVisibility(View.INVISIBLE);
    }


    // get   GeneratedDataOrder cities
    private void AddOffersRestaurant() {
        addOffersRestaurantFragmentItemProgressBar.setVisibility(View.VISIBLE);
//        LoadData(getActivity(),USER_API_TOKEN);

        apiServerRestaurant.newOffer(
                convertToRequestBody(addOffersRestaurantFragmentEditDescriptionItem.getText().toString()),
                convertToRequestBody("0"),
                convertToRequestBody(addOffersRestaurantFragmentEditFromOfferItem.getText().toString()),
                convertToRequestBody(addOffersRestaurantFragmentEditNameItem.getText().toString()),
                convertFileToMultipart(filePath, "photo"),
                convertToRequestBody(addOffersRestaurantFragmentEditToItem.getText().toString()),
                convertToRequestBody(LoadData(getActivity(), USER_API_TOKEN)),
                convertToRequestBody("0")
               ).enqueue(new Callback<AddOffer>() {
            @Override
            public void onResponse(Call<AddOffer> call, Response<AddOffer> response) {
                try {
                    Log.d("response", response.body().getMsg());
                    Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_LONG).show();

                    if (response.body().getStatus() == 1) {

                        Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_LONG).show();

                        addOffersRestaurantFragmentItemProgressBar.setVisibility(View.INVISIBLE);

                    } else {
                        Log.d("response", response.body().getMsg());
                        addOffersRestaurantFragmentItemProgressBar.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }

            }

            @Override
            public void onFailure(Call<AddOffer> call, Throwable t) {
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
            addOffersRestaurantFragmentImgItem.setImageBitmap(BitmapFactory.decodeFile(filePath)
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

    @OnClick({R.id.addOffersRestaurantFragmentImgItem, R.id.addOffersRestaurantFragmentBtnUpdateItems})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addOffersRestaurantFragmentImgItem:
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
            case R.id.addOffersRestaurantFragmentBtnUpdateItems:
                AddOffersRestaurant();
                break;
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    public void decimalFormatCalendar() {

        DecimalFormat decimalFormat = new DecimalFormat("00");
        calendar = Calendar.getInstance();
        YEAR = Integer.parseInt(decimalFormat.format(calendar.get(Calendar.YEAR)));
        MONTH = Integer.parseInt(decimalFormat.format(calendar.get(Calendar.MONTH)+1));
        DAY_OF_MONTH = Integer.parseInt(decimalFormat.format(calendar.get(Calendar.DAY_OF_MONTH)));

//        DataOfBirth = "1972" + "-" + "01" + "-" + "01";
//        addOffersRestaurantFragmentEditToItem.setText(DataOfBirth);
        addOffersRestaurantFragmentEditToItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @SuppressLint("DefaultLocale")
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            DataOfBirth = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", dayOfMonth);
                            addOffersRestaurantFragmentEditToItem.setText(DataOfBirth);
                        }
                    }, YEAR, MONTH, DAY_OF_MONTH);

                    datePickerDialog.show();
                    return true;
                }
                return false;
            }
        });

        addOffersRestaurantFragmentEditFromOfferItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                     DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @SuppressLint("DefaultLocale")
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            DataLastDonation = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", dayOfMonth);
                            addOffersRestaurantFragmentEditFromOfferItem.setText(DataLastDonation);
                        }
                    }, YEAR, MONTH, DAY_OF_MONTH-1);

                    datePickerDialog.show();

                    return true;
                }
                return false;
            }
        });

    }
}
