package com.app.bhaskar.easypaisa.response_model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AgentKycResponse(
    @Expose
    @SerializedName("message")
    var message: String = "",
    @Expose
    @SerializedName("status")
    var status: String = ""
)