package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.mvp.model.ElectricityBillPaymentModel
import com.pa.baseframework.baseview.BasePresenter
import com.pa.baseframework.baseview.BaseView


interface ElectricityBillPaymentPresenter : BasePresenter {
    fun getElStates()
    fun getBillBoard(id: String)
    fun getchBillDetails()
    fun payElectricityBill()

    interface ElecricityBillPaymentView : BaseView {
        fun doRetriveModel(): ElectricityBillPaymentModel
        fun doGetProvider(): String
        fun onState()
        fun onBoard()
        fun onFetchBill()
        fun onBillPaid()
    }
}