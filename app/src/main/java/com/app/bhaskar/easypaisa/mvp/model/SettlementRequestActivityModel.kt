package com.app.bhaskar.easypaisa.mvp.model

import android.content.Context
import com.app.bhaskar.easypaisa.mvp.domain.SettlementRequestActivityDomain
import com.pa.baseframework.baseview.BaseViewModel

class SettlementRequestActivityModel : BaseViewModel {

    private var domain: SettlementRequestActivityDomain = SettlementRequestActivityDomain()

    constructor(mContext: Context) : super(mContext)


    fun getDomain(): SettlementRequestActivityDomain {
        return domain
    }
}