package com.casinocards.appmagicTeenpatti.model;

import com.google.gson.annotations.SerializedName;

public class UPIPAymentURLBean {

    @SerializedName("status")
    private String status;
    @SerializedName("amount")
    private String amount;
    @SerializedName("order_id")
    private String orderId;
    @SerializedName("payment_link")
    private String paymentLink;
    @SerializedName("gateway_txn")
    private String gatewayTxn;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentLink() {
        return paymentLink;
    }

    public void setPaymentLink(String paymentLink) {
        this.paymentLink = paymentLink;
    }

    public String getGatewayTxn() {
        return gatewayTxn;
    }

    public void setGatewayTxn(String gatewayTxn) {
        this.gatewayTxn = gatewayTxn;
    }
}
