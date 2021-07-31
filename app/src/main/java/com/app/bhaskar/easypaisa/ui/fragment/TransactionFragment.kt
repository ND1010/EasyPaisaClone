package com.app.bhaskar.easypaisa.ui.fragment


import aepsapp.easypay.com.aepsandroid.adapters.TransactionHistoryAdapter
import aepsapp.easypay.com.aepsandroid.fragments.BottomsheetHistoryFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.mvp.model.TransactionFragmentModel
import com.app.bhaskar.easypaisa.mvp.presenter.TransactionFragmentPresenter
import com.app.bhaskar.easypaisa.mvp.presenter.TransactionFragmentPresenterImpl
import com.app.bhaskar.easypaisa.response_model.TransactionFilterDataResponse
import com.app.bhaskar.easypaisa.response_model.TransactionResponse
import com.app.bhaskar.easypaisa.ui.activity.DmtTransactionActivity
import com.app.bhaskar.easypaisa.utils.Constants
import com.pa.baseframework.baseview.BaseFragment
import kotlinx.android.synthetic.main.fragment_transaction.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class TransactionFragment : BaseFragment(), TransactionFragmentPresenter.TransactionFragmentView {

    companion object {
        private const val TAG = "TransactionFragment"
    }

    private lateinit var model: TransactionFragmentModel

    @Inject
    lateinit var presenter: TransactionFragmentPresenterImpl
    private lateinit var historyAdapter: TransactionHistoryAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var arrayListHistory = ArrayList<TransactionResponse.Data>()
    private lateinit var bottomsheetHistoryFilter: BottomsheetHistoryFilter
    private var selectedFilter: TransactionFilterDataResponse.History? = null
    private var page = 0
    private val limit = 20
    var pastVisiblesItems: Int = 0
    var visibleItemCount: Int = 0
    var totalItemCount: Int = 0
    private var loading = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e(TAG,"onCreateView")
        model = TransactionFragmentModel(getViewActivity())
        presenter = TransactionFragmentPresenterImpl(this)
        EasyPaisaApp.component.inject(presenter)
        return inflater.inflate(R.layout.fragment_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e(DmtTransactionActivity.TAG,"onViewCreated")
        recyclerviewHistory.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(getViewActivity(), RecyclerView.VERTICAL, false)
        recyclerviewHistory.layoutManager = layoutManager
        historyAdapter = TransactionHistoryAdapter(getViewActivity(), arrayListHistory) {

        }
        recyclerviewHistory.adapter = historyAdapter
        initPageData()

        tvSelectFilter.setOnClickListener {
            openFilerBottomSheet()
        }

        recyclerviewHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = layoutManager.childCount
                    totalItemCount = layoutManager.itemCount
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false
                            presenter.getTransactionHistory()
                        }
                    }
                }
            }
        })
    }


    private fun initPageData() {
        page = 0
        arrayListHistory.clear()
        doRetriveModel().getTransactionRequest().type = "aepsstatement"
        doRetriveModel().getTransactionRequest().start = page.toString()
        presenter.getTransactionHistory()
    }

    private fun openFilerBottomSheet() {
        bottomsheetHistoryFilter =
            BottomsheetHistoryFilter(getViewActivity(), presenter.getFilterList()) {
                selectedFilter = it
                page = 0
                arrayListHistory.clear()
                updateUi()
            }
        bottomsheetHistoryFilter.show(activity!!.supportFragmentManager, "filter")
    }

    private fun updateUi() {
        if (selectedFilter == null) {
            initPageData()
        } else {
            doRetriveModel().getTransactionRequest().type = selectedFilter!!.type
            doRetriveModel().getTransactionRequest().start = page.toString()
            presenter.getTransactionHistory()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun doRetriveModel(): TransactionFragmentModel {
        return model
    }

    override fun onTxnHistory() {
        if (doRetriveModel().getDomain().transactionHistoryResponse.statuscode == Constants.ApiResponse.RES_SUCCESS) {
            arrayListHistory.addAll(doRetriveModel().getDomain().transactionHistoryResponse.data)
            historyAdapter.setHistiry(arrayListHistory)
            if (doRetriveModel().getDomain().transactionHistoryResponse.data.size >= limit) {
                page += 1
                loading = true
            } else {
                loading = false
            }
        } else {
            arrayListHistory.clear()
            historyAdapter.setHistiry(arrayListHistory)
            page = 0
            loading = false
        }

        if (arrayListHistory.isEmpty()) {
            recyclerviewHistory.visibility =View.GONE
            groupNoTxn.visibility = View.VISIBLE
        } else {
            recyclerviewHistory.visibility =View.VISIBLE
            groupNoTxn.visibility = View.GONE
        }
    }

    override fun showProgress() {
        Log.e(TAG,"showProgress")
        groupNoTxn.visibility = View.GONE
        if (arrayListHistory.isNotEmpty()){
            recyclerviewHistory.visibility =View.VISIBLE
        }else{
            recyclerviewHistory.visibility =View.GONE
        }
        progressHistory.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        Log.e(TAG,"hideProgress")
        recyclerviewHistory.visibility =View.VISIBLE
        progressHistory.visibility = View.GONE
    }

}
