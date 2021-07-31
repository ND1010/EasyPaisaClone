package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.mvp.model.AepsActivityModel
import com.pa.baseframework.baseview.BasePresenter
import com.pa.baseframework.baseview.BaseView


interface AepsPresenter : BasePresenter {
    fun goForTransactionAeps()
    fun doAepsTransaction()
    fun doValidateCustomer(mobile: String)

    interface AepsView : BaseView {
        fun doRetriveModel(): AepsActivityModel
        fun onAepTxnData()
        fun onUserValidate()
    }
}