package com.app.bhaskar.easypaisa.response_model


import com.google.gson.annotations.SerializedName

data class ElectricityBoardResponse(
    @SerializedName("message")
    var message: Message = Message(),
    @SerializedName("status")
    var status: String = ""
) {
    data class Message(
        @SerializedName("providers")
        var providers: List<Provider> = listOf()
    ) {
        data class Provider(
            @SerializedName("billfetch")
            var billfetch: String = "",
            @SerializedName("extraparamname")
            var extraparamname: String = "",
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("logo")
            var logo: String? = "",
            @SerializedName("name")
            var name: String = "",
            @SerializedName("paramname")
            var paramname: String = "",
            @SerializedName("type")
            var type: String = ""
        )
    }
}