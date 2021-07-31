package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class MatmOnlineOrderRequest(
    @SerializedName("pantokens")
    var pantokens: String = "",
    @SerializedName("token")
    var token: String = "",
    @SerializedName("transactionType")
    var transactionType: String = "",
    @SerializedName("type")
    var type: String = "",
    @SerializedName("address")
    var address: String = "",
    @SerializedName("user_id")
    var userId: String = ""
)