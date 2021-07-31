package com.app.bhaskar.easypaisa.mvp.domain

import com.app.bhaskar.easypaisa.response_model.AddNewBankRespones
import com.app.bhaskar.easypaisa.response_model.ValidateBankDetailResponse

data class AddNewBankDomain(var validateBankDetailResponse: ValidateBankDetailResponse,var addNewBankRespones: AddNewBankRespones) {
    constructor():this(ValidateBankDetailResponse(),AddNewBankRespones())
}