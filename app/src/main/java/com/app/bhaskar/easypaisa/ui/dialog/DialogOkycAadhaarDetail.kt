package com.app.bhaskar.easypaisa.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Base64
import android.view.Window
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.request_model.OkycUserDataRequest
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.raw_board.view.*
import okhttp3.internal.wait

/**
 * Created by Viral on 12-10-2017.
 */

class DialogOkycAadhaarDetail(
    val mContext: Context,
    var okycUserDataRequest: OkycUserDataRequest?
) : Dialog(mContext) {

    private lateinit var ivUserProfile: AppCompatImageView
    private lateinit var tvName: TextView
    private lateinit var tvGender: TextView
    private lateinit var tvAadhaarNumber: TextView
    private lateinit var tvAddress: TextView
    private lateinit var tvMobile: TextView
    private lateinit var btnNo: AppCompatButton
    private lateinit var btnYes: AppCompatButton


    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.setContentView(R.layout.dialog_okyc_aadhaar_detail)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
    }

    fun showMicroAtmTransactionDialog(onclickAction: (Int) -> Unit) {
        ivUserProfile = findViewById(R.id.ivUserProfile)
        tvName = findViewById(R.id.tvName)
        tvGender = findViewById(R.id.tvGender)
        tvAddress = findViewById(R.id.tvAddress)
        tvAadhaarNumber = findViewById(R.id.tvAadhaarNumber)
        tvMobile = findViewById(R.id.tvMobile)
        btnNo = findViewById(R.id.btnNo)
        btnYes = findViewById(R.id.btnYes)

        val imageByteArray: ByteArray = Base64.decode(okycUserDataRequest?.data!!.imagebase64, Base64.DEFAULT)
        Glide.with(mContext)
            .load(imageByteArray)
            .into(ivUserProfile)
        tvName.text = okycUserDataRequest?.data!!.name
        tvGender.text = okycUserDataRequest?.data!!.gender
        tvAddress.text = okycUserDataRequest?.data!!.address
        tvAadhaarNumber.text = okycUserDataRequest?.data!!.aadhaarNumber

        btnYes.setOnClickListener {
            onclickAction(1)
            dismiss()
        }
        btnNo.setOnClickListener {
            onclickAction(0)
            dismiss()
        }

        this.show()
    }

}
