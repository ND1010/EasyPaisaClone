/**
 *Created by dhruv on 04-09-2019.
 */
package aepsapp.easypay.com.aepsandroid.fragments

import aepsapp.easypay.com.aepsandroid.adapters.RechargeBoardAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.response_model.RechargeProviderResponse
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_select_electricity_state.*

/**
 *Created by dhruv on 04-09-2019.
 */
class BottomsheetRechargeProider(
    var mContext: Context,
    var arrayListboard: ArrayList<RechargeProviderResponse.Message.Provider>,
    var itemClicked: (RechargeProviderResponse.Message.Provider) -> Unit
) :
    BottomSheetDialogFragment() {

    private val TAG = "BottomsheetRechargeProider"
    private lateinit var mView: View
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mAdaapterBoard: RechargeBoardAdapter


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
        tvStateLebel.text = "Select Your Board/Operator"
        mLayoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
        mAdaapterBoard = RechargeBoardAdapter(mContext, arrayListboard) {
            itemClicked(it)
            dismiss()
        }
        recyclerviewState.layoutManager = mLayoutManager
        recyclerviewState.setHasFixedSize(true)
        recyclerviewState.adapter = mAdaapterBoard
    }

}