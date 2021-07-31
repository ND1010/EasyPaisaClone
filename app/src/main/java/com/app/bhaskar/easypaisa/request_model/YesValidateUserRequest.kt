package com.app.bhaskar.easypaisa.request_model


import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.google.gson.annotations.SerializedName

data class YesValidateUserRequest(
    @SerializedName("mobileNumber")
    var mobileNumber: String = "",
    @SerializedName("token")
    var token: String = EasyPaisaApp.getLoggedInUser()!!.apptoken,
    @SerializedName("transactionType")
    var transactionType: String = "verification",
    @SerializedName("user_id")
    var userId: String = EasyPaisaApp.getLoggedInUser()!!.id.toString()
)