package com.app.bhaskar.easypaisa.response_model


import com.google.gson.annotations.SerializedName

data class MobileRechargeResponse(
    @SerializedName("date")
    var date: String = "",
    @SerializedName("amount")
    var maount: String = "",
    @SerializedName("message")
    var message: String = "",
    @SerializedName("number")
    var number: String = "",
    @SerializedName("providername")
    var providername: String = "",
    @SerializedName("refno")
    var refno: String = "",
    @SerializedName("status")
    var status: String = "",
    @SerializedName("txnid")
    var txnid: String = ""
)