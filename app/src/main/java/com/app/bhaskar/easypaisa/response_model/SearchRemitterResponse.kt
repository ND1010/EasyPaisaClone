package com.app.bhaskar.easypaisa.response_model


import com.google.gson.annotations.SerializedName

data class SearchRemitterResponse(
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: String = "",
    @SerializedName("transdata")
    var transdata: Transdata = Transdata(),
    @SerializedName("userdata")
    var userdata: Userdata = Userdata()
) {
    data class Transdata(
        @SerializedName("beneid")
        var beneid: Any = Any(),
        @SerializedName("mobile")
        var mobile: String = "",
        @SerializedName("transid")
        var transid: Int = 0
    )

    data class Userdata(
        @SerializedName("availlimit")
        var availlimit: String = "",
        @SerializedName("benelist")
        var benelist: List<Benelist> = listOf(),
        @SerializedName("kyc")
        var kyc: String = "",
        @SerializedName("mobile")
        var mobile: String = "",
        @SerializedName("name")
        var name: String = "",
        @SerializedName("totallimit")
        var totallimit: String = ""
    ) {
        data class Benelist(
            @SerializedName("beneaccount")
            var beneaccount: String = "",
            @SerializedName("benebank")
            var benebank: String = "",
            @SerializedName("beneid")
            var beneid: Int = 0,
            @SerializedName("beneifsc")
            var beneifsc: String = "",
            @SerializedName("benemobile")
            var benemobile: String = "",
            @SerializedName("benename")
            var benename: String = "",
            @SerializedName("beneotpverify")
            var beneotpverify: Boolean = false,
            @SerializedName("benepaytm")
            var benepaytm: Boolean = false,
            @SerializedName("beneverify")
            var beneverify: Boolean = false
        )
    }
}