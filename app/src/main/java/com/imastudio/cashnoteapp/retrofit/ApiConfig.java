package com.imastudio.cashnoteapp.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiConfig {

    static OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();

    public static ApiServices getApiService(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.97.18/cashnote/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        ApiServices service = retrofit.create(ApiServices.class);
        return service;
    }
}
