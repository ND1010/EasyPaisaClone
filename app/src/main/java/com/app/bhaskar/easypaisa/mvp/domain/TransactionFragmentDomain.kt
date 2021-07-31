package com.app.bhaskar.easypaisa.mvp.domain

import com.app.bhaskar.easypaisa.response_model.TransactionResponse

data class TransactionFragmentDomain(var transactionHistoryResponse: TransactionResponse) {
    constructor():this(TransactionResponse())
}