package com.app.bhaskar.easypaisa.model


import com.google.gson.annotations.SerializedName

data class HomeData(
    @SerializedName("category_name")
    var categoryName: String = "",
    @SerializedName("servi")
    var servi: ArrayList<Servi> = ArrayList()
) {
    data class Servi(
        @SerializedName("service_icon")
        var serviceIcon: Int = 0,
        @SerializedName("service_name")
        var serviceName: String = "",
        @SerializedName("service_id")
        var serviceId: Int = 0

    )
}