package com.app.bhaskar.easypaisa.response_model


import com.google.gson.annotations.SerializedName

data class RechargeProviderResponse(
    @SerializedName("message")
    var message: Message = Message(),
    @SerializedName("status")
    var status: String = ""
) {
    data class Message(
        @SerializedName("providers")
        var providers: List<Provider> = ArrayList()
    ) {
        data class Provider(
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("name")
            var name: String = "",
            @SerializedName("type")
            var type: String = "",
            @SerializedName("logo")
            var logo: String? = ""
        ){
            override fun toString(): String {
                return name
            }
        }

    }
}