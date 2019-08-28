package com.example.myinsta.data;

import com.example.myinsta.model.JsonResponseModel;
import com.example.myinsta.model.PostModel;

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
            ,@Field("sex") String sex,@Field("user") String username,@Field("pass") String password,
            @Field("image") String image ,@Field("picname") String picname);

    @GET("login.php")
    Call<JsonResponseModel> loginUser(@Query("user") String username, @Query("pass") String password);

    @FormUrlEncoded
    @POST("newpost.php")
    Call<JsonResponseModel> newpost(@Field("username") String username ,@Field("image") String image ,
                                    @Field("picname") String picname,@Field("des") String des );

    @GET("getPost.php")
    Call<PostModel> getPost();

    @GET("getComment.php")
    Call<PostModel> getComment(@Query("postid") String postid);

    @GET("getLike.php")
    Call<PostModel> getLike(@Query("postid") String postid);

    @FormUrlEncoded
    @POST("verify.php")
    Call<JsonResponseModel> verify(@Field("username") String username ,@Field("os") String os  );

    @FormUrlEncoded
    @POST("newComment.php")
    Call<JsonResponseModel> newComment(@Field("username") String username ,@Field("comment") String comment ,
                                    @Field("postid") String postid );

    @FormUrlEncoded
    @POST("like.php")
    Call<JsonResponseModel> like(@Field("userid") String userid ,@Field("postid") String postid);

    @FormUrlEncoded
    @POST("getLikeColor.php")
    Call<JsonResponseModel> getLikeColor(@Field("userid") String userid ,@Field("postid") String postid);

    @FormUrlEncoded
    @POST("getCommentColor.php")
    Call<JsonResponseModel> getCommentColor(@Field("userid") String userid ,@Field("postid") String postid);

    @FormUrlEncoded
    @POST("getSaveColor.php")
    Call<JsonResponseModel> getSaveColor(@Field("userid") String userid ,@Field("postid") String postid);

    @FormUrlEncoded
    @POST("save.php")
    Call<JsonResponseModel> save(@Field("userid") String userid ,@Field("postid") String postid );

}
