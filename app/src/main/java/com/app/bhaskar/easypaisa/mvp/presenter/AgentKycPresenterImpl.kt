package com.app.bhaskar.easypaisa.mvp.presenter

import android.Manifest
import android.view.View
import androidx.core.app.ActivityCompat
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.repositories.EasyPaisaRepository
import com.app.bhaskar.easypaisa.repositories.RepositoryImpl
import com.app.bhaskar.easypaisa.repositories.VerifyPancardRequest
import com.app.bhaskar.easypaisa.request_model.GenerateTokenFaceRequest
import com.app.bhaskar.easypaisa.request_model.OkycUserDataRequest
import com.app.bhaskar.easypaisa.response_model.AgentKycResponse
import com.app.bhaskar.easypaisa.restapi.RestApi
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.app.bhaskar.easypaisa.utils.Utils.Companion.PERMISSIONS
import javax.inject.Inject

class AgentKycPresenterImpl(val view: AgentKycPresenter.AgentKycView) : AgentKycPresenter{

    private val mEventBus = EasyPaisaApp.getDefault()
    @Inject
    lateinit var repository: EasyPaisaRepository
    var repositoryKarza: EasyPaisaRepository =
        RepositoryImpl(EasyPaisaApp.getDB().metOfficeDataDao(),EasyPaisaApp.provideRetrofit().create(    RestApi::class.java))


    override fun onError(message: String) {
    }

    override fun registerBus() {
        mEventBus.register(this)
    }

    override fun unRegisterBus() {
        mEventBus.unregister(this)
    }

    override fun requestForReadWritePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(view.getViewActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Utils.showAlert(view.getViewActivity(), view.getViewActivity().getString(R.string.provide_permission_storage), "Storage Permission", View.OnClickListener {
                //Yes
                ActivityCompat.requestPermissions(view.getViewActivity(),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), Constants.UI.MY_PERMISSIONS_REQUEST_STORAGE_READ)
            })
        } else {
            ActivityCompat.requestPermissions(view.getViewActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), Constants.UI.MY_PERMISSIONS_REQUEST_STORAGE_READ)
        }
    }

    override fun requestForCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(view.getViewActivity(), Manifest.permission.CAMERA)
            ||ActivityCompat.shouldShowRequestPermissionRationale(view.getViewActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Utils.showAlert(view.getViewActivity(), view.getViewActivity().getString(R.string.provide_permission_camera), "Camera Permission", View.OnClickListener {
                //Yes
                ActivityCompat.requestPermissions(view.getViewActivity(), PERMISSIONS, Constants.UI.MY_PERMISSIONS_REQUEST_CAMERA)
            }, View.OnClickListener {
                //No
            })
        } else {
            ActivityCompat.requestPermissions(view.getViewActivity(), PERMISSIONS,Constants.UI.MY_PERMISSIONS_REQUEST_CAMERA)
        }
    }

    override fun doAgentKyc() {
        val req = view.doRetriveModel().getAgentKycRequest()
        if (Utils.isNetworkConnected(view.getViewActivity())){
            view.showProgress()
            val successHandler: (AgentKycResponse) -> Unit = {
                view.doRetriveModel().getLoginDomain().agentKuycResponse = it
                view.hideProgress()
                view.agnetKycDone()
            }
            repository.doAgentKyc(req, successHandler,{
                view.hideProgress()
                if (it?.message != null) {
                    view.showError(it.message!!)
                }
            })
        }else{
            view.showError(view.getViewActivity().getString(R.string.no_internet_message))
        }
    }

    override fun doGetKarzaToken() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            Utils.showProgressDialog(view.getViewActivity(),"Please wait for while...")
            val generateTokenFaceRequest = GenerateTokenFaceRequest()
            generateTokenFaceRequest.productId.add("aadhaar_xml")
            repositoryKarza.apiKarzaToken(generateTokenFaceRequest, {
                Utils.hideProgressDialog()
                view.onKarzaToken(it)
            }, {
                Utils.hideProgressDialog()
                if (it?.message != null) {
                    view.showError(it.message!!)
                }
            })
        }
    }

    override fun doSendOkycUserAadharData(okycUserDataRequest: OkycUserDataRequest) {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            Utils.showProgressDialog(view.getViewActivity(),"Please wait for while...")
            repositoryKarza.apiOkyUserAadharData(okycUserDataRequest, {
                Utils.hideProgressDialog()
                view.onOkycAdded(it)
            }, {
                Utils.hideProgressDialog()
                if (it?.message != null) {
                    view.showError(it.message!!)
                }
            })
        }
    }

    override fun verifyPanCard(panNumber: String) {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            Utils.showProgressDialog(view.getViewActivity(),"Verifying Pan number...")
            val verifyPancardReq = VerifyPancardRequest()
            verifyPancardReq.userId= EasyPaisaApp.getLoggedInUser()?.id.toString()
            verifyPancardReq.pan= panNumber
            verifyPancardReq.token= EasyPaisaApp.getLoggedInUser()?.apptoken.toString()
            repository.apiVerifyPan(verifyPancardReq, {
                Utils.hideProgressDialog()
                view.onVerifyToken(it)
            }, {
                Utils.hideProgressDialog()
                if (it?.message != null) {
                    view.showError(it.message!!)
                }
            })
        }
    }
}