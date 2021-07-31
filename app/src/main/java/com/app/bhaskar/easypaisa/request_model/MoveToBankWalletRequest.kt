package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class MoveToBankWalletRequest(
    @SerializedName("account_id")
    var account: String = "",
    @SerializedName("bank")
    var bank: String = "",
    @SerializedName("ifsc")
    var ifsc: String = "",
    @SerializedName("token")
    var token: String = "",
    @SerializedName("type")
    var type: String = "",
    @SerializedName("user_id")
    var userId: String = "",
    @SerializedName("amount")
    var amount: String = "",
    @SerializedName("latitude")
    var latitude: String = "",
    @SerializedName("longitude")
    var longitude: String = ""
)