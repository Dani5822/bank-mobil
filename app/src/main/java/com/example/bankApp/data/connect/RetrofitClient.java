package com.example.bankApp.data.connect;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String localhost = "http://10.0.2.2:3000/";
    private static final String api = "https://cdn.jsdelivr.net/npm/@fawazahmed0/";
    private static Retrofit retrofit;
    private static Retrofit euroRetrofit;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(localhost)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getEuroInstance() {
        if (euroRetrofit == null) {
            euroRetrofit = new Retrofit.Builder()
                    .baseUrl(api)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return euroRetrofit;
    }
}
