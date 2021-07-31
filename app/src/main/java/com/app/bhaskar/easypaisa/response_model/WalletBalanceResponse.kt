package com.app.bhaskar.easypaisa.response_model


import com.google.gson.annotations.SerializedName

data class WalletBalanceResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: String = ""
) {
    data class Data(
        @SerializedName("aepsbalance")
        var aepsbalance: Double = 0.0,
        @SerializedName("mainwallet")
        var mainwallet: Double = 0.0
    )
}