package com.app.bhaskar.easypaisa.response_model


import com.google.gson.annotations.SerializedName

data class WalletRequiredDataResponse(
    @SerializedName("message")
    var message: Message = Message(),
    @SerializedName("status")
    var status: String = ""
) {
    data class Message(
        @SerializedName("accounts")
        var accounts: List<Account> = listOf(),
        @SerializedName("banks")
        var banks: List<Bank> = listOf(),
        @SerializedName("paymodes")
        var paymodes: List<Paymode> = listOf(),
        @SerializedName("useraccount")
        var useraccount: String = "",
        @SerializedName("userbank")
        var userbank: String = "",
        @SerializedName("userifsc")
        var userifsc: String = ""
    ) {
        data class Account(
            @SerializedName("account")
            var account: String = "",
            @SerializedName("accounttype")
            var accounttype: String = "",
            @SerializedName("bank")
            var bank: String = "",
            @SerializedName("bankname")
            var bankname: String = "",
            @SerializedName("created_at")
            var createdAt: String = "",
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("ifsc")
            var ifsc: String = "",
            @SerializedName("name")
            var name: String = "",
            @SerializedName("status")
            var status: String = "",
            @SerializedName("updated_at")
            var updatedAt: String = "",
            @SerializedName("user_id")
            var userId: Int = 0,
            @SerializedName("username")
            var username: String = ""

        ) {
            override fun toString(): String {
                return bank
            }
        }

        data class Bank(
            @SerializedName("account")
            var account: String = "",
            @SerializedName("branch")
            var branch: String = "",
            @SerializedName("created_at")
            var createdAt: String = "",
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("ifsc")
            var ifsc: String = "",
            @SerializedName("name")
            var name: String = "",
            @SerializedName("status")
            var status: String = "",
            @SerializedName("updated_at")
            var updatedAt: String = "",
            @SerializedName("user_id")
            var userId: Int = 0


        ) {
            override fun toString(): String {
                return name
            }
        }

        data class Paymode(
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("name")
            var name: String = "",
            @SerializedName("status")
            var status: String = ""

        ) {
            override fun toString(): String {
                return name
            }
        }
    }
}