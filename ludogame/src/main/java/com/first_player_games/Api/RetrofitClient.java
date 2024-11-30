package com.first_player_games.Api;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

   private static String BASE_URL = "https://admin.magicteenpatti.in/letscard/";


   // private static String BASE_URL = "http://134.209.152.157/letscard/";
   // private static String BASE_URL = "http://195.35.23.110/letscard/";
    private static Retrofit retrofit = null;
    private static RetrofitClient retrofitClient = null;

    private RetrofitClient() {
        if (retrofit==null) {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(30, TimeUnit.SECONDS);
            builder.readTimeout(30, TimeUnit.SECONDS);
            builder.writeTimeout(30, TimeUnit.SECONDS);
            OkHttpClient client = builder.addInterceptor(interceptor).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
    }

    public static synchronized RetrofitClient getInstance(){
        if(retrofitClient == null)
        {
            retrofitClient = new RetrofitClient();
        }

        return retrofitClient;
    }

    public Api getApi(){
        return retrofit.create(Api.class);
    }

}
