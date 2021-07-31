package com.app.bhaskar.easypaisa.mvp.model

import android.content.Context
import com.app.bhaskar.easypaisa.mvp.domain.AddNewBankDomain
import com.app.bhaskar.easypaisa.request_model.AddNewBankRequest
import com.app.bhaskar.easypaisa.request_model.ValidateBankDetailRequest
import com.pa.baseframework.baseview.BaseViewModel

class AddNewBankActivityModel: BaseViewModel {

    private var domain : AddNewBankDomain
    private var addNewBankRequest : AddNewBankRequest
    private var validateBankDetailRequest : ValidateBankDetailRequest

    constructor(mContext: Context):super(mContext){
        domain = AddNewBankDomain()
        addNewBankRequest = AddNewBankRequest()
        validateBankDetailRequest = ValidateBankDetailRequest()
    }

    fun getAddNewBankReq():AddNewBankRequest{
        return addNewBankRequest
    }

    fun getDomain():AddNewBankDomain{
        return domain
    }

    fun getValidateBankDetailReq():ValidateBankDetailRequest{
        return validateBankDetailRequest
    }
}