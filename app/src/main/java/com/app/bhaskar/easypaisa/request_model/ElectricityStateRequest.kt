package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class ElectricityStateRequest(
    @SerializedName("token")
    var token: String = "",
    @SerializedName("user_id")
    var userId: String = ""
)