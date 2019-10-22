package com.example.sofranewapp.data.api;

import com.example.sofranewapp.data.model.addReview.AddReview;
import com.example.sofranewapp.data.model.clientRegister.ClientRegister;
import com.example.sofranewapp.data.model.getOffersClient.GetOffersClient;
import com.example.sofranewapp.data.model.loginClient.ClientLogin;
import com.example.sofranewapp.data.model.myOrders.MyOrders;
import com.example.sofranewapp.data.model.newOrder.NewOrder;
import com.example.sofranewapp.data.model.newPassword.NewPassword;
import com.example.sofranewapp.data.model.notifications.Notifications;
import com.example.sofranewapp.data.model.notifyToken.NotifyToken;
import com.example.sofranewapp.data.model.rejectOrder.RejectOrder;
import com.example.sofranewapp.data.model.resetPassword.ResetPassword;
import com.example.sofranewapp.data.model.showOrder.ShowOrder;
import com.example.sofranewapp.data.model.updateProfileClient.ProfileClient;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface APiSofraClint {
    @POST("client/login")
    @FormUrlEncoded
    Call<ClientLogin> onLogin(@Field("email") String email
            , @Field("password") String password);

    @Multipart
    @POST("client/sign-up")
    Call<ClientRegister> addClientRegister(@Part("name") RequestBody name, @Part("email") RequestBody email
            , @Part("password") RequestBody password, @Part("password_confirmation") RequestBody password_confirmation
            , @Part("phone") RequestBody phone, @Part("address") RequestBody address, @Part("region_id") RequestBody region_id
            , @Part MultipartBody.Part image);


    @Multipart
    @POST("client/profile")
    Call<ProfileClient> editProfileClient(@Part("api_token") RequestBody api_token, @Part("name") RequestBody name, @Part("email") RequestBody email
            , @Part("password") RequestBody password, @Part("password_confirmation") RequestBody password_confirmation
            , @Part("phone") RequestBody phone, @Part("address") RequestBody address, @Part("region_id") RequestBody region_id
            , @Part MultipartBody.Part image);

    @POST("client/profile")
    @FormUrlEncoded
    Call<ProfileClient> getProfileClient(@Field("api_token") String api_token );

    @POST("client/reset-password")
    @FormUrlEncoded
    Call<ResetPassword> resetPasswordClient(@Field("email") String email);

    @POST("client/new-password")
    @FormUrlEncoded
    Call<NewPassword> newPassword(@Field("code") String pin_code, @Field("password") String password, @Field("password_confirmation") String password_confirmation
    );


    @POST("client/restaurant/review")
    @FormUrlEncoded
    Call<AddReview>AddReview(@Field("rate") float rate
            , @Field("comment") String comment   , @Field("restaurant_id") int restaurant_id
            , @Field("api_token") String api_token);


    @GET("offers")
    Call<GetOffersClient> getOffers(@Query("page") int page);


    @POST("client/new-order")
    @FormUrlEncoded
    Call<NewOrder>newOrder(
            @Field("api_token") String api_token , @Field("restaurant_id") int restaurant_id
            , @Field("note") String note  , @Field("address") String address
            , @Field("payment_method_id") int payment_method_id , @Field("phone") String phone, @Field("name") String name
            , @Field("items[]") List<Integer> items
            , @Field("quantities[]")  List<Integer> quantities, @Field("notes[]")  List<String>  notes );



    @GET("client/my-orders")
    Call<MyOrders> getMyOrders(@Query("api_token") String api_token, @Query("state") String state, @Query("page") int page);

    @POST("client/decline-order")
    @FormUrlEncoded
    Call<RejectOrder> rejectOrder(@Field("api_token") String api_token, @Field("order_id") int order_id);


    @POST("client/confirm-order")
    @FormUrlEncoded
    Call<RejectOrder> confirmOrder(@Field("api_token") String api_token, @Field("order_id") int order_id);



    @GET("client/show-order")
    Call<ShowOrder> myShowOrder(@Query("api_token") String api_token, @Query("order_id") int order_id);


    @POST("client/register-token")
    @FormUrlEncoded
    Call<NotifyToken>RegisterToken(@Field("token") String token
            , @Field("api_token") String api_token, @Field("type") String type);

    @POST("client/remove-token")
    @FormUrlEncoded
    Call<NotifyToken>RemoveToken(@Field("token") String token
            , @Field("api_token") String api_token);



    @GET("client/notifications")
    Call<Notifications> getNotifications(@Query("api_token") String api_token, @Query("page") int page);
}
