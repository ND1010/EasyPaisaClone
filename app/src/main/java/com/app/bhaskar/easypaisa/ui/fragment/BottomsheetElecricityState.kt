/**
 *Created by dhruv on 04-09-2019.
 */
package aepsapp.easypay.com.aepsandroid.fragments

import aepsapp.easypay.com.aepsandroid.adapters.ElStateAdapter
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.response_model.EelectricityStateResponse
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.dialog_select_electricity_state.*
import java.util.*
import kotlin.collections.ArrayList

/**
 *Created by dhruv on 04-09-2019.
 */
class BottomsheetElecricityState(
    var mContext: Context,
    var arrayListStates: ArrayList<EelectricityStateResponse.Message.State>,
    var itemClicked: (EelectricityStateResponse.Message.State) -> Unit
) :
    BottomSheetDialogFragment() {

    private val TAG = "BottomsheetElecricityState"
    private lateinit var mView: View
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mAdaapterState: ElStateAdapter


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

    private fun initViews() {
        mLayoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
        mAdaapterState = ElStateAdapter(mContext, arrayListStates) {
            itemClicked(it)
            dismiss()
        }
        recyclerviewState.layoutManager = mLayoutManager
        recyclerviewState.setHasFixedSize(true)
        recyclerviewState.adapter = mAdaapterState
    }

}