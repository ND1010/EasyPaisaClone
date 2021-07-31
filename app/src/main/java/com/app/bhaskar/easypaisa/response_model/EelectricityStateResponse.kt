package com.app.bhaskar.easypaisa.response_model


import com.google.gson.annotations.SerializedName

data class EelectricityStateResponse(
    @SerializedName("message")
    var message: Message = Message(),
    @SerializedName("status")
    var status: String = ""
) {
    data class Message(
        @SerializedName("state")
        var state: List<State> = listOf()
    ) {
        data class State(
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("plan_code")
            var planCode: String = "",
            @SerializedName("state")
            var state: String = ""
        )
    }
}