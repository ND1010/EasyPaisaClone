package com.app.bhaskar.easypaisa.mvp.domain


import com.google.gson.annotations.SerializedName

data class LoadWalletUpiResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: String = ""
) {
    data class Data(
        @SerializedName("data")
        var `data`: Data = Data(),
        @SerializedName("URL")
        var uRL: String = ""
    ) {
        data class Data(
            @SerializedName("amount")
            var amount: String = "",
            @SerializedName("client_email")
            var clientEmail: String = "",
            @SerializedName("client_mobile")
            var clientMobile: String = "",
            @SerializedName("client_name")
            var clientName: String = "",
            @SerializedName("client_txn_id")
            var clientTxnId: String = "",
            @SerializedName("client_vpa")
            var clientVpa: String = "",
            @SerializedName("hash")
            var hash: String = "",
            @SerializedName("key")
            var key: String = "",
            @SerializedName("p_info")
            var pInfo: String = "",
            @SerializedName("redirect_url")
            var redirectUrl: String = "",
            @SerializedName("udf1")
            var udf1: String = "",
            @SerializedName("udf2")
            var udf2: String = "",
            @SerializedName("udf3")
            var udf3: String = ""
        )
    }
}