package com.app.bhaskar.easypaisa.repositories

import android.annotation.SuppressLint
import com.app.bhaskar.easypaisa.PanTokenResponse
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.model.APIError
import com.app.bhaskar.easypaisa.mvp.domain.LoadWalletUpiResponse
import com.app.bhaskar.easypaisa.request_model.*
import com.app.bhaskar.easypaisa.response_model.*
import com.app.bhaskar.easypaisa.restapi.RestApi
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.pa.models.dao.ResultDataDao
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.newThread
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.bouncycastle.jce.provider.symmetric.Grainv1
import java.io.File

@SuppressLint("CheckResult")
class RepositoryImpl(val localSource: ResultDataDao, val mRestApi: RestApi) : EasyPaisaRepository {

    private val mEventBus = EasyPaisaApp.getDefault()

    override fun apiLoginToApp(
        request: LoginRequest,
        successHandler: (LoginResponse?) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<LoginResponse> =
            mRestApi.doApiForLogin(Utils.getRequest(EasyPaisaApp.getGson().toJson(request)))
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    @SuppressLint("CheckResult")
    override fun apiPasswordResetnToken(
        request: PasswordResetToeknRequest,
        successHandler: (PasswordResetTokenResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<PasswordResetTokenResponse> =
            mRestApi.doApiForPassResetToken(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(
                        request
                    )
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    @SuppressLint("CheckResult")
    override fun apiUpdatePassword(
        request: UpdatePasswordRequest,
        successHandler: (UpdatePasswordResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<UpdatePasswordResponse> =
            mRestApi.doApiForUpdatePassword(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(
                        request
                    )
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }


    @SuppressLint("CheckResult")
    override fun apiWalletBalance(
        request: WalletBalanceRequest,
        successHandler: (WalletBalanceResponse?) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<WalletBalanceResponse> =
            mRestApi.doApiForWalletBalance(
                Utils.getRequest(
                    EasyPaisaApp.getGsonWithExpose().toJson(
                        request
                    )
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    @SuppressLint("CheckResult")
    override fun apiUserReqData(
        request: UserRequiredDataRequest,
        succressHandler: (UserRequiredDataResponse) -> Unit,
        failerHansler: (Throwable?) -> Unit
    ) {
        val request: Flowable<UserRequiredDataResponse> =
            mRestApi.doApiForUserRequiredData(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(
                        request
                    )
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    if (it.status == Constants.ApiResponse.RES_UNAUTHORISED) {
                        val apiError = APIError(
                            501,
                            "${it.message}, Please login again to access the services."
                        )
                        mEventBus.postSticky(apiError)
                        return@subscribe
                    }
                    succressHandler(it)
                }, { error ->
                    failerHansler(error)
                }
            )
    }

    @SuppressLint("CheckResult")
    override fun doAgentKyc(
        request: AgentKycRequest,
        successHandler: (AgentKycResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val token =
            EasyPaisaApp.getLoggedInUser()!!.apptoken.toRequestBody("text/plain".toMediaTypeOrNull())
        val user_id =
            EasyPaisaApp.getLoggedInUser()!!.id.toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())
        val merchantAddress =
            request.merchantAddress.toRequestBody("text/plain".toMediaTypeOrNull())
        val merchantCityName =
            request.merchantCityName.toRequestBody("text/plain".toMediaTypeOrNull())
        val merchantState =
            request.merchantState.toRequestBody("text/plain".toMediaTypeOrNull())
        val userPan =
            request.userPan.toRequestBody("text/plain".toMediaTypeOrNull())
        val merchantPinCode =
            request.merchantPinCode.toRequestBody("text/plain".toMediaTypeOrNull())
        val merchantAadhar =
            request.merchantAadhar.toRequestBody("text/plain".toMediaTypeOrNull())
        val latitude =
            request.latitude.toRequestBody("text/plain".toMediaTypeOrNull())
        val longitude =
            request.longitude.toRequestBody("text/plain".toMediaTypeOrNull())

        val params = HashMap<String, RequestBody>()
        params["user_id"] = user_id
        params["token"] = token
        params["address"] = merchantAddress
        params["city"] = merchantCityName
        params["state"] = merchantState
        params["aadharcard"] = merchantAadhar
        params["pancard"] = userPan
        params["pincode"] = merchantPinCode
        params["latitude"] = latitude
        params["longitude"] = longitude

        var bodyUserSelfieImage: MultipartBody.Part? = null
        var bodyPanImage: MultipartBody.Part? = null
        var bodyAadhaarImage: MultipartBody.Part? = null
        var bodyAadhaarImageBack: MultipartBody.Part? = null

        val fileUserSelfie = File(request.userSelfieImages)
        val fbodyUserSelfie = fileUserSelfie.asRequestBody("*/*".toMediaTypeOrNull())
        params["userSelfieImages"] = fbodyUserSelfie
        bodyUserSelfieImage = MultipartBody.Part.createFormData(
            "userSelfieImages",
            fileUserSelfie.name,
            fbodyUserSelfie
        )

        val filePan = File(request.pancardPics)
        val fbodyPan = filePan.asRequestBody("*/*".toMediaTypeOrNull())
        params["pancardpics"] = fbodyPan
        bodyPanImage = MultipartBody.Part.createFormData("pancardpics", filePan.name, fbodyPan)

        val fileAadhaar = File(request.aadharPics)
        val fbodyAadhaar = fileAadhaar.asRequestBody("*/*".toMediaTypeOrNull())
        params["aadharcardpics"] = fbodyAadhaar
        bodyAadhaarImage =
            MultipartBody.Part.createFormData("aadharcardpics", fileAadhaar.name, fbodyAadhaar)

        val fileAadhaarBack = File(request.aadharcardbackpics)
        val fbodyAadhaarBack = fileAadhaarBack.asRequestBody("*/*".toMediaTypeOrNull())
        params["aadharcardbackpics"] = fbodyAadhaarBack
        bodyAadhaarImageBack =
            MultipartBody.Part.createFormData(
                "aadharcardbackpics",
                fileAadhaarBack.name,
                fbodyAadhaarBack
            )

        val request: Flowable<AgentKycResponse> =
            mRestApi.doApiForAgentKyc(
                params,
                bodyPanImage,
                bodyAadhaarImage,
                bodyAadhaarImageBack,
                bodyUserSelfieImage
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiFingPayICICIEasyPayTransaction(
        request: FingPayICICIAepsTransactionRequest,
        successHandler: (FingPayAepsTxnResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<FingPayAepsTxnResponse> =
            mRestApi.doApiFingPayAepsTransactions(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(
                        request
                    ))
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    @SuppressLint("CheckResult")
    override fun apiFingPayICICITransaction(
        request: FingPayICICIAepsTransactionRequest,
        successHandler: (FingPayAepsTxnResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<FingPayAepsTxnResponse> =
            mRestApi.doApiFingPayAepsTransactions(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(
                        request
                    )
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    @SuppressLint("CheckResult")
    override fun apiRechargePlan(
        request: MobileRechargePlanRequest,
        successHandler: (MobileRechargePlanResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<MobileRechargePlanResponse> =
            mRestApi.doApiGetMobilePlan(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(
                        request
                    )
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }


    @SuppressLint("CheckResult")
    override fun apiRechargeProviders(
        request: GetMobileRechargeProviderRequest,
        successHandler: (RechargeProviderResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<RechargeProviderResponse> =
            mRestApi.doApiGetMobileOperators(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(
                        request
                    )
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    @SuppressLint("CheckResult")
    override fun apiOperatorFromMobile(
        request: GetMobileOperatorFromMobile,
        successHandler: (MobileOperatorFromMobile) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<MobileOperatorFromMobile> =
            mRestApi.doApiGetMobileOperatorFromMobile(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(
                        request
                    )
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    @SuppressLint("CheckResult")
    override fun apiMobileRecharge(
        request: MobileRechargeRequest,
        successHandler: (MobileRechargeResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<MobileRechargeResponse> =
            mRestApi.doApiMobileRecharge(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(
                        request
                    )
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }


    @SuppressLint("CheckResult")
    override fun apiElectricityState(
        request: ElectricityStateRequest,
        successHandler: (EelectricityStateResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<EelectricityStateResponse> =
            mRestApi.doApiElectricityState(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(
                        request
                    )
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    @SuppressLint("CheckResult")
    override fun apiElectricityBoard(
        request: ElectricityBoardRequest,
        successHandler: (ElectricityBoardResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<ElectricityBoardResponse> =
            mRestApi.doApiElectricityBoard(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(
                        request
                    )
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    @SuppressLint("CheckResult")
    override fun apiFetchElBillDetails(
        request: FetchElBillDetailRequest,
        successHandler: (FetchElBillDetailResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<FetchElBillDetailResponse> =
            mRestApi.doApiFetchBillDetail(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(request)
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    @SuppressLint("CheckResult")
    override fun apiPayElectricityBill(
        request: ElectricityBillPaymentRequest,
        successHandler: (ElectricityBillPaymentResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<ElectricityBillPaymentResponse> =
            mRestApi.doApiElectricityPayBill(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(request)
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiRechargeDTH(
        request: ElectricityBillPaymentRequest,
        successHandler: (ElectricityBillPaymentResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<ElectricityBillPaymentResponse> =
            mRestApi.doApiDTHRecharge(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(request)
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    @SuppressLint("CheckResult")
    override fun apiGetPancardData(
        request: PancardDataRequest,
        successHandler: (AgentPancardResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<AgentPancardResponse> =
            mRestApi.doApiPanCardDetail(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(request)
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    @SuppressLint("CheckResult")
    override fun apiUtiRegistration(
        request: UtiRegisterRequest,
        successHandler: (UtiRegistrationResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<UtiRegistrationResponse> =
            mRestApi.doApiUtiReg(Utils.getRequest(EasyPaisaApp.getGson().toJson(request)))
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    @SuppressLint("CheckResult")
    override fun apiGetTokenForPancard(
        request: PanTokenRequest,
        successHandler: (PanTokenResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<PanTokenResponse> =
            mRestApi.doApiPanToken(Utils.getRequest(EasyPaisaApp.getGson().toJson(request)))
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    @SuppressLint("CheckResult")
    override fun apiResetPsaId(
        request: UtiPanTokenResetRequest,
        successHandler: (UtiResetPsaIDResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<UtiResetPsaIDResponse> =
            mRestApi.doApiResetPsaId(Utils.getRequest(EasyPaisaApp.getGson().toJson(request)))
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    @SuppressLint("CheckResult")
    override fun apiChangePassword(
        request: ChangePasswordRequest,
        successHandler: (UpdatePasswordResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<UpdatePasswordResponse> =
            mRestApi.doApiUpdatePass(Utils.getRequest(EasyPaisaApp.getGson().toJson(request)))
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }


    override fun apiAepsTxnData(
        request: AepsTransactionRequest,
        successHandler: (AepsTnxDataResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<AepsTnxDataResponse> =
            mRestApi.doApiAepTxn(Utils.getRequest(EasyPaisaApp.getGson().toJson(request)))
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun doRegisterUser(
        request: RegistrationRequest,
        successHandler: (UserRegistrationResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<UserRegistrationResponse> =
            mRestApi.doApiRegistration(Utils.getRequest(EasyPaisaApp.getGson().toJson(request)))
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun doGetTxnHistory(
        request: TransactionHistoryRequest,
        successHandler: (TransactionResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<TransactionResponse> =
            mRestApi.doApiTxnHistory(Utils.getRequest(EasyPaisaApp.getGson().toJson(request)))
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiWalletLoadReqData(
        request: WalletRequiredDataRequest,
        successHandler: (WalletRequiredDataResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<WalletRequiredDataResponse> =
            mRestApi.doGetWalletData(Utils.getRequest(EasyPaisaApp.getGson().toJson(request)))
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiWalletLoadReq(
        request: WalletLoadRequest,
        successHandler: (WalletLoadRequestResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {

        val request: Flowable<WalletLoadRequestResponse> =
            mRestApi.doLoadWallet(Utils.getRequest(EasyPaisaApp.getGson().toJson(request)))
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiMoveWalletBank(
        request: MoveToBankWalletRequest,
        successHandler: (WalletLoadRequestResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<WalletLoadRequestResponse> =
            mRestApi.doLoadWallet(Utils.getRequest(EasyPaisaApp.getGson().toJson(request)))
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiSearchRemitter(
        request: SearchRemitter,
        successHandler: (SearchRemitterResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<SearchRemitterResponse> =
            mRestApi.doSearchRemitter(Utils.getRequest(EasyPaisaApp.getGson().toJson(request)))
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiDoDmtTransaction(
        request: DmtTransferRequest,
        successHandler: (DmtTransactionResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<DmtTransactionResponse> =
            mRestApi.doDmtTransaction(Utils.getRequest(EasyPaisaApp.getGson().toJson(request)))
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiAddNewBene(
        request: AddNewBeneficiaryRequest,
        successHandler: (AddNewBeneficiaryResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<AddNewBeneficiaryResponse> =
            mRestApi.doAddNewBene(Utils.getRequest(EasyPaisaApp.getGson().toJson(request)))
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiRegisterRemitter(
        request: RegisterRemmiterRequest,
        successHandler: (RemitterRegisterResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<RemitterRegisterResponse> =
            mRestApi.doRegisterRemmiter(Utils.getRequest(EasyPaisaApp.getGson().toJson(request)))
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiGenerateOtp(
        request: GenerateOtpRequest,
        successHandler: (GenerateOtpResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<GenerateOtpResponse> =
            mRestApi.doGenerateOtp(Utils.getRequest(EasyPaisaApp.getGson().toJson(request)))
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiVerifyRemitter(
        request: VerifyRemitterOtpRequest,
        successHandler: (VerifyRemitterResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<VerifyRemitterResponse> =
            mRestApi.doVerifyRemitter(Utils.getRequest(EasyPaisaApp.getGson().toJson(request)))
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiMicroAtmInit(
        request: MicroAtmInitTransactionRequest,
        successHandler: (MicroAtmInitTransactionResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<MicroAtmInitTransactionResponse> =
            mRestApi.doMicroAtmInit(Utils.getRequest(EasyPaisaApp.getGson().toJson(request)))
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiMicroAtmUpdate(
        request: MicroAtmUpdateRequest,
        successHandler: (MicroAtmUpdateResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<MicroAtmUpdateResponse> =
            mRestApi.doMicroAtmUpdate(Utils.getRequest(EasyPaisaApp.getGson().toJson(request)))
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiFingPayICICITransactionMiniStatement(
        request: FingPayICICIAepsTransactionRequest,
        successHandler: (FingpayMiniStatementResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<FingpayMiniStatementResponse> =
            mRestApi.doApiFingPayAepsMini(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(
                        request
                    )
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiAccountLeder(
        request: AccountLedgerRequest,
        successHandler: (AccountLedgerResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<AccountLedgerResponse> =
            mRestApi.doAccountLedger(Utils.getRequest(EasyPaisaApp.getGson().toJson(request)))
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiYesAepsTransaction(
        request: FingPayICICIAepsTransactionRequest,
        successHandler: (FingPayAepsTxnResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<FingPayAepsTxnResponse> =
            mRestApi.doApiYesAepsTransactions(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(
                        request
                    )
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiLogout(
        request: WalletBalanceRequest,
        successHandler: (LogoutResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<LogoutResponse> =
            mRestApi.doLgoutFromApp(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(
                        request
                    )
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun doGetOtp(
        request: RegisterOtpRequest,
        successHandler: (RegOtpResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<RegOtpResponse> =
            mRestApi.doGetOtp(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(
                        request
                    )
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiUserValidate(
        request: YesValidateUserRequest,
        successHandler: (YesValidateUserResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<YesValidateUserResponse> =
            mRestApi.doVlidateUser(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(
                        request
                    )
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiOkyUserAadharData(
        okycUserDataRequest: OkycUserDataRequest,
        successHandler: (RegOtpResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<RegOtpResponse> =
            mRestApi.doOkycAadhaarData(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(
                        okycUserDataRequest
                    )
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiKarzaToken(
        generateTokenFaceRequest: GenerateTokenFaceRequest,
        successHandler: (GenerateTokenFaceResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<GenerateTokenFaceResponse> =
            mRestApi.doGetKarzaToken(
                "QvAdpbUt6RReqLkZ",
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(
                        generateTokenFaceRequest
                    )
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiVerifyPan(
        verifyPancardReq: VerifyPancardRequest,
        successHandler: (VerifyPancardResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<VerifyPancardResponse> =
            mRestApi.doVerifyPan(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(
                        verifyPancardReq
                    )
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun doGetDepositeOtp(
        request: DepositeGetOtpRequest,
        successHandler: (RegOtpResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<RegOtpResponse> =
            mRestApi.doGetDepositeOtp(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(
                        request
                    )
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun doDeposite(
        request: DepositeVerifyOtpRequest,
        successHandler: (RegOtpResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<RegOtpResponse> =
            mRestApi.doGetDepositeOtp(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(
                        request
                    )
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun doVerifyDepositeOtp(
        request: DepositeVerifyOtpRequest,
        successHandler: (DepositeOtpVerifyResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<DepositeOtpVerifyResponse> =
            mRestApi.doVerifyDepositeOtp(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(
                        request
                    )
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiLoadWalletUpi(
        request: LoadWalletUpiRequest,
        successHandler: (LoadWalletUpiResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<LoadWalletUpiResponse> =
            mRestApi.doLoadWalletUpi(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(
                        request
                    )
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiValidateBank(
        request: ValidateBankDetailRequest,
        successHandler: (ValidateBankDetailResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<ValidateBankDetailResponse> =
            mRestApi.doValidateBankDetail(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(
                        request
                    )
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiOrderNewDevice(
        req: OrderDeviceRequest,
        successHandler: (OrderDeviceResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<OrderDeviceResponse> =
            mRestApi.doApiOrderDevice(Utils.getRequest(EasyPaisaApp.getGson().toJson(req)))
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiSendOtpEkyc(
        request: SendOtpEkycRequest,
        successHandler: (SednOtpEkycResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<SednOtpEkycResponse> =
            mRestApi.doApiSendOtpEkyc(Utils.getRequest(EasyPaisaApp.getGson().toJson(request)))
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiAddNewBank(
        request: AddNewBankRequest,
        successHandler: (AddNewBankRespones) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<AddNewBankRespones> =
            mRestApi.doAddNewBank(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(
                        request
                    )
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiVerifyOtpEkyc(
        request: VerifyOtpEkycRequest,
        successHandler: (BaseResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<BaseResponse> =
            mRestApi.doVerifyOtpEkyc(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(
                        request
                    )
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiEkycAuthentication(
        request: EkycAuthenticationRequest,
        successHandler: (BaseResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<BaseResponse> =
            mRestApi.doVerifyOtpEkyc(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(
                        request
                    )
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiInitPaymentGateway(
        request: OnlinePaymentInitRequest,
        successHandler: (OnlinePaymentInitResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<OnlinePaymentInitResponse> =
            mRestApi.doInitPaymentGateway(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(
                        request
                    )
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiUpdatePaymentGateway(
        request: UpdatePaymentStatusRequest,
        successHandler: (BaseResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<BaseResponse> =
            mRestApi.doUpdatePaymentGateway(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(
                        request
                    )
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiAgentEkycData(
        request: WalletBalanceRequest,
        successHandler: (AgentKycDetailResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {

        val request: Flowable<AgentKycDetailResponse> =
            mRestApi.doGetAgentKycDetails(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(
                        request
                    )
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiOnlineOrderMatm(
        req: MatmOnlineOrderRequest,
        successHandler: (MatmOnlinePaymentResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<MatmOnlinePaymentResponse> =
            mRestApi.doOnlineOrderMatm(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(req)
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

    override fun apiOnlineOrderMatmUpdate(
        req: MatmOnlineOrderDeviceUpdateRequest,
        successHandler: (BaseResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    ) {
        val request: Flowable<BaseResponse> =
            mRestApi.doOnlineOrderMatmUpdate(
                Utils.getRequest(
                    EasyPaisaApp.getGson().toJson(req)
                )
            )
        request
            .subscribeOn(newThread())
            .observeOn(mainThread())
            .subscribe(
                {
                    successHandler(it)
                }, { error ->
                    failerHandler(error)
                }
            )
    }

}