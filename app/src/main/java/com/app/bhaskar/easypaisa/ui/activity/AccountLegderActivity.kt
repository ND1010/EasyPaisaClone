package com.app.bhaskar.easypaisa.ui.activity

import aepsapp.easypay.com.aepsandroid.adapters.AccountLedgerAdapter
import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.mvp.model.AccountLedgerActivityModel
import com.app.bhaskar.easypaisa.mvp.presenter.AccountAccountLedgerPresenter
import com.app.bhaskar.easypaisa.mvp.presenter.AccountLedgerPresenterImpl
import com.app.bhaskar.easypaisa.response_model.AccountLedgerResponse
import com.app.bhaskar.easypaisa.utils.Constants
import com.google.android.material.tabs.TabLayout
import com.pa.baseframework.baseview.BaseActivity
import kotlinx.android.synthetic.main.activity_account_legder.*
import kotlinx.android.synthetic.main.toolbar_simple.*
import javax.inject.Inject

class AccountLegderActivity : BaseActivity(),
    AccountAccountLedgerPresenter.AccountAccountLedgerView {


    companion object {
        const val TAG = "AccountLegderActivity"
    }

    private var lederType: String = "accountladger"
    private lateinit var model: AccountLedgerActivityModel

    @Inject
    lateinit var presenter: AccountLedgerPresenterImpl
    private lateinit var accountLedgerAdapter: AccountLedgerAdapter
    private var arrayListAcc = ArrayList<AccountLedgerResponse.Data>()
    private var page = 0
    private val limit = 20
    var pastVisiblesItems: Int = 0
    var visibleItemCount: Int = 0
    var totalItemCount: Int = 0
    private var loading = true
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_legder)
        initviews()
    }

    private fun initviews() {
        ivHomeBack.setOnClickListener {
            onBackPressed()
        }
        tvToolbarTitle.text = "Account Ledger"
        model = AccountLedgerActivityModel(getViewActivity())
        presenter = AccountLedgerPresenterImpl(this)
        EasyPaisaApp.component.inject(presenter)

        layoutManager = LinearLayoutManager(getViewActivity())
        accountLedgerAdapter = AccountLedgerAdapter(getViewActivity(), arrayListAcc) {

        }
        recyclerviewAccL.layoutManager = layoutManager
        recyclerviewAccL.setHasFixedSize(true)
        recyclerviewAccL.adapter = accountLedgerAdapter
        initPageData()
        recyclerviewAccL.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = layoutManager.childCount
                    totalItemCount = layoutManager.itemCount
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false
                            presenter.getAccountTransactionLeder()
                        }
                    }
                }
            }
        })

        tablayoutAeps.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        lederType = "accountladger"
                        initPageData()
                    }
                    1 -> {
                        lederType = "aepsladger"
                        initPageData()
                    }
                }
            }
        })

    }

    override fun showProgress() {
        if (arrayListAcc.isNotEmpty()){
            recyclerviewAccL.visibility = View.VISIBLE
        }else{
            recyclerviewAccL.visibility = View.GONE
        }
        progressBarAcc.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        recyclerviewAccL.visibility = View.VISIBLE
        progressBarAcc.visibility = View.GONE
    }

    private fun initPageData() {
        page = 0
        arrayListAcc.clear()
        doRetriveModel().getAccountLedgerReq().type = lederType
        doRetriveModel().getAccountLedgerReq().start = page.toString()
        presenter.getAccountTransactionLeder()
    }

    override fun doRetriveModel(): AccountLedgerActivityModel {
        return model
    }

    override fun getViewActivity(): Activity {
        return this@AccountLegderActivity
    }

    override fun onNetworkStateChange(isConnect: Boolean) {
    }

    override fun onAccountLeder() {
        if (doRetriveModel().getDomain().accountLedgerResponse.statuscode == Constants.ApiResponse.RES_SUCCESS) {
            arrayListAcc.addAll(doRetriveModel().getDomain().accountLedgerResponse.data)
            accountLedgerAdapter.setData(arrayListAcc)
            if (doRetriveModel().getDomain().accountLedgerResponse.data.size >= limit) {
                page += 1
                loading = true
            } else {
                loading = false
            }
        } else {
            arrayListAcc.clear()
            accountLedgerAdapter.setData(arrayListAcc)
            page = 0
            loading = false
        }

        if (arrayListAcc.isEmpty()) {
            recyclerviewAccL.visibility = View.GONE
            tvNoTxn.visibility = View.VISIBLE
        } else {
            recyclerviewAccL.visibility = View.VISIBLE
            tvNoTxn.visibility = View.GONE
        }
    }

}