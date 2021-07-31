package com.app.bhaskar.easypaisa.mvp.model

import android.content.Context
import com.app.bhaskar.easypaisa.mvp.domain.AddNewBeneActivityDomain
import com.app.bhaskar.easypaisa.request_model.AddNewBeneficiaryRequest
import com.pa.baseframework.baseview.BaseViewModel

class AddNewBeneActivityModel : BaseViewModel {

    private var domain: AddNewBeneActivityDomain
    private var addNewBenRequest = AddNewBeneficiaryRequest()

    constructor(mContext: Context) : super(mContext) {
        domain = AddNewBeneActivityDomain()
        addNewBenRequest = AddNewBeneficiaryRequest()
    }

    fun getAddNewBeneRequest(): AddNewBeneficiaryRequest {
        return addNewBenRequest
    }

    fun getDomain(): AddNewBeneActivityDomain {
        return domain
    }
}