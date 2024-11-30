package com.casinocards.appmagicTeenpatti.Comman;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.casinocards.appmagicTeenpatti.Activity.Homepage;
import com.casinocards.appmagicTeenpatti.ApiClasses.Const;
import com.casinocards.appmagicTeenpatti.R;
import com.casinocards.appmagicTeenpatti.Utils.Functions;
import com.casinocards.appmagicTeenpatti.Utils.Variables;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DialogWebviewUpi {
    WebView mWebView;
    Context context;
String PAYMENT_URL="";
    public DialogWebviewUpi(Context context) {
        this.context = context;
    }

    public interface DealerInterface {

        void onClick(int pos);

    }

    TextView txtnotfound;

    public void showDialog(String tag) {
        // custom dialog
        final Dialog dialog = Functions.DialogInstance(context);
        dialog.setContentView(R.layout.dialog_webviewcontent);
        dialog.setTitle("Title...");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ((ImageView) dialog.findViewById(R.id.imgclosetop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        txtnotfound = dialog.findViewById(R.id.txtnotfound);
        TextView txtheader = dialog.findViewById(R.id.txtheader);
        txtheader.setText("Upi Gateway");

        mWebView = dialog.findViewById(R.id.webview);

        mWebView.setBackgroundColor(Color.TRANSPARENT);
        UpiApi(mWebView,dialog,tag);


        initWebView();


        dialog.show();
       // String PAYMENT_URL = "https://merchant.upigateway.com/gateway/pay/c9c295cddf436fdff2cc629acd3267ac";

        Functions.setDialogParams(dialog);


    }

    private void callUpi(String PAYMENT_URL) {
        if (PAYMENT_URL.startsWith("upi:")) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(PAYMENT_URL));
            context.startActivity(intent);
        } else {
            mWebView.loadUrl(PAYMENT_URL);
        }
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    private void initWebView() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setSupportMultipleWindows(true);
        // Do not change Useragent otherwise it will not work. even if not working uncommit below
        // mWebView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 4.4.4; One Build/KTU84L.H4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.135 Mobile Safari/537.36");
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.addJavascriptInterface(new WebviewInterface(), "Interface");
    }

    public class WebviewInterface {
        @JavascriptInterface
        public void paymentResponse(String client_txn_id, String txn_id) {
            Log.i("TAG", client_txn_id);
            Log.i("TAG", txn_id);
            // this function is called when payment is done (success, scanning ,timeout or cancel by user).
            // You must call the check order status API in server and get update about payment.
            // 🚫 Do not Call UpiGateway API in Android App Directly.
            Toast.makeText(context, "Order ID: " + client_txn_id + ", Txn ID: " + txn_id, Toast.LENGTH_SHORT).show();
            // Close the Webview.
        }

        @JavascriptInterface
        public void errorResponse() {
            // this function is called when Transaction in Already Done or Any other Issue.
            Toast.makeText(context, "Transaction Error.", Toast.LENGTH_SHORT).show();
            // Close the Webview.
        }
    }

    private void UpiApi(final WebView webview, final Dialog dialog, final String tag) {

        final RelativeLayout rlt_progress = dialog.findViewById(R.id.rlt_progress);
        rlt_progress.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.PG_UPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        try {
                            Log.d("czcz",new Gson().toJson(response));
                            JSONObject jsonObject = new JSONObject(response);
                            int code = jsonObject.getInt("code");
                            String message = jsonObject.getString("message");
                            PAYMENT_URL = jsonObject.getString("payment_url");
                            Log.d("czcz",PAYMENT_URL);
                            callUpi(PAYMENT_URL);

                            if (code==200) {


                             //   JSONObject jsonObject1 = jsonObject.getJSONObject("setting");

                                //String data = "";


                              /*  if (data.equals("")) {
                                    txtnotfound.setVisibility(View.VISIBLE);
                                } else {
                                    txtnotfound.setVisibility(View.GONE);
                                }*/


                             //   data = data.replaceAll("&#39;", "'");
/*
                                String szMessage = "<font face= \"trebuchet\" size=3 color=\"#fff\"><b>"
                                        + data
                                        + "</b></font>";*/


                                webview.getSettings().setJavaScriptEnabled(true);
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
                Functions.showToast(context, "Something went wrong");
                txtnotfound.setVisibility(View.VISIBLE);
                rlt_progress.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("txnAmount","17");
                params.put("customerName","bet");
                params.put("customerMobile", "9876543210");
                params.put("customerEmail", "qwerty@gmail.com");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Const.TOKEN);
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);

    }


}
