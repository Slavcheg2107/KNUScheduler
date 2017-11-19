package com.knu.krasn.knuscheduler.Network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by krasn on 10/29/2017.
 */

public class RetrofitConfig {
    private final static String BASE_URL = "https://immense-refuge-24759.herokuapp.com/api/";

    private HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    private OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(4000, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .build();

    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    private ApiInterface apiNetwork = retrofit.create(ApiInterface.class);

    public ApiInterface getApiNetwork() {
        return apiNetwork;
    }
}
