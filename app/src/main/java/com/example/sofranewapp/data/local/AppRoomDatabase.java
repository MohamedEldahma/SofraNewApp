package com.example.sofranewapp.data.local;


import android.content.Context;

import com.example.sofranewapp.data.model.Item;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = Item.class, version = 1, exportSchema = false)
public abstract class AppRoomDatabase extends RoomDatabase {

    private static AppRoomDatabase appDatabase;

    public static synchronized AppRoomDatabase getAppDatabase(Context context){
        if (appDatabase==null){
            appDatabase = Room.databaseBuilder(context, AppRoomDatabase.class, "dataSofra3")
                    .allowMainThreadQueries()
                    .build();
        }
     return appDatabase;
    }


    public abstract ItemDAO getItemDAO();

}
