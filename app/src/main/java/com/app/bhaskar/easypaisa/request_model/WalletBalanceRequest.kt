package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WalletBalanceRequest(
    @Expose
    @SerializedName("token")
    var apptoken: String = "",
    @Expose
    @SerializedName("user_id")
    var userId: String = ""
)