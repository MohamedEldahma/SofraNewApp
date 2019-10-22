package com.example.sofranewapp.data.local;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;

public class SharedPreferncesMangerRestaurant {



    public static SharedPreferences sharedPreferences = null;
    public static String USER_API_TOKEN = "USER_API_TOKEN";
    public static String USER_ID = "USER_ID";
    public static String USER_NAME = "USER_NAME";
    public static String USER_EMAIL = "USER_EMAIL";
     public static String USER_PHONE = "USER_PHONE";
    public static String USER_DLD = "USER_DLD";
     public static String USER_CITY_ID = "USER_CITY_ID";
    public static String USER_CITY_NAME = "USER_CITY_NAME";
    public static String USER_GOVERMENT_ID = "USER_GOVERMENT_ID";
    public static String USER_GOVERMENT_NAME = "USER_GOVERMENT_NAME";
     public static String Key_password = "password";
    public static String KEY_IS_CHECK_BOX = "isCheckBox";


    public static void setSharedPreferencesRestaurant(Activity activity) {
        if (sharedPreferences == null) {
            sharedPreferences = activity.getSharedPreferences(
                    "sofraRestaurant", Context.MODE_PRIVATE);

        }
    }

//    public static void setSharedPreferencesRestaurant(Fragment fragment) {
//        if (sharedPreferences == null) {
//            sharedPreferences = fragment.getSharedPreferences(
//                    "sofraRestaurant", fragment.MODE_PRIVATE);
//        }
//    }

    public static void SaveData(Activity activity, String data_Key, String data_Value) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(data_Key, data_Value);
            editor.apply();
        } else {
            setSharedPreferencesRestaurant(activity);
        }
    }

    public static void SaveData(Activity activity, String data_Key, boolean data_Value) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(data_Key, data_Value);
            editor.apply();
        } else {
            setSharedPreferencesRestaurant(activity);
        }
    }

    public static String LoadData(Activity activity, String data_Key) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
        } else {
            setSharedPreferencesRestaurant(activity);
        }

        return sharedPreferences.getString(data_Key, null);
    }

    public static boolean LoadBooleanRestaurant(Activity activity, String data_Key) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
        } else {
            setSharedPreferencesRestaurant(activity);
        }

        return sharedPreferences.getBoolean(data_Key, false);
    }

    public static void clean(Activity activity) {
        setSharedPreferencesRestaurant(activity);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
        }
    }



}
