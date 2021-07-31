package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.repositories.EasyPaisaRepository
import com.app.bhaskar.easypaisa.request_model.MobileRechargePlanRequest
import com.app.bhaskar.easypaisa.utils.Utils
import javax.inject.Inject

class MobileRechargePlanPresenterImpl(val view: MobileRechargePlansPresenter.MobileRechargePlansView) : MobileRechargePlansPresenter{


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

    override fun getPlans(req: MobileRechargePlanRequest) {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress()
            repository.apiRechargePlan(req, {
                view.hideProgress()
                view.doRetriveModel().getDomain().mobileRechargePlanResponse= it
                view.onMobilePlan()
            }, {
                view.hideProgress()
                if (it?.message != null) {
                    view.showError(it.message!!)
                }
            })
        }else{
            view.showError(view.getViewActivity().getString(R.string.no_internet_message))
        }
    }
}