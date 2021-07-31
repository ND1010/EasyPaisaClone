package com.app.bhaskar.easypaisa.response_model


import com.google.gson.annotations.SerializedName

data class OnlinePaymentInitResponse(
    @SerializedName("amount")
    var amount: Int = 0,
    @SerializedName("key")
    var key: String = "",
    @SerializedName("message")
    var message: String = "",
    @SerializedName("orderid")
    var orderid: String = "",
    @SerializedName("status")
    var status: String = ""
)