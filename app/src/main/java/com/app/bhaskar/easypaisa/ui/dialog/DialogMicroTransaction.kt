package com.app.bhaskar.easypaisa.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.utils.Constants
import kotlinx.android.synthetic.main.dialog_microatm_transaction.*

/**
 * Created by Viral on 12-10-2017.
 */

class DialogMicroTransaction(mContext: Context) : Dialog(mContext) {

    private lateinit var ivAtmWithdrawal: LinearLayout
    private lateinit var ivAtmBalance: LinearLayout
    private lateinit var ivAtmDeposit: LinearLayout
    private lateinit var ivAtmMiniStatement: LinearLayout
    private lateinit var ivAtmResetPin: LinearLayout
    private lateinit var ivAtmChangePin: LinearLayout

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.setContentView(R.layout.dialog_microatm_transaction)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
    }

    fun showMicroAtmTransactionDialog(onclickAction: (Int) -> Unit) {
        ivAtmWithdrawal = findViewById(R.id.ivAtmWithdrawal)
        ivAtmBalance = findViewById(R.id.ivAtmBalance)
        ivAtmDeposit = findViewById(R.id.ivAtmDeposit)
        ivAtmMiniStatement = findViewById(R.id.ivAtmMiniStatement)
        ivAtmResetPin = findViewById(R.id.ivAtmResetPin)
        ivAtmChangePin = findViewById(R.id.ivAtmChangePin)

        ivAtmWithdrawal.setOnClickListener {
            onclickAction(Constants.MicroAtm.MICRO_WITHDRWAL)
            dismiss()
        }
        ivAtmBalance.setOnClickListener {
            onclickAction(Constants.MicroAtm.MICRO_BALANCE)
            dismiss()
        }
        ivAtmDeposit.setOnClickListener {
            onclickAction(Constants.MicroAtm.MICRO_DEPOSIT)
            dismiss()
        }
        ivAtmMiniStatement.setOnClickListener {
            onclickAction(Constants.MicroAtm.MICRO_MINI_STATEMENT)
            dismiss()
        }
        ivAtmResetPin.setOnClickListener {
            onclickAction(Constants.MicroAtm.MICRO_RESET_PIN)
            dismiss()
        }
        ivAtmChangePin.setOnClickListener {
            onclickAction(Constants.MicroAtm.MICRO_CHANGE_PIN)
            dismiss()
        }
        this.show()
    }

}
