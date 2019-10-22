package com.example.sofranewapp.data.local;


import com.example.sofranewapp.data.model.Item;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ItemDAO {

    @Insert
    void insert(Item... items);

    @Update
    void update(Item... items);

    @Query("update itemsOrder set quantity = :q  where idItems = :id")
    void update(int id, int q);
      @Delete
      void delete(Item... items);


    @Query("DELETE FROM itemsOrder ")
    void deleteAll();

    @Query("DELETE FROM itemsOrder where idItems = :idItems")
    void delete(int idItems);


    @Query("SELECT * FROM itemsOrder")
    List<Item> getItems();

    @Query("SELECT * FROM itemsOrder WHERE state = '0'")
    List<Item> getItemByIdRestaurant();


    @Query("SELECT * FROM itemsOrder WHERE idItems = :idItems " )
    Item getItemByIdItems(int idItems);


}
