/**
 *Created by dhruv on 04-09-2019.
 */
package aepsapp.easypay.com.aepsandroid.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.request_model.SearchRemitter
import com.app.bhaskar.easypaisa.ui.activity.RegisterRemitterActivity
import com.app.bhaskar.easypaisa.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_dmt_search.view.*

/**
 *Created by dhruv on 04-09-2019.
 */
class BottomDmtUserSearchFragment(
    var mContext: Context,var searchRemitter: (SearchRemitter) -> Unit) :
    BottomSheetDialogFragment() {

    private val TAG = "BottomDmtUserSearchFragment"
    private lateinit var mView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.dialog_dmt_search, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        mView.btn_search.setOnClickListener {
            if (mView.textInputLayoutDmtMobile.editText!!.text.toString().trim().isEmpty()){
                mView.textInputLayoutDmtMobile.error = "Enter mobile number"
            }else if(mView.textInputLayoutDmtMobile.editText!!.text.toString().trim().isNotEmpty()
                && mView.textInputLayoutDmtMobile.editText!!.text.toString().trim().length !=10){
                mView.textInputLayoutDmtMobile.error = "Invalid mobile number"
            }else{
                mView.textInputLayoutDmtMobile.isErrorEnabled =false
                val searchRemitter = SearchRemitter(
                    mobile = mView.textInputLayoutDmtMobile.editText!!.text.toString().trim()
                    ,token = EasyPaisaApp.getLoggedInUser()?.apptoken!!,userId = EasyPaisaApp.getLoggedInUser()?.id!!.toString())
                searchRemitter(searchRemitter)
            }
        }

        mView.tvDontHavAccountRegisterHere.setOnClickListener {
            if (mView.textInputLayoutDmtMobile.editText!!.text.toString().trim().isEmpty()){
                mView.textInputLayoutDmtMobile.error = "Enter mobile number"
            }else if(mView.textInputLayoutDmtMobile.editText!!.text.toString().trim().isNotEmpty()
                && mView.textInputLayoutDmtMobile.editText!!.text.toString().trim().length !=10){
                mView.textInputLayoutDmtMobile.error = "Invalid mobile number"
            }else{
                mContext.startActivity(Intent(mContext, RegisterRemitterActivity::class.java).putExtra(
                    Constants.UI.MOBILE_NO,mView.textInputLayoutDmtMobile.editText!!.text.toString().trim()))
            }
        }
    }

}