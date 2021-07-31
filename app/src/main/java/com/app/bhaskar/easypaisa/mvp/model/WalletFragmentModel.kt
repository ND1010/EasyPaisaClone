package com.app.bhaskar.easypaisa.mvp.model

import android.content.Context
import com.app.bhaskar.easypaisa.mvp.domain.WalletFragmentDomain
import com.app.bhaskar.easypaisa.request_model.WalletBalanceRequest
import com.pa.baseframework.baseview.BaseViewModel

class WalletFragmentModel: BaseViewModel {
    private var domain : WalletFragmentDomain
    private var walletBalanceReq : WalletBalanceRequest

    constructor(mContext: Context):super(mContext){
        domain = WalletFragmentDomain()
        walletBalanceReq = WalletBalanceRequest()
    }

    fun getDomain():WalletFragmentDomain{
        return domain
    }
    fun getWalletBalanceReq():WalletBalanceRequest{
        return  walletBalanceReq
    }
}