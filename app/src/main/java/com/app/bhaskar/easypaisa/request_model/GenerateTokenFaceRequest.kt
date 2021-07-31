package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class GenerateTokenFaceRequest(
    @SerializedName("productId")
    var productId: ArrayList<String> = ArrayList()
)