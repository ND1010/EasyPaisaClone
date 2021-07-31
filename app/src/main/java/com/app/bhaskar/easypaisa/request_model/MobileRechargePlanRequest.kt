package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class MobileRechargePlanRequest(
    @SerializedName("number")
    var number: String = "",
    @SerializedName("plantype")
    var plantype: String = "",
    @SerializedName("provider")
    var provider: String = "",
    @SerializedName("token")
    var token: String = "",
    @SerializedName("transactionType")
    var transactionType: String = "",
    @SerializedName("user_id")
    var userId: String = ""
)