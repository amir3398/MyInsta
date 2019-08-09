package com.example.myinsta.data;

import com.example.myinsta.model.JsonResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    @FormUrlEncoded
    @POST("reg.php")
    Call<JsonResponseModel> registerUser(@Field("name") String name ,@Field("family") String family
            ,@Field("sex") String sex,@Field("user") String username,@Field("pass") String password);

    @GET("login.php")
    Call<JsonResponseModel> loginUser(@Query("user") String username, @Query("pass") String password);

}
