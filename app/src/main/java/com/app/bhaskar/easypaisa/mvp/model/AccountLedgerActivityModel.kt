package com.app.bhaskar.easypaisa.mvp.model

import android.content.Context
import com.app.bhaskar.easypaisa.mvp.domain.AccountLedgerDomain
import com.app.bhaskar.easypaisa.request_model.AccountLedgerRequest
import com.pa.baseframework.baseview.BaseViewModel

class AccountLedgerActivityModel : BaseViewModel {

    private var domain: AccountLedgerDomain
    private var accountLedgerRequest: AccountLedgerRequest

    constructor(mContext: Context) : super(mContext) {
        domain = AccountLedgerDomain()
        accountLedgerRequest = AccountLedgerRequest()
    }

    fun getDomain(): AccountLedgerDomain {
        return domain
    }

    fun getAccountLedgerReq(): AccountLedgerRequest {
        return accountLedgerRequest
    }

}