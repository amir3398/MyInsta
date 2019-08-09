package com.example.myinsta.data;

import android.content.Context;

import com.example.myinsta.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private Retrofit retrofit;
    private static RetrofitClient instance;


    public static RetrofitClient getInstance(Context context){
        if(instance==null)
            instance=new RetrofitClient( context);

        return instance;
    }


    private RetrofitClient(Context context){
        retrofit=new Retrofit.Builder().baseUrl(context.getString(R.string.server_address)).
                addConverterFactory(GsonConverterFactory.create()).build();
    }


    public Api getApi(){

        return retrofit.create(Api.class);
    }

}
