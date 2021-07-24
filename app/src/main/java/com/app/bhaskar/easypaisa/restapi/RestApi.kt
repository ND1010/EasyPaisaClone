package com.app.bhaskar.easypaisa.restapi


import com.app.bhaskar.easypaisa.PanTokenResponse
import com.app.bhaskar.easypaisa.mvp.domain.LoadWalletUpiResponse
import com.app.bhaskar.easypaisa.request_model.DmtTransactionResponse
import com.app.bhaskar.easypaisa.request_model.LogoutResponse
import com.app.bhaskar.easypaisa.response_model.*
import com.app.bhaskar.easypaisa.utils.Constants
import io.reactivex.Flowable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.bouncycastle.jce.provider.symmetric.Grainv1
import retrofit2.http.*
import java.util.*

interface RestApi {

    @POST(Constants.ApiMethod.API_LOGIN)
    fun doApiForLogin(@Body request: RequestBody): Flowable<LoginResponse>

    @POST(Constants.ApiMethod.API_FORGOT_PASSWORD)
    fun doApiForUpdatePassword(@Body request: RequestBody): Flowable<UpdatePasswordResponse>

    @POST(Constants.ApiMethod.API_SEND_TOKEN)
    fun doApiForPassResetToken(@Body request: RequestBody): Flowable<PasswordResetTokenResponse>

    @POST(Constants.ApiMethod.API_WALLET_BAL)
    fun doApiForWalletBalance(@Body request: RequestBody): Flowable<WalletBalanceResponse>

    @POST(Constants.ApiMethod.API_USER_REQ_DATA)
    fun doApiForUserRequiredData(@Body request: RequestBody): Flowable<UserRequiredDataResponse>

    @POST(Constants.ApiMethod.API_AGNET_KYC)
    @Multipart
    fun doApiForAgentKyc(
        @PartMap
        params: HashMap<String, RequestBody>,
        @Part
        bodyPanImage: MultipartBody.Part,
        @Part
        bodyAadhaarImage: MultipartBody.Part,
        @Part
        bodyAadhaarImageBack: MultipartBody.Part,
        @Part
        userSelfie: MultipartBody.Part
    ): Flowable<AgentKycResponse>

    @POST(Constants.ApiMethod.API_FING_PAY_AEPS)
    fun doApiFingPayAepsTransactions(@Body request: RequestBody): Flowable<FingPayAepsTxnResponse>

    @POST(Constants.ApiMethod.API_MOBILE_PROVIDER)
    fun doApiGetMobileOperators(@Body request: RequestBody): Flowable<RechargeProviderResponse>

    @POST(Constants.ApiMethod.API_MOBILE_RECHARGE)
    fun doApiGetMobilePlan(@Body request: RequestBody): Flowable<MobileRechargePlanResponse>

    @POST(Constants.ApiMethod.API_MOBILE_RECHARGE)
    fun doApiGetMobileOperatorFromMobile(@Body request: RequestBody): Flowable<MobileOperatorFromMobile>

    @POST(Constants.ApiMethod.API_MOBILE_RECHARGE)
    fun doApiMobileRecharge(@Body request: RequestBody): Flowable<MobileRechargeResponse>

    @POST(Constants.ApiMethod.API_ELECTRICITY_STATE)
    fun doApiElectricityState(@Body request: RequestBody): Flowable<EelectricityStateResponse>

    @POST(Constants.ApiMethod.API_ELECTRICITY_BOARD)
    fun doApiElectricityBoard(@Body request: RequestBody): Flowable<ElectricityBoardResponse>

    @POST(Constants.ApiMethod.API_ELECTRICITY_PAYBILL)
    fun doApiFetchBillDetail(@Body request: RequestBody): Flowable<FetchElBillDetailResponse>

    @POST(Constants.ApiMethod.API_ELECTRICITY_PAYBILL)
    fun doApiElectricityPayBill(@Body request: RequestBody): Flowable<ElectricityBillPaymentResponse>

    @POST(Constants.ApiMethod.API_PANCARD_DETAIL)
    fun doApiPanCardDetail(@Body request: RequestBody): Flowable<AgentPancardResponse>

    @POST(Constants.ApiMethod.API_UTI_TRANSACTION)
    fun doApiUtiReg(@Body request: RequestBody): Flowable<UtiRegistrationResponse>

    @POST(Constants.ApiMethod.API_UTI_TRANSACTION)
    fun doApiPanToken(@Body request: RequestBody): Flowable<PanTokenResponse>

    @POST(Constants.ApiMethod.API_UTI_TRANSACTION)
    fun doApiResetPsaId(@Body request: RequestBody): Flowable<UtiResetPsaIDResponse>

    @POST(Constants.ApiMethod.API_CHANGE_PASS)
    fun doApiUpdatePass(@Body request: RequestBody): Flowable<UpdatePasswordResponse>

    @POST(Constants.ApiMethod.API_AEPS_TXN)
    fun doApiAepTxn(@Body request: RequestBody): Flowable<AepsTnxDataResponse>

    @POST(Constants.ApiMethod.API_REGISTRATION)
    fun doApiRegistration(@Body request: RequestBody): Flowable<UserRegistrationResponse>

    @POST(Constants.ApiMethod.API_TXN_HISTORY)
    fun doApiTxnHistory(@Body request: RequestBody): Flowable<TransactionResponse>

    @POST(Constants.ApiMethod.API_LOAD_DATA)
    fun doGetWalletData(@Body request: RequestBody): Flowable<WalletRequiredDataResponse>

    @POST(Constants.ApiMethod.API_FUND_TRANSACTION)
    fun doLoadWallet(@Body request: RequestBody): Flowable<WalletLoadRequestResponse>

