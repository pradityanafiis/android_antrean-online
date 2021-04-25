package com.praditya.antreanonline.api;

import com.praditya.antreanonline.api.response.MultipleResponses;
import com.praditya.antreanonline.api.response.SingleResponse;
import com.praditya.antreanonline.model.BusinessHour;
import com.praditya.antreanonline.model.Category;
import com.praditya.antreanonline.model.EstimatedQueue;
import com.praditya.antreanonline.model.Merchant;
import com.praditya.antreanonline.model.Queue;
import com.praditya.antreanonline.model.Service;
import com.praditya.antreanonline.model.User;


import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface Services {

    /* SERVICES FOR AUTHENTICATION */
    @POST("auth/register")
    Call<SingleResponse<User>> register(
        @Body User user
    );

    @FormUrlEncoded
    @POST("auth/login")
    Call<SingleResponse<User>> login(
        @Field("email") String email,
        @Field("password") String password,
        @Field("firebase_token") String firebaseToken
    );

    @GET("auth/logout")
    Call<SingleResponse<User>> logout(
        @Header("Authorization") String token
    );

    /* SERVICES FOR USER */
    @POST("user/update-profile")
    Call<SingleResponse<User>> updateUser(
        @Header("Authorization") String token,
        @Body User user
    );

    @FormUrlEncoded
    @PUT("user/change-password")
    Call<SingleResponse<User>> changePassword(
        @Header("Authorization") String token,
        @Field("current_password") String currentPassword,
        @Field("new_password") String newPassword
    );

    /* SERVICES FOR MERCHANT */
    @FormUrlEncoded
    @POST("merchant/find-by-name")
    Call<MultipleResponses<Merchant>> findMerchantByName(
        @Header("Authorization") String token,
        @Field("name") String name
    );

    @FormUrlEncoded
    @POST("merchant/find-by-category")
    Call<MultipleResponses<Merchant>> findMerchantByCategory(
        @Header("Authorization") String token,
        @Field("category") String category
    );

    @GET("merchant/find-by-user")
    Call<SingleResponse<Merchant>> findMerchantByUser(
        @Header("Authorization") String token
    );

    @FormUrlEncoded
    @POST("merchant/find-by-id")
    Call<SingleResponse<Merchant>> findMerchantById(
        @Header("Authorization") String token,
        @Field("id") int merchantId
    );

    @POST("merchant/store")
    Call<SingleResponse<Merchant>> createMerchant(
        @Header("Authorization") String token,
        @Body Merchant merchant
    );

    @PUT("merchant/update")
    Call<SingleResponse<Merchant>> updateMerchant(
        @Header("Authorization") String token,
        @Body Merchant merchant
    );

    @POST("business-hour/update")
    Call<SingleResponse<Merchant>> setBusinessHour(
        @Header("Authorization") String token,
        @Body Merchant merchant
    );

    /* SERVICES FOR SERVICE */
    @GET("service/find-by-merchant")
    Call<MultipleResponses<Service>> findServiceByMerchant(
        @Header("Authorization") String token
    );

    @POST("service/store")
    Call<SingleResponse<Service>> createService(
        @Header("Authorization") String token,
        @Body Service service
    );

    @PUT("service/update")
    Call<SingleResponse<Service>> updateService(
        @Header("Authorization") String token,
        @Body Service service
    );

    /* SERVICES FOR QUEUE */
    @FormUrlEncoded
    @POST("queue/find-by-date")
    Call<SingleResponse<EstimatedQueue>> findQueueByDate(
        @Header("Authorization") String token,
        @Field("service_id") int serviceId,
        @Field("date") String date
    );

    @FormUrlEncoded
    @POST("queue/find-last-queue")
    Call<SingleResponse<String>> findLastQueue(
        @Header("Authorization") String token,
        @Field("service_id") int serviceId,
        @Field("date") String date
    );

    @POST("queue/store")
    Call<SingleResponse<Queue>> createQueue(
        @Header("Authorization") String token,
        @Body Queue queue
    );

    @GET("queue/find-active-by-user")
    Call<MultipleResponses<Queue>> findActiveQueueByUser(
        @Header("Authorization") String token
    );

    @GET("queue/find-history-by-user")
    Call<MultipleResponses<Queue>> findHistoryQueueByUser(
        @Header("Authorization") String token
    );

    @GET("queue/count-waiting-by-merchant")
    Call<SingleResponse<Integer>> countWaitingQueueByMerchant(
        @Header("Authorization") String token
    );

    @GET("queue/find-waiting-by-merchant")
    Call<MultipleResponses<Queue>> findWaitingQueueByMerchant(
        @Header("Authorization") String token
    );

    @GET("queue/find-history-by-merchant")
    Call<MultipleResponses<Queue>> findHistoryQueueByMerchant(
        @Header("Authorization") String token
    );

    @FormUrlEncoded
    @POST("queue/update-status")
    Call<SingleResponse<Queue>> updateQueueStatus(
        @Header("Authorization") String token,
        @Field("queue_id") int id,
        @Field("status") String status
    );

    @FormUrlEncoded
    @POST("queue/find-by-qrcode")
    Call<SingleResponse<Queue>> findQueueByQRCode(
        @Header("Authorization") String token,
        @Field("id") int id
    );
}
