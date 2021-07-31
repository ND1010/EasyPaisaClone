package com.app.bhaskar.easypaisa.mvp.domain

import com.app.bhaskar.easypaisa.response_model.AddNewBeneficiaryResponse

data class AddNewBeneActivityDomain(var addNewBenRe: AddNewBeneficiaryResponse) {
    constructor():this(AddNewBeneficiaryResponse())
}