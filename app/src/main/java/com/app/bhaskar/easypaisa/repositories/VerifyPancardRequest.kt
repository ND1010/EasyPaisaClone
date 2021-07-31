package com.app.bhaskar.easypaisa.repositories


import com.google.gson.annotations.SerializedName

data class VerifyPancardRequest(
    @SerializedName("pan")
    var pan: String = "",
    @SerializedName("token")
    var token: String = "",
    @SerializedName("user_id")
    var userId: String = ""
)