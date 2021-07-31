package com.app.bhaskar.easypaisa.response_model


import com.google.gson.annotations.SerializedName

data class MatmOnlinePaymentResponse(
    @SerializedName("amount")
    var amount: String = "",
    @SerializedName("key")
    var key: String = "",
    @SerializedName("message")
    var message: String = "",
    @SerializedName("orderid")
    var orderid: String = "",
    @SerializedName("status")
    var status: String = ""
)