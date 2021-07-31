package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class MicroAtmIciciTransactionRequest(
    @SerializedName("amount")
    var amount: String = "",
    @SerializedName("amount_editable")
    var amountEditable: Boolean = false,
    @SerializedName("imei")
    var imei: String = "",
    @SerializedName("lat")
    var lat: Double = 0.0,
    @SerializedName("long")
    var long: Double = 0.0,
    @SerializedName("mID")
    var mID: String = "",
    @SerializedName("remarks")
    var remarks: String = "",
    @SerializedName("mPss")
    var mPss: String = "",
    @SerializedName("txnID")
    var txnID: String = "",
    @SerializedName("microatm_manufacturer")
    var microatmManufacturer: String = "",
    @SerializedName("number")
    var number: String = "",
    @SerializedName("smID")
    var smID: String = "",
    @SerializedName("type")
    var type: Int = 0
)