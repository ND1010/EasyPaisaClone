package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest(
    @SerializedName("token")
    var apptoken: String = "",
    @SerializedName("oldpassword")
    var oldpassword: String = "",
    @SerializedName("password")
    var password: String = "",
    @SerializedName("user_id")
    var userId: String = ""
)