package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.repositories.EasyPaisaRepository
import javax.inject.Inject

class SelectbankActivityPresenterImpl(val view: SelectBankPresenter.SelectBankView) : SelectBankPresenter{

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