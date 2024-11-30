package com.casinocards.appmagicTeenpatti;

import com.google.gson.annotations.SerializedName;

public class upiResponseBean {

    @SerializedName("message")
    private String message;
    @SerializedName("order_id")
    private Integer orderId;
    @SerializedName("payment_url")
    private String paymentUrl;
    @SerializedName("upi_id_hash")
    private String upiIdHash;
    @SerializedName("code")
    private Integer code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getUpiIdHash() {
        return upiIdHash;
    }

    public void setUpiIdHash(String upiIdHash) {
        this.upiIdHash = upiIdHash;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
