package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.mvp.model.AddNewBeneActivityModel
import com.pa.baseframework.baseview.BasePresenter
import com.pa.baseframework.baseview.BaseView


interface AddNewBeneficiaryPresenter : BasePresenter {

    fun doAddNewBeneficiary()

    interface AddNewBeneficiaryView : BaseView {
        fun doRetriveModel(): AddNewBeneActivityModel
        fun onBenAdded()
    }
}