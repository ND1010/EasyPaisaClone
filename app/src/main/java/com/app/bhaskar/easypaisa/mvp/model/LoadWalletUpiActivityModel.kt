package com.app.bhaskar.easypaisa.mvp.model

import android.content.Context
import com.app.bhaskar.easypaisa.mvp.domain.LoadWalletUpiDomain
import com.app.bhaskar.easypaisa.request_model.LoadWalletUpiRequest
import com.app.bhaskar.easypaisa.request_model.OnlinePaymentInitRequest
import com.app.bhaskar.easypaisa.request_model.UpdatePaymentStatusRequest
import com.pa.baseframework.baseview.BaseViewModel

class LoadWalletUpiActivityModel : BaseViewModel {

    private var domain: LoadWalletUpiDomain = LoadWalletUpiDomain()
    private var loadWalletUpiRequest = LoadWalletUpiRequest()
    private var onlinePaymentInitRequest = OnlinePaymentInitRequest()
    private val updatePaymentStatusRequest = UpdatePaymentStatusRequest()

    constructor(mContext: Context) : super(mContext)

    fun getUpdatePaymentStatusRequest():UpdatePaymentStatusRequest{
        return updatePaymentStatusRequest
    }

    fun getOnlinePaymentInitRequest():OnlinePaymentInitRequest{
        return onlinePaymentInitRequest
    }

    fun getLoadWalletUpiReq():LoadWalletUpiRequest{
        return loadWalletUpiRequest
    }

    fun getLoginDomain(): LoadWalletUpiDomain {
        return domain
    }
}