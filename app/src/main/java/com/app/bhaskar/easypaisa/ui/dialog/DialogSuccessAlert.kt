package com.app.bhaskar.easypaisa.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.app.bhaskar.easypaisa.R

/**
 * Created by Viral on 12-10-2017.
 */

class DialogSuccessAlert(mContext: Context) : Dialog(mContext) {

    private var btnCancel: Button? = null
    private var btnOk: Button? = null
    private val view: View

    init {
        val inflater = LayoutInflater.from(mContext)
        view = inflater.inflate(R.layout.dialog_success, null)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        super.setContentView(view)
    }


    fun setMessage(message: String): DialogSuccessAlert {
        val txtMessage = view.findViewById(R.id.tvMessage) as TextView
        txtMessage.text = message
        return this
    }
    fun dismissDialog() {
        if (isShowing){
            dismiss()
        }
    }

    fun setMessageOnlyDialog(message: String): DialogSuccessAlert{
        btnOk = view.findViewById(R.id.tvOk) as Button
        val txtMessage = view.findViewById(R.id.tvMessage) as TextView
        txtMessage.setText(message)
        btnCancel = view.findViewById(R.id.tvCancel) as Button
        btnOk!!.visibility = View.GONE
        btnCancel!!.visibility = View.GONE
        return this
    }

    fun setPositiveButton(btnText: String, yesClick: View.OnClickListener): DialogSuccessAlert {
        btnOk = view.findViewById(R.id.tvOk) as Button
        btnCancel = view.findViewById(R.id.tvCancel) as Button
        btnOk!!.visibility = View.VISIBLE
        btnCancel!!.visibility = View.GONE
        btnOk!!.text = btnText
        btnOk!!.setOnClickListener { v ->
            yesClick.onClick(v)
            dismiss()
        }
        return this
    }

    fun setPositiveButton(): DialogSuccessAlert {
        btnOk = view.findViewById(R.id.tvOk) as Button
        btnCancel = view.findViewById(R.id.tvCancel) as Button
        btnOk!!.visibility = View.GONE
        btnCancel!!.visibility = View.GONE
        return this
    }

    fun setNegativeButton(btnText: String, noClick: View.OnClickListener): DialogSuccessAlert {
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
