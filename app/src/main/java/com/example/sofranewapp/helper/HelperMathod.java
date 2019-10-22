package com.example.sofranewapp.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.sofranewapp.R;
import com.example.sofranewapp.ui.fragment.resturant.NotificationsRestaurantFragment;
import com.example.sofranewapp.ui.fragment.user.NotificationsClientFragment;


import java.io.File;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class HelperMathod {

    private static ProgressDialog checkDialog;


    public static void replaceFrag(Fragment fragment, FragmentManager supportFragmentManager, int id) {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.replace(id, fragment);
        transaction.addToBackStack(null);
        transaction.commit();}

    // check length password
    public static boolean checkLengthPassword(String newPassword) {
        return newPassword.length() > 6;
    }

// check Correspond password  == ConfirmPassword

    public static boolean checkCorrespondPassword(String newPassword, String ConfirmPassword) {
        return newPassword.equals(ConfirmPassword);
    }

    // get start fragment
    public static void getStartFragments(FragmentManager supportFragmentManager, int ReplaceFragment, Fragment fragment) {

        supportFragmentManager.beginTransaction().replace(ReplaceFragment, fragment).addToBackStack(null).commit();

    }

    public static MultipartBody.Part convertFileToMultipart(String pathImageFile, String Key) {
        if (pathImageFile != null) {
            File file = new File(pathImageFile);
            RequestBody reqFileselect = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part Imagebody = MultipartBody.Part.createFormData(Key, file.getName(), reqFileselect);
            return Imagebody;
        } else {
            return null;
        }
    }

    public static RequestBody convertToRequestBody(String part) {
        try {
            if (!part.equals("")) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), part);
                return requestBody;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }


    // lode image
    public static void LodeImageCircle(Context context, String url, ImageView imageView) {


        Glide.with(context).load(url).error(R.drawable.icon_default).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        // log exception
                        Log.e("TAG", "Error loading image", e);
                        return false; // important to return false so the error placeholder can be placed
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        return false;
                    }
                }).apply(RequestOptions.circleCropTransform())

                .into(imageView);
    }

    // lode image
    public static void LodeImage(Context context, String url, ImageView imageView) {


        Glide.with(context).load(url).error(R.drawable.icon_default).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        // log exception
                        Log.e("TAG", "Error loading image", e);
                        return false; // important to return false so the error placeholder can be placed
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        return false;
                    }
                }).apply(RequestOptions.centerInsideTransform().centerCrop())

                .into(imageView);
    }

//
//    public static void LodeImageCircle(Context context, String url, ImageView imageView, final ProgressBar progressBar) {
//
//        Glide.with(context).load(url).error(R.drawable.ic_menu_camera).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
//                .listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        // log exception
//                        Log.e("TAG", "Error loading image", e);
//                        progressBar.setVisibility(View.GONE);
//                        return false; // important to return false so the error placeholder can be placed
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        progressBar.setVisibility(View.GONE);
//
//                        return false;
//                    }
//                })
//                .into(imageView);
//    }

    // tool bar
    public static void ToolBarClient(final FragmentManager supportFragmentManager, final Activity activity
            , Toolbar toolbar, String string) {


        toolbar.setTitleTextColor(activity.getResources().getColor(R.color.dark));
        toolbar.setTitle(string);
        toolbar.inflateMenu(R.menu.tab_menu);

        MenuItem menuItem = toolbar.getMenu().findItem(R.id.action_notify);

        View actionView = MenuItemCompat.getActionView(menuItem);
 //        if (getNotificationsCount(activity)!=null) {
//            if (!getNotificationsCount(activity).equals("null")) {
//                textCartItemCount.setText(getNotificationsCount(activity));
//
//            }
//        }


        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStartFragments(supportFragmentManager, R.id.mainClientReplaceFragment, new NotificationsClientFragment());

            }
        });

    }

    // tool bar
    public static void ToolBarRestaurant(final FragmentManager supportFragmentManager, final Activity activity
            , Toolbar toolbar, String string) {


        toolbar.setTitleTextColor(activity.getResources().getColor(R.color.dark));
        toolbar.setTitle(string);
        toolbar.inflateMenu(R.menu.tab_menu);

        MenuItem menuItem = toolbar.getMenu().findItem(R.id.action_notify);

        View actionView = MenuItemCompat.getActionView(menuItem);
        //        if (getNotificationsCount(activity)!=null) {
//            if (!getNotificationsCount(activity).equals("null")) {
//                textCartItemCount.setText(getNotificationsCount(activity));
//
//            }
//        }


        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStartFragments(supportFragmentManager, R.id.mainRestaurantReplaceFragment, new NotificationsRestaurantFragment());

            }
        });

    }

    public static void disappearKeypad(Activity activity, View v) {
        try {
            if (v != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        } catch (Exception e) {
        }
    }

    public static void showProgressDialog(Activity activity, String title) {
        try {
            checkDialog = new ProgressDialog(activity);
            checkDialog.setMessage(title);
            checkDialog.setIndeterminate(false);
            checkDialog.setCancelable(false);
            checkDialog.show();
        } catch (Exception e) {
        }
    }

    public static void dismissProgressDialog() {
        try {
            if (checkDialog != null && checkDialog.isShowing()) {
                checkDialog.dismiss();
            }
        } catch (Exception e) {
        }
    }

//
//    // get Notifications Count
//    public static String getNotificationsCount(final Activity activity) {
//        try {
//            ApiServerRestaurant apiServer = getRestaurant().create(ApiServerRestaurant.class);
//            // get  PaginationData  post
//            apiServer.getNotificationsCount(LoadData(activity, USER_API_TOKEN)).enqueue(new Callback<NotificationsCount>() {
//
//                @Override
//                public void onResponse(Call<NotificationsCount> call, Response<NotificationsCount> response) {
//
////                    Log.d(" notifications 5", response.body().getMsg());
//
//                    try {
//                        if (response.body().getStatus() == 1) {
//                            getNotificationsCount = String.valueOf(response.body().getDataPagination().getNotificationsCount());
//
//                        } else {
//                            Toast.makeText(activity, "Not PaginationData ", Toast.LENGTH_SHORT).show();
//                        }
//                    }catch (Exception e){
//                        e.getMessage();
//                    }
//
//                }
//
//                @Override
//                public void onFailure(Call<NotificationsCount> call, Throwable t) {
//                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
//
//                }
//            });
//
//        } catch (Exception e) {
//            e.getMessage();
//        }
//        return getNotificationsCount;
//    }



}
