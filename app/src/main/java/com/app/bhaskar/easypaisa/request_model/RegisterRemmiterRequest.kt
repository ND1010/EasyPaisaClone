package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class RegisterRemmiterRequest(
    @SerializedName("beneaccount")
    var beneaccount: String = "",
    @SerializedName("beneifsc")
    var beneifsc: String = "",
    @SerializedName("benename")
    var benename: String = "",
    @SerializedName("mobile")
    var mobile: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("token")
    var token: String = "",
    @SerializedName("type")
    var type: String = "",
    @SerializedName("user_id")
    var userId: String = ""
)