package com.app.bhaskar.easypaisa.response_model


import com.google.gson.annotations.SerializedName

data class GenerateTokenFaceResponse(
    @SerializedName("requestId")
    var requestId: String = "",
    @SerializedName("result")
    var result: Result = Result(),
    @SerializedName("statusCode")
    var statusCode: Int = 0
) {
    data class Result(
        @SerializedName("data")
        var `data`: Data = Data(),
        @SerializedName("reason")
        var reason: Any = Any(),
        @SerializedName("success")
        var success: Boolean = false
    ) {
        data class Data(
            @SerializedName("karzaToken")
            var karzaToken: String = ""
        )
    }
}