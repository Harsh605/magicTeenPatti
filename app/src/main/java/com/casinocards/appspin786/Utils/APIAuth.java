package com.casinocards.appmagicTeenpatti.Utils;

import com.casinocards.appmagicTeenpatti.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class APIAuth  implements Interceptor{
    private String token = null;
    private String device_model = null;
    private String device_imei = null;
    private String device_type = "Android";

    public APIAuth() {

    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request original = chain.request();
        Request request = original.newBuilder()
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                /* .addHeader("Device-Model", device_model)
                 .addHeader("Device-Imei", device_imei)
                 .addHeader("Device-Type", device_type)*/
                .addHeader("Device-App-Version", BuildConfig.VERSION_NAME)
                .method(original.method(), original.body())
                .build();

        return chain.proceed(request);


    }
}
