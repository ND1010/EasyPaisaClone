package com.app.bhaskar.easypaisa.request_model


import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.google.gson.annotations.SerializedName

data class TransactionHistoryRequest(
    @SerializedName("token")
    var token: String = EasyPaisaApp.getLoggedInUser()?.apptoken!!,
    @SerializedName("type")
    var type: String = "",
    @SerializedName("start")
    var start: String = "",
    @SerializedName("user_id")
    var userId: String =  EasyPaisaApp.getLoggedInUser()?.id.toString()
)