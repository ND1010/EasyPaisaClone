package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.mvp.model.ChangePassActivityModel
import com.pa.baseframework.baseview.BasePresenter
import com.pa.baseframework.baseview.BaseView


interface ChangePasswordPresenter : BasePresenter {
    fun changePassword()

    interface ChangePassView : BaseView {
        fun doRetriveModel(): ChangePassActivityModel
        fun onPasswordChanged()
    }
}