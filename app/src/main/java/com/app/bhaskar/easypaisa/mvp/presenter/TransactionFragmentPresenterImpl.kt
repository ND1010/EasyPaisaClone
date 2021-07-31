package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.repositories.EasyPaisaRepository
import com.app.bhaskar.easypaisa.response_model.TransactionFilterDataResponse
import com.app.bhaskar.easypaisa.utils.Utils
import javax.inject.Inject

class TransactionFragmentPresenterImpl(val view: TransactionFragmentPresenter.TransactionFragmentView) :
    TransactionFragmentPresenter {

    private val mEventBus = EasyPaisaApp.getDefault()

    @Inject
    lateinit var repository: EasyPaisaRepository

    override fun onError(message: String) {
    }

    override fun registerBus() {
        mEventBus.register(this)
    }

    override fun unRegisterBus() {
        mEventBus.unregister(this)
    }

    override fun getFilterList(): java.util.ArrayList<TransactionFilterDataResponse.History> {
        val arrayListFilter = ArrayList<TransactionFilterDataResponse.History>()
        if (arrayListFilter.isEmpty()) {
            val aepsFilter = TransactionFilterDataResponse.History(
                R.drawable.ic_aeps_aadhaarpay,
                "AePS Transaction",
                "aepsstatement"
            )
            val mobileRech = TransactionFilterDataResponse.History(
                R.drawable.ic_mobile,
                "Mobile and Dth recharge statement",
                "rechargestatement"
            )
            val BillPayment = TransactionFilterDataResponse.History(
                R.drawable.ic_dth,
                "Utility Bill Payments",
                "billpaystatement"
            )
            val uti = TransactionFilterDataResponse.History(
                R.drawable.ic_money_transfer,
                "UTI Pancard Transaction",
                "utipancardstatement"
            )
            val micro = TransactionFilterDataResponse.History(
                R.drawable.ic_microatm,
                "Micro ATM Transaction",
                "matmstatement"
            )
            val dmt = TransactionFilterDataResponse.History(
                R.drawable.ic_money_transfer,
                "DMT Transaction",
                "dmtstatement"
            )
            /*val aepsFund = TransactionFilterDataResponse.History(
                R.drawable.ic_money_transfer,
                "AePS Fund Request",
                "aepsfundrequest"
            )*/
            /*val aepsWalletFundReq = TransactionFilterDataResponse.History(
                R.drawable.ic_money_transfer,
                "Wallet Fund Request",
                "fundrequest"
            )*/
            val aepsWalletStatement = TransactionFilterDataResponse.History(
                R.drawable.ic_money_transfer,
                "AePS Wallet Statement",
                "awalletstatement"
            )
//            val mainWalletStatement = TransactionFilterDataResponse.History(
//                R.drawable.ic_money_transfer,
//                "Main Wallet Statement",
//                "accountladger"//mainWalletStatement
//            )

            arrayListFilter.add(aepsFilter)
            arrayListFilter.add(micro)
            arrayListFilter.add(mobileRech)
            arrayListFilter.add(uti)
            arrayListFilter.add(BillPayment)
            arrayListFilter.add(dmt)
//            arrayListFilter.add(aepsFund)
//            arrayListFilter.add(aepsWalletFundReq)
            arrayListFilter.add(aepsWalletStatement)
//            arrayListFilter.add(mainWalletStatement)
        }

        return arrayListFilter
    }

    override fun getTransactionHistory() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            val request  = view.doRetriveModel().getTransactionRequest()
            view.showProgress()
            repository.doGetTxnHistory(request, {
                view.hideProgress()
                view.doRetriveModel().getDomain().transactionHistoryResponse = it
                view.onTxnHistory()
            }, {
                view.hideProgress()
                view.showError(it!!.localizedMessage!!)
            })
        } else {
            view.showError(view.getViewActivity().getString(R.string.no_internet_message))
        }
    }

}