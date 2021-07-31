package com.app.bhaskar.easypaisa.ui.fragment


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.mvp.model.WalletFragmentModel
import com.app.bhaskar.easypaisa.mvp.presenter.WalletFragmentPresenter
import com.app.bhaskar.easypaisa.mvp.presenter.WalletFragmentPresenterImpl
import com.app.bhaskar.easypaisa.ui.activity.AccountLegderActivity
import com.app.bhaskar.easypaisa.ui.activity.LoadWalletActivity
import com.app.bhaskar.easypaisa.ui.activity.LoadWalletUpiActivity
import com.app.bhaskar.easypaisa.ui.activity.SettlementRequestActivity
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.pa.baseframework.baseview.BaseFragment
import kotlinx.android.synthetic.main.fragment_wallet.*
import kotlinx.android.synthetic.main.layout_main_wallet.*
import java.text.DecimalFormat
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class WalletFragment : BaseFragment(), WalletFragmentPresenter.WalletView {

    companion object {
        const val TAG = "WalletFragment"
    }

    @Inject
    lateinit var presenter: WalletFragmentPresenterImpl
    private lateinit var model: WalletFragmentModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model = WalletFragmentModel(getViewActivity())
        presenter = WalletFragmentPresenterImpl(this)
        EasyPaisaApp.component.inject(presenter)
        return inflater.inflate(R.layout.fragment_wallet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvMainWallet.text = Utils.formatAmount(0.0)
        tvAepsWallet.text = Utils.formatAmount(0.0)

        tvLoadWallet.setOnClickListener {
            startActivity(Intent(getViewActivity(), LoadWalletActivity::class.java))
        }

        tvMakeFundReq.setOnClickListener {
            startActivity(Intent(getViewActivity(), SettlementRequestActivity::class.java))
        }

        cardMainAccountLedger.setOnClickListener {
            startActivity(Intent(getViewActivity(), AccountLegderActivity::class.java))
        }

        tvLoadWalletUpi.setOnClickListener {
            startActivity(Intent(getViewActivity(), LoadWalletUpiActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.getWalletBalance()
    }

    override fun doRetriveModel(): WalletFragmentModel {
        return model
    }


    override fun showProgress() {
        progressBarWalletBal.visibility = View.VISIBLE
        layoutMainWalletBal.visibility = View.GONE
    }

    override fun hideProgress() {
        activity?.let {
            if (progressBarWalletBal != null)
                progressBarWalletBal.visibility = View.GONE
            if (layoutMainWalletBal != null)
                layoutMainWalletBal.visibility = View.VISIBLE
        }

    }

    override fun onWalletBalance() {
        val response = doRetriveModel().getDomain().walletBalanceResponse
        if (response.status == Constants.ApiResponse.RES_SUCCESS) {
            val decim = DecimalFormat("0.00")
            val amountMain = decim.format(response.data.mainwallet)
            val amountAePs = decim.format(response.data.aepsbalance)
            tvMainWallet.text = Utils.formatAmount(amountMain.toDouble())
            tvAepsWallet.text = Utils.formatAmount(amountAePs.toDouble())
        }
    }
}
