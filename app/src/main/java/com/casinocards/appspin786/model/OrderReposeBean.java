package com.casinocards.appmagicTeenpatti.model;

import com.google.gson.annotations.SerializedName;

public class OrderReposeBean {

    @SerializedName("status")
    private String status;
    @SerializedName("transactionid")
    private String transactionid;
    @SerializedName("amount")
    private String amount;
    @SerializedName("utr")
    private String utr;
    @SerializedName("date")
    private String date;
    @SerializedName("vpa")
    private String vpa;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUtr() {
        return utr;
    }

    public void setUtr(String utr) {
        this.utr = utr;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVpa() {
        return vpa;
    }

    public void setVpa(String vpa) {
        this.vpa = vpa;
    }
}
