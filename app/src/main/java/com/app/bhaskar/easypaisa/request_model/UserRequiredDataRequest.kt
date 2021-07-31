package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class UserRequiredDataRequest(
    @SerializedName("token")
    var apptoken: String = "",
    @SerializedName("user_id")
    var userId: String = ""
)