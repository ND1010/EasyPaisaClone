package com.app.bhaskar.easypaisa.mvp.domain

import com.app.bhaskar.easypaisa.response_model.WalletBalanceResponse

data class WalletFragmentDomain(var walletBalanceResponse: WalletBalanceResponse) {
    constructor():this(WalletBalanceResponse())
}