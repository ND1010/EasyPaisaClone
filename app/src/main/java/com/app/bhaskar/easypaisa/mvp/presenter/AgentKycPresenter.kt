package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.mvp.model.AgentKycActivityModel
import com.app.bhaskar.easypaisa.request_model.OkycUserDataRequest
import com.app.bhaskar.easypaisa.response_model.GenerateTokenFaceResponse
import com.app.bhaskar.easypaisa.response_model.RegOtpResponse
import com.app.bhaskar.easypaisa.response_model.VerifyPancardResponse
import com.pa.baseframework.baseview.BasePresenter
import com.pa.baseframework.baseview.BaseView


interface AgentKycPresenter : BasePresenter {
    fun requestForCameraPermission()
    fun requestForReadWritePermission()
    fun doAgentKyc()
    fun doGetKarzaToken()
    fun doSendOkycUserAadharData(okycUserDataRequest: OkycUserDataRequest)
    fun verifyPanCard(panNumber: String)

    interface AgentKycView : BaseView {
        fun doRetriveModel(): AgentKycActivityModel
        fun agnetKycDone()
        fun onKarzaToken(it: GenerateTokenFaceResponse)
        fun onOkycAdded(it: RegOtpResponse)
        fun onVerifyToken(it: VerifyPancardResponse)
    }
}