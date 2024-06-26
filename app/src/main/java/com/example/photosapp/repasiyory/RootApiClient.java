package com.example.photosapp.repasiyory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RootApiClient {
    public static String BASE_URL="https://api.unsplash.com/";
    //public static String BASE_URL="https://api.unsplash.com/";
    public static Retrofit retrofit = null;

    public static RootService getRootService(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(RootService.class);
    };

}
