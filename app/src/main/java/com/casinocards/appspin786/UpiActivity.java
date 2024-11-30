package com.casinocards.appmagicTeenpatti;

import static com.casinocards.appmagicTeenpatti.Activity.Homepage.MY_PREFS_NAME;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.casinocards.appmagicTeenpatti.Activity.BuyChipsDetails;
import com.casinocards.appmagicTeenpatti.ApiClasses.Const;
import com.casinocards.appmagicTeenpatti.Utils.ApiClient;
import com.casinocards.appmagicTeenpatti.Utils.Functions;
import com.casinocards.appmagicTeenpatti.Utils.ServerAPI;
import com.casinocards.appmagicTeenpatti.model.OrderReposeBean;
import com.casinocards.appmagicTeenpatti.model.UPIPAymentURLBean;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class UpiActivity extends AppCompatActivity {
    WebView mWebView;
    TextView txtnotfound;
    String PAYMENT_URL = "";
    ProgressDialog progressDialog;
    int orderID = 0;
    SharedPreferences prefs;
    String planID = "";
    String userId = "";
    String amt = "";
    private float m_downX;
    private boolean isPaymentResponseProcessed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upi);
        mWebView = findViewById(R.id.webview);
        txtnotfound = findViewById(R.id.txtnotfound);
        amt = getIntent().getStringExtra("amount");
        String mobile = getIntent().getStringExtra("mobile");
        String name = getIntent().getStringExtra("name");
        planID = getIntent().getStringExtra("planID");
        userId = getIntent().getStringExtra("userId");
        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        progressDialog = new ProgressDialog(this);
        //     UpiApi(amt,mobile,name,planID,userId);
        //  callUpiPaymentUrl(amt, mobile, name, planID, userId);
        getPaymentUrl(amt, mobile, name);
        //  getAfterPaymentSuccess(amt);

        initWebView();
    }

    private void callUpi(String PAYMENT_URL) {
        if (PAYMENT_URL.startsWith("upi://pay")) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(PAYMENT_URL));
            startActivity(intent);
        } else {
            mWebView.loadUrl(PAYMENT_URL);
        }
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    private void initWebView() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.getSettings().setJavaScriptEnabled(true);

        // Do not change Useragent otherwise it will not work. even if not working uncommit below
        // mWebView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 4.4.4; One Build/KTU84L.H4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.135 Mobile Safari/537.36");

        mWebView.setWebChromeClient(new WebChromeClient());

        //    mWebView.addJavascriptInterface(new DialogWebviewUpi.WebviewInterface(), "Interface");

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//                progressBar.setVisibility(View.VISIBLE);
                Log.d("Zxczxc0", url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if ( !isPaymentResponseProcessed){
                    isPaymentResponseProcessed = true; // Mark as processed
                    //  Toast.makeText(view.getContext(), "No", Toast.LENGTH_SHORT).show();
                    callUPICheckStatus(String.valueOf(orderID));
                }
                Uri uri = (request != null) ? request.getUrl() : null;
                //     Toast.makeText(UpiActivity.this, "1", Toast.LENGTH_SHORT).show();
                Log.d("Zxczxc0", view.getUrl());

                if (uri != null && "upi".equals(uri.getScheme())) {
                    // Handle UPI URL
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    try {
                        startActivityForResult(intent,101);
                    } catch (ActivityNotFoundException e) {
                        // Handle the case where no app can handle the UPI intent
                        Toast.makeText(view.getContext(), "No app found to handle UPI payment", Toast.LENGTH_SHORT).show();
                    }
                }
                return true; // Indicate WebView to not load the URL
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //      Toast.makeText(UpiActivity.this, "2", Toast.LENGTH_SHORT).show();

                Log.d("Zxczxc4444444", url);
                invalidateOptionsMenu();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                try {
                    invalidateOptionsMenu();
                    mWebView.stopLoading();
                    mWebView.loadUrl("about:blank");
                } catch (Exception e) {
                }

                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                }
            }

        });

        mWebView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getPointerCount() > 1) {
                    //Multi touch detected
                    //     Toast.makeText(UpiActivity.this, "44", Toast.LENGTH_SHORT).show();
                    return true;
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        // save the x
                        m_downX = event.getX();
                        //     Toast.makeText(UpiActivity.this, "66", Toast.LENGTH_SHORT).show();

                    }
                    break;

                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP: {
                        // set x so that it doesn't move
                        event.setLocation(m_downX, event.getY());
                    }
                    break;
                }

                return false;
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK || resultCode == 11) { // 11 is a common result code for UPI
                if (data != null) {
                    String transactionResponse = data.getStringExtra("response");
                    if (isPaymentSuccessful(transactionResponse)) {
                        Toast.makeText(this, "  Success Payment", Toast.LENGTH_SHORT).show();
                        callUPICheckStatus(String.valueOf(orderID));
                    } else {
                        Toast.makeText(this, " Payment failed or was cancelled.", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(this, "No response from UPI app.", Toast.LENGTH_SHORT).show();


                }
            } else {
                Toast.makeText(this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private boolean isPaymentSuccessful(String transactionResponse) {
        if (transactionResponse == null) return false;
        // Process response, check if contains "Status=SUCCESS" or similar keywords indicating success
        return transactionResponse.toLowerCase().contains("status=success");
    }

    private void getPaymentUrl(String amt, String mobile, String name) {
        final RelativeLayout rlt_progress = findViewById(R.id.rlt_progress);
        rlt_progress.setVisibility(View.VISIBLE);

        Random random = new Random();
        orderID = 100000 + random.nextInt(900000);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.IndianPayUpi,
                response -> {
                    rlt_progress.setVisibility(View.GONE);
                    Log.e("TAG_onResponse", "onResponse: " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        if (code.equals("true")) {
                            progressDialog.dismiss();
                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            String paymentLink = dataObject.getString("payment_link");
                            PAYMENT_URL = dataObject.getString("payment_link");
                            Log.d("sadasd", paymentLink);
                            callUpi(PAYMENT_URL);
                        } else {
                            finish();
                            Functions.showToast(UpiActivity.this, message + message);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        rlt_progress.setVisibility(View.GONE);
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                rlt_progress.setVisibility(View.GONE);
                // NoInternet(listTicket.this);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("token", Const.TOKEN);
                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("orderid", String.valueOf(orderID));
                params.put("amount", amt);
                params.put("name", name);
                params.put("email", "test@gmail.com");
                params.put("mobile", mobile);
                params.put("remark", "payment");
                params.put("type", "2");
                params.put("redirect_url", "https://admin.magicteenpatti.in/letscard/Home/indianpay");
                Functions.LOGE("BuyChipsDetails", Const.IndianPayUpi + "\n" + params);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(UpiActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void getAfterPaymentSuccess() {
        progressDialog.show();
        Random random = new Random();
        orderID = 100000 + random.nextInt(900000);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.IndianPayUpiStatus,
                response -> {
                    Log.e("TAG_onResponse", "onResponse: " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        if (code.equals("true")) {
                            progressDialog.dismiss();
                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            //      String paymentLink = dataObject.getString("payment_link");
                            //      PAYMENT_URL = dataObject.getString("payment_link");
                            //   Log.d("sadasd", paymentLink);
                            //   callUpi(PAYMENT_URL);
                            Functions.showToast(UpiActivity.this, message + message);
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Functions.showToast(UpiActivity.this, message + message);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                // NoInternet(listTicket.this);
                progressDialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("token", Const.TOKEN);

                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                params.put("plan_id", planID);
                params.put("coin", "0");
                params.put("price", amt);
                params.put("razor_payment_id", String.valueOf(orderID));
                Functions.LOGE("BuyChipsDetails", Const.IndianPayUpiStatus + "\n" + params);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(UpiActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void callUPICheckStatus(String orderID) {
        ServerAPI serverAPI = ApiClient.getServerApi(this);
        //  GeneralUtilities.showDialog(context,"Please wait...");

        try {
       /*     JSONObject jsonObject = new JSONObject();
            jsonObject.put("Mobile", mobNo);
            String json = jsonObject.toString();
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);*/

            Call<OrderReposeBean> call = serverAPI.getOrderResponse(orderID);
            call.enqueue(new retrofit2.Callback<OrderReposeBean>() {
                @Override
                public void onResponse(Call<OrderReposeBean> call, retrofit2.Response<OrderReposeBean> response) {
                    try {

                        if (response != null) {
                            if (response.body() != null) {
                                Log.d("ZXZ", new Gson().toJson(response.body()));
                                //    finish();
                                if (response.body().getStatus().equals("success")) {
                                    Log.d("ZXZ", new Gson().toJson(response.body()));
                                    Toast.makeText(UpiActivity.this, response.body().getStatus(), Toast.LENGTH_SHORT).show();
                                    getAfterPaymentSuccess();
                                }else if (response.body().getStatus().equals("pending")) {

                                    // Toast.makeText(UpiActivity.this, response.body().getStatus(), Toast.LENGTH_SHORT).show();

                                } else {

                                    Log.d("ZXZ", new Gson().toJson(response.body()));
                                    Toast.makeText(UpiActivity.this, response.body().getStatus(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {

                        }
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(Call<OrderReposeBean> call, Throwable t) {
                    Log.d("fxzcxc", t.getLocalizedMessage());

                }
            });
        } catch (Exception e) {

        }
    }

    public void callUpiPaymentUrl(String amt, String mobile, String name, String planID, String userId) {
        ServerAPI serverAPI = ApiClient.getServerApi(this);
        //  GeneralUtilities.showDialog(context,"Please wait...");
        final RelativeLayout rlt_progress = findViewById(R.id.rlt_progress);
        rlt_progress.setVisibility(View.VISIBLE);

        Random random = new Random();
        orderID = 100000 + random.nextInt(900000);

        try {
            JSONObject jsonObject = new JSONObject();
            //      jsonObject.put("merchantid", "04");
            jsonObject.put("orderid", orderID);
            jsonObject.put("amount", amt);
            jsonObject.put("name", name);
            jsonObject.put("email", "test@gmail.com");
            jsonObject.put("mobile", mobile);
            jsonObject.put("remark", "payment");
            jsonObject.put("type", "2");
            jsonObject.put("redirect_url", "https://admin.magicteenpatti.in/letscard/Home/indianpay");
            String json = jsonObject.toString();
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
            Call<UPIPAymentURLBean> call = serverAPI.paymentUrl(requestBody);
            call.enqueue(new retrofit2.Callback<UPIPAymentURLBean>() {
                @Override
                public void onResponse(Call<UPIPAymentURLBean> call, retrofit2.Response<UPIPAymentURLBean> response) {
                    try {
                        rlt_progress.setVisibility(View.GONE);
                        if (response != null) {
                            if (response.body() != null) {
                                if (response.body().getStatus().equals("SUCCESS")) {
                                    PAYMENT_URL = response.body().getPaymentLink();
                                    Log.d("sadasd", PAYMENT_URL);
                                    callUpi(PAYMENT_URL);
                                }
                                if (response.body().getStatus().equals(400)) {
                                    Toast.makeText(UpiActivity.this, response.body().getStatus(), Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(UpiActivity.this, response.body().getStatus(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {

                        }
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(Call<UPIPAymentURLBean> call, Throwable t) {
                    Log.d("fxzcxc", t.getLocalizedMessage());

                }
            });
        } catch (Exception e) {

        }
    }

    private void UpiApi(String amt, String mobile, String name, String planiD, String userIdd) {

        final RelativeLayout rlt_progress = findViewById(R.id.rlt_progress);
        rlt_progress.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.PG_UPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        try {
                            Log.d("czcz", new Gson().toJson(response));
                            JSONObject jsonObject = new JSONObject(response);
                            int code = jsonObject.getInt("code");
                            String message = jsonObject.getString("message");
                            PAYMENT_URL = jsonObject.getString("payment_url");
                            Log.d("czcz", PAYMENT_URL);
                            callUpi(PAYMENT_URL);

                            if (code == 200) {

                                //   webview.loadDataWithBaseURL("", szMessage, "text/html", "UTF-8", "");


                            } else {
                                if (jsonObject.has("message")) {

                                    txtnotfound.setVisibility(View.VISIBLE);

                                }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            txtnotfound.setVisibility(View.VISIBLE);

                        }

                        rlt_progress.setVisibility(View.GONE);


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                Functions.showToast(UpiActivity.this, "Something went wrong");
                txtnotfound.setVisibility(View.VISIBLE);
                rlt_progress.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("txnAmount", amt);
                params.put("customerName", name);
                params.put("customerMobile", mobile);
                params.put("customerEmail", "aaaa@gmail.com");
                params.put("plan_id", planiD);
                params.put("user_id", userIdd);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Const.TOKEN);
                return headers;
            }
        };

        Volley.newRequestQueue(UpiActivity.this).add(stringRequest);

    }

}