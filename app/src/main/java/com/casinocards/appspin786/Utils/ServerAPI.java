package com.casinocards.appmagicTeenpatti.Utils;

import com.casinocards.appmagicTeenpatti.model.OrderReposeBean;
import com.casinocards.appmagicTeenpatti.model.UPIPAymentURLBean;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServerAPI {

    @Headers({"Accept: application/json"})
    @POST("letscard/Home/indianpay2")
    Call<UPIPAymentURLBean> paymentUrl(@Body RequestBody body);

    @Headers({"Accept: application/json"})
    @POST("single_transaction")
    Call<UPIPAymentURLBean> singleTrans(@Body RequestBody body);

    @GET("payinstatus")
    Call<OrderReposeBean> getOrderResponse(@Query("order_id") String orderId);
}
