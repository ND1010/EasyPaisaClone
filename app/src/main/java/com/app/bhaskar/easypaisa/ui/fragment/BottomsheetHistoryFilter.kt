/**
 *Created by dhruv on 04-09-2019.
 */
package aepsapp.easypay.com.aepsandroid.fragments

import aepsapp.easypay.com.aepsandroid.adapters.TransactionFilterAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.response_model.TransactionFilterDataResponse
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_select_electricity_state.*

/**
 *Created by dhruv on 04-09-2019.
 */
class BottomsheetHistoryFilter(
    var mContext: Context,
    var arrayListboard: ArrayList<TransactionFilterDataResponse.History>,
    var itemClicked: (TransactionFilterDataResponse.History?) -> Unit) :
    BottomSheetDialogFragment() {

    private val TAG = "BottomsheetElecricityBoard"
    private lateinit var mView: View
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mAdaapterTxnFilter: TransactionFilterAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.dialog_select_electricity_state, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        tvStateLebel.text = "Select Transaction Type"
        tvClear.visibility = View.VISIBLE
        mLayoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
        mAdaapterTxnFilter = TransactionFilterAdapter(mContext, arrayListboard) {
            itemClicked(it)
            dismiss()
        }
        recyclerviewState.layoutManager = mLayoutManager
        recyclerviewState.setHasFixedSize(true)
        recyclerviewState.adapter = mAdaapterTxnFilter

        tvClear.setOnClickListener {
            dismiss()
            itemClicked(null)
        }
    }

}