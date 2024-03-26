package com.example.lab5_ph32356;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.lab5_ph32356.ApiService.DOMAIN;
public class HttpRequest {
    private ApiService reApiService;

    public HttpRequest() {
        reApiService= new Retrofit.Builder()
                .baseUrl(DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiService.class);
}
    public ApiService callAPI() {
        return reApiService;
    }
}