    @POST(Constants.ApiMethod.API_DMT_TXN)
    fun doSearchRemitter(@Body request: RequestBody): Flowable<SearchRemitterResponse>

    @POST(Constants.ApiMethod.API_DMT_TXN)
    fun doDmtTransaction(@Body request: RequestBody): Flowable<DmtTransactionResponse>

    @POST(Constants.ApiMethod.API_DMT_TXN)
    fun doAddNewBene(@Body request: RequestBody): Flowable<AddNewBeneficiaryResponse>

    @POST(Constants.ApiMethod.API_DMT_TXN)
    fun doRegisterRemmiter(@Body request: RequestBody): Flowable<RemitterRegisterResponse>

    @POST(Constants.ApiMethod.API_DMT_TXN)
    fun doGenerateOtp(@Body request: RequestBody): Flowable<GenerateOtpResponse>

    @POST(Constants.ApiMethod.API_DMT_TXN)
    fun doVerifyRemitter(@Body request: RequestBody): Flowable<VerifyRemitterResponse>

    @POST(Constants.ApiMethod.API_MICRO_ATM)
    fun doMicroAtmInit(@Body request: RequestBody): Flowable<MicroAtmInitTransactionResponse>

    @POST(Constants.ApiMethod.API_MICRO_ATM_UPDATE)
    fun doMicroAtmUpdate(@Body request: RequestBody): Flowable<MicroAtmUpdateResponse>

    @POST(Constants.ApiMethod.API_AEPS_TXN)
    fun doApiFingPayAepsMini(@Body request: RequestBody): Flowable<FingpayMiniStatementResponse>

    @POST(Constants.ApiMethod.API_TXN_HISTORY)
    fun doAccountLedger(@Body request: RequestBody): Flowable<AccountLedgerResponse>

    @POST(Constants.ApiMethod.API_YES_BANK_AEPS)
    fun doApiYesAepsTransactions(@Body request: RequestBody): Flowable<FingPayAepsTxnResponse>

    @POST(Constants.ApiMethod.API_LOGOUT)
    fun doLgoutFromApp(@Body request: RequestBody): Flowable<LogoutResponse>

    @POST(Constants.ApiMethod.API_YES_BANK_AEPS)
    fun doVlidateUser(@Body request: RequestBody): Flowable<YesValidateUserResponse>

    @POST(Constants.ApiMethod.API_MOBILE_RECHARGE)
    fun doApiDTHRecharge(@Body request: RequestBody): Flowable<ElectricityBillPaymentResponse>

    @POST(Constants.ApiMethod.API_GET_OTP)
    fun doGetOtp(@Body request: RequestBody): Flowable<RegOtpResponse>

    @POST(Constants.ApiMethod.URL_GENERATE_TOKEN)
    fun doGetKarzaToken(@Header("x-karza-key") karza_key:String ,@Body request: RequestBody): Flowable<GenerateTokenFaceResponse>

    @POST(Constants.ApiMethod.API_OKYC_DATA)
    fun doOkycAadhaarData(@Body request: RequestBody): Flowable<RegOtpResponse>

    @POST(Constants.ApiMethod.API_VERIFY_PAN)
    fun doVerifyPan(@Body request: RequestBody): Flowable<VerifyPancardResponse>

    @POST(Constants.ApiMethod.API_FING_PAY_AEPS)
    fun doVerifyDepositeOtp(@Body request: RequestBody): Flowable<DepositeOtpVerifyResponse>

    @POST(Constants.ApiMethod.API_FING_PAY_AEPS)
    fun doGetDepositeOtp(@Body request: RequestBody): Flowable<RegOtpResponse>

    @POST(Constants.ApiMethod.API_FUND_TRANSACTION)
    fun doLoadWalletUpi(@Body request: RequestBody): Flowable<LoadWalletUpiResponse>

    @POST(Constants.ApiMethod.API_FUND_TRANSACTION)
    fun doValidateBankDetail(@Body request: RequestBody): Flowable<ValidateBankDetailResponse>

    @POST(Constants.ApiMethod.API_FUND_TRANSACTION)
    fun doAddNewBank(@Body request: RequestBody): Flowable<AddNewBankRespones>

    @POST(Constants.ApiMethod.URL_ORDER_DEVICE)
    fun doApiOrderDevice(@Body request: RequestBody): Flowable<OrderDeviceResponse>

    @POST(Constants.ApiMethod.URL_EKYC_AUTH)
    fun doApiSendOtpEkyc(@Body request: RequestBody): Flowable<SednOtpEkycResponse>

    @POST(Constants.ApiMethod.URL_EKYC_AUTH)
    fun doVerifyOtpEkyc(@Body request: RequestBody): Flowable<BaseResponse>

    @POST(Constants.ApiMethod.API_FUND_TRANSACTION)
    fun doInitPaymentGateway(@Body request: RequestBody): Flowable<OnlinePaymentInitResponse>

    @POST(Constants.ApiMethod.API_FUND_TRANSACTION)
    fun doUpdatePaymentGateway(@Body request: RequestBody): Flowable<BaseResponse>

    @POST(Constants.ApiMethod.API_AGENT_KYC_DETAIL)
    fun doGetAgentKycDetails(@Body request: RequestBody): Flowable<AgentKycDetailResponse>

    @POST(Constants.ApiMethod.URL_ORDER_DEVICE)
    fun doOnlineOrderMatm(@Body request: RequestBody): Flowable<MatmOnlinePaymentResponse>

    @POST(Constants.ApiMethod.URL_ORDER_DEVICE)
    fun doOnlineOrderMatmUpdate(@Body request: RequestBody): Flowable<BaseResponse>
}
