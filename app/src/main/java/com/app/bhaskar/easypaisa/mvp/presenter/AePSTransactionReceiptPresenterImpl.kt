package com.app.bhaskar.easypaisa.mvp.presenter

import android.content.Intent
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.repositories.EasyPaisaRepository
import com.app.bhaskar.easypaisa.request_model.AepsRequest
import com.app.bhaskar.easypaisa.ui.activity.CaptureFingerActivity
import com.app.bhaskar.easypaisa.utils.Constants
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class AePSTransactionReceiptPresenterImpl(val view: AepsTransactionReceiptPresenter.AepsTransactionReceiptView) :
    AepsTransactionReceiptPresenter {

    private val mEventBus = EasyPaisaApp.getDefault()
    @Inject
    lateinit var repository: EasyPaisaRepository


    override fun onError(message: String) {
    }

    override fun registerBus() {
        mEventBus.register(this)
    }

    override fun unRegisterBus() {
        mEventBus.unregister(this)
    }

}