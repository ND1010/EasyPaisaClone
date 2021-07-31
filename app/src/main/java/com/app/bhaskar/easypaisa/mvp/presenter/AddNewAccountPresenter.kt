package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.mvp.model.AddNewBankActivityModel
import com.pa.baseframework.baseview.BasePresenter
import com.pa.baseframework.baseview.BaseView


interface AddNewAccountPresenter : BasePresenter {
    fun addNewAccount()
    fun validateAccountDetail()

    interface AddNewAccountView : BaseView {
        fun doRetriveModel(): AddNewBankActivityModel
        fun onAccountValidate()
        fun onAccountAdded()
    }
}