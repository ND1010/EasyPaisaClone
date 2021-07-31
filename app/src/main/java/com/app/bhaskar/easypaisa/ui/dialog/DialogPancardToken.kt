package com.app.bhaskar.easypaisa.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.utils.Constants

/**
 * Created by Viral on 12-10-2017.
 */

class DialogPancardToken(mContext: Context) : Dialog(mContext) {

    private var btnCancel: Button? = null
    private var btnOk: Button? = null
    private var ivImgStatus: ImageView? = null
    private val view: View

    init {
        val inflater = LayoutInflater.from(mContext)
        view = inflater.inflate(R.layout.dialog_pancard_token, null)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        super.setContentView(view)
    }


    fun setMessage(status:String,message: String): DialogPancardToken {
        val txtMessage = view.findViewById(R.id.tvMessage) as TextView
        txtMessage.text = message
        if (status  == Constants.ApiResponse.RES_SUCCESS){
            ivImgStatus?.setImageResource(R.drawable.ic_success_tnx)
        }else{
            ivImgStatus?.setImageResource(R.drawable.ic_failed_txn)
        }
        return this
    }


    fun setPositiveButton(btnText: String, yesClick: View.OnClickListener): DialogPancardToken {
        btnOk = view.findViewById(R.id.tvOk) as Button
        btnOk!!.visibility = View.VISIBLE
        btnOk!!.text = btnText
        btnOk!!.setOnClickListener { v ->
            yesClick.onClick(v)
            dismiss()
        }
        //dismiss();
        return this
    }

    fun setNegativeButton(btnText: String, noClick: View.OnClickListener): DialogPancardToken {
        btnCancel = view.findViewById(R.id.tvCancel) as Button
        btnCancel!!.visibility = View.VISIBLE
        btnCancel!!.text = btnText
        btnCancel!!.setOnClickListener { v ->
            noClick.onClick(v)
            dismiss()
        }
        //dismiss();
        return this
    }

}
