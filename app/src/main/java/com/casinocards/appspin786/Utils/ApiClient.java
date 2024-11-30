package com.casinocards.appmagicTeenpatti.Utils;

import static paytm.assist.easypay.easypay.appinvoke.BuildConfig.BASE_URL;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;
    private static ServerAPI serverAPI = null;
    //private static String BASE_URLUPI = "https://indianpay.co.in/admin/";
    private static String BASE_URLUPI = "https://admin.magicteenpatti.in/";


    public static ServerAPI getServerApi(Context context) {
//
//
//     /*   String token = PreferenceManager.getInstance(context).getToken();
//        String imei = PreferenceManager.getInstance(context).getIMEI();
//        String model = PreferenceManager.getInstance(context).getModel();
//*/
        APIAuth apiAuth = new APIAuth();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.addInterceptor(apiAuth)
                .addInterceptor(logging)
                .connectTimeout(40, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS)
                .writeTimeout(40, TimeUnit.SECONDS);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URLUPI)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.build())
                .build();
        serverAPI = retrofit.create(ServerAPI.class);
        return serverAPI;
    }


  /*  public static ServerAPI getServerApi(Context context) {
        APIAuth apiAuth = new APIAuth();
       HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
       okHttpClient.addInterceptor(apiAuth)
            .addInterceptor(logging)
              .connectTimeout(40, TimeUnit.SECONDS)
              .readTimeout(40, TimeUnit.SECONDS)
               .writeTimeout(40, TimeUnit.SECONDS);
        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(AppConstant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServerAPI apiService = retrofit1.create(ServerAPI.class);
        // Create RequestBody from the raw JSON string

   return apiService;
    }*/
}
