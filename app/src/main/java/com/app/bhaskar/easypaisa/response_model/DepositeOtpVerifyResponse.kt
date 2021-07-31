package com.app.bhaskar.easypaisa.response_model


import com.google.gson.annotations.SerializedName

data class DepositeOtpVerifyResponse(
    @SerializedName("account")
    var account: String = "",
    @SerializedName("amount")
    var amount: String = "",
    @SerializedName("benename")
    var benename: String = "",
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: String = "",
    @SerializedName("txnid")
    var txnid: String = ""
)