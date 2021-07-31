package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class RozarPayConfig(
    @SerializedName("config")
    var config: Config = Config()
) {
    data class Config(
        @SerializedName("display")
        var display: Display = Display()
    ) {
        data class Display(
            @SerializedName("blocks")
            var blocks: Blocks = Blocks(),
            @SerializedName("preferences")
            var preferences: Preferences = Preferences(),
            @SerializedName("sequence")
            var sequence: List<String> = listOf()
        ) {
            data class Blocks(
                @SerializedName("banks")
                var banks: Banks = Banks()
            ) {
                data class Banks(
                    @SerializedName("instruments")
                    var instruments: ArrayList<Instrument> = ArrayList(),
                    @SerializedName("name")
                    var name: String = "Pay using UPI"
                ) {
                    data class Instrument(
                        @SerializedName("method")
                        var method: String = "upi"
                    )
                }
            }

            data class Preferences(
                @SerializedName("show_default_blocks")
                var showDefaultBlocks: Boolean = false
            )
        }
    }
}