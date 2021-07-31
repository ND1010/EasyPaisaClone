package com.app.bhaskar.easypaisa.mvp.model

import android.content.Context
import com.app.bhaskar.easypaisa.mvp.domain.SelectBankActivityDomain
import com.pa.baseframework.baseview.BaseViewModel

class SelectBankActivityModel: BaseViewModel {

    private var domain : SelectBankActivityDomain

    constructor(mContext: Context):super(mContext){
        domain = SelectBankActivityDomain()
    }

    fun getLoginDomain():SelectBankActivityDomain{
        return domain
    }
}