package com.app.bhaskar.easypaisa.response_model


import com.google.gson.annotations.SerializedName

data class OnlinePaymentError(
    @SerializedName("error")
    var error: Error = Error()
) {
    data class Error(
        @SerializedName("code")
        var code: String = "",
        @SerializedName("description")
        var description: String = "",
        @SerializedName("reason")
        var reason: String = "",
        @SerializedName("source")
        var source: String = "",
        @SerializedName("step")
        var step: String = ""
    )
}