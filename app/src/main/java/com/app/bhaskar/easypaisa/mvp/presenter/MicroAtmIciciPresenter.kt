package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.mvp.model.MicroAtmIciciActivityModel
import com.pa.baseframework.baseview.BasePresenter
import com.pa.baseframework.baseview.BaseView


interface MicroAtmIciciPresenter : BasePresenter {
    fun doTransaction()
    fun doAtmInitTransaction()
    fun doAtmUpdateTransaction()

    interface MicroAtmIciciView : BaseView {
        fun doRetriveModel(): MicroAtmIciciActivityModel
        fun onMicroInitResponse()
        fun onMicroUpdate()
    }
}