package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.mvp.model.AccountFragmentModel
import com.app.bhaskar.easypaisa.mvp.model.AepsActivityModel
import com.pa.baseframework.baseview.BasePresenter
import com.pa.baseframework.baseview.BaseView


interface AccountFragmentPresenter : BasePresenter {

    interface AccountFragmentView : BaseView {
        fun doRetriveModel(): AccountFragmentModel
        fun onLogoutSuccess()
    }

    fun changePassword()
    fun doLogout()
}