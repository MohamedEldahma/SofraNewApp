
package com.example.sofranewapp.data.model.addReview;

import com.example.sofranewapp.data.model.Generated.GeneratedClient;
import com.example.sofranewapp.data.model.Generated.GeneratedRestaurant;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review {

    @SerializedName("rate")
    @Expose
    private String rate;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("restaurant_id")
    @Expose
    private Integer restaurantId;
    @SerializedName("client_id")
    @Expose
    private Integer clientId;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("client")
    @Expose
    private GeneratedClient client;
    @SerializedName("restaurant")
    @Expose
    private GeneratedRestaurant restaurant;

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public GeneratedClient getClient() {
        return client;
    }

    public void setClient(GeneratedClient client) {
        this.client = client;
    }

    public GeneratedRestaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(GeneratedRestaurant restaurant) {
        this.restaurant = restaurant;
    }

}
