package com.app.bhaskar.easypaisa.response_model


import com.google.gson.annotations.SerializedName

data class TransactionFilterDataResponse(
    @SerializedName("history")
    var history: List<History> = listOf()
) {
    data class History(
        @SerializedName("icon")
        var icon: Int = 0,
        @SerializedName("name")
        var name: String = "",
        @SerializedName("type")
        var type: String = ""
    )
}