package com.app.bhaskar.easypaisa.mvp.model

import android.content.Context
import com.app.bhaskar.easypaisa.mvp.domain.LoadWalletActivityDomain
import com.pa.baseframework.baseview.BaseViewModel

class LoadWalletActivityModel : BaseViewModel {

    private var domain: LoadWalletActivityDomain = LoadWalletActivityDomain()

    constructor(mContext: Context) : super(mContext)

    fun getDomain(): LoadWalletActivityDomain {
        return domain
    }
}