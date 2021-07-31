package com.app.bhaskar.easypaisa.response_model


import com.google.gson.annotations.SerializedName

data class GenerateOtpResponse(
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: String = "",
    @SerializedName("transdata")
    var transdata: Transdata = Transdata()
) {
    data class Transdata(
        @SerializedName("beneid")
        var beneid: Any = Any(),
        @SerializedName("mobile")
        var mobile: String = "",
        @SerializedName("transid")
        var transid: Int = 0
    )
}