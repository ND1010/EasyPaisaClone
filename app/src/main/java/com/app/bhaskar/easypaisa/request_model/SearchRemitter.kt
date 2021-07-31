package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class SearchRemitter(
    @SerializedName("mobile")
    var mobile: String = "",
    @SerializedName("token")
    var token: String = "",
    @SerializedName("type")
    var type: String = "verification",
    @SerializedName("user_id")
    var userId: String = ""
)