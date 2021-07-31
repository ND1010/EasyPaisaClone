package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class BasicDataValuePair(
    @SerializedName("param")
    var `param`: String = "",
    @SerializedName("value")
    var value: String = ""
)