package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.mvp.model.SelectBankActivityModel
import com.pa.baseframework.baseview.BasePresenter
import com.pa.baseframework.baseview.BaseView


interface SelectBankPresenter : BasePresenter {
    interface SelectBankView : BaseView {
        fun doRetriveModel(): SelectBankActivityModel
    }
}