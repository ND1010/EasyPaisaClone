package com.app.bhaskar.easypaisa.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.CountDownTimer
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.request_model.OtpVerificationRequest
import com.google.android.material.textfield.TextInputLayout
import java.util.concurrent.TimeUnit


/**
 * Created by Viral on 12-10-2017.
 */

class DialogOtpVerification(
    mContext: Context
) : Dialog(mContext) {

    private var timer: CountDownTimer? = null
    private var textInputLayoutOtp: TextInputLayout
    private var btn_submit: AppCompatButton
    private var btn_cancel: AppCompatButton
    private var linearRemainingTime: LinearLayout
    private var linearResend: LinearLayout
    private var tvTimeLeft: TextView
    private var tvResend: TextView
    private val OTP_TIME:Long = 1000 * 60 * 3

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.setContentView(R.layout.dialog_otp_verification)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )

        setCancelable(false)
        setCanceledOnTouchOutside(false)

        textInputLayoutOtp = findViewById(R.id.textInputLayoutOtp)
        btn_submit = findViewById(R.id.btn_submit)
        linearRemainingTime = findViewById(R.id.linearRemainingTime)
        linearResend = findViewById(R.id.linearResend)
        tvTimeLeft = findViewById(R.id.tvTimeLeft)
        tvResend = findViewById(R.id.tvResend)
        btn_cancel = findViewById(R.id.btn_cancel)
    }

    fun showOTPDialog(
        onclickProceed: (String) -> Unit,
        onclickResend: () -> Unit,
        cancelCick: () -> Unit
    ) {
        countDownTimer()
        btn_cancel.setOnClickListener {
            dismiss()
            killTimer()
            cancelCick()
        }
        btn_submit.setOnClickListener {
            when {
                textInputLayoutOtp.editText!!.text.toString().trim().isEmpty() -> {
                    textInputLayoutOtp.error = "Enter OTP"
                }
                textInputLayoutOtp.editText!!.text.toString().trim().length < 4 -> {
                    textInputLayoutOtp.error = "Invalid OTP"
                }
                else -> {
                    onclickProceed(textInputLayoutOtp.editText!!.text.toString().trim())
                }
            }
        }

        tvResend.setOnClickListener {
            onclickResend()
        }
        this.show()
    }

    fun countDownTimer() {
        linearRemainingTime.visibility = View.VISIBLE
        linearResend.visibility = View.GONE

        timer = object : CountDownTimer(OTP_TIME, 1000) {
            // adjust the milli seconds here
            override fun onTick(millisUntilFinished: Long) {
                tvTimeLeft.text = String.format(
                    "%d : %d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(
                                    millisUntilFinished
                                )
                            )
                )
            }

            override fun onFinish() {
                killTimer()
                linearRemainingTime.visibility = View.GONE
                linearResend.visibility = View.VISIBLE
            }
        }.start()
    }

    fun killTimer() {
        if (timer != null) {
            timer?.cancel()
            timer = null
        }
    }
}
