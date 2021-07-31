package com.app.bhaskar.easypaisa.repositories

import com.app.bhaskar.easypaisa.PanTokenResponse
import com.app.bhaskar.easypaisa.mvp.domain.LoadWalletUpiResponse
import com.app.bhaskar.easypaisa.request_model.*
import com.app.bhaskar.easypaisa.response_model.*


interface EasyPaisaRepository {
    fun apiLoginToApp(
        request: LoginRequest,
        successHandler: (LoginResponse?) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiUpdatePassword(
        request: UpdatePasswordRequest,
        successHandler: (UpdatePasswordResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiPasswordResetnToken(
        request: PasswordResetToeknRequest,
        successHandler: (PasswordResetTokenResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiWalletBalance(
        request: WalletBalanceRequest,
        successHandler: (WalletBalanceResponse?) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiUserReqData(
        request: UserRequiredDataRequest,
        succressHandler: (UserRequiredDataResponse) -> Unit,
        failerHansler: (Throwable?) -> Unit
    )

    fun doAgentKyc(
        request: AgentKycRequest,
        successHandler: (AgentKycResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiFingPayICICITransaction(
        request: FingPayICICIAepsTransactionRequest,
        successHandler: (FingPayAepsTxnResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiRechargePlan(
        request: MobileRechargePlanRequest,
        successHandler: (MobileRechargePlanResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiRechargeProviders(
        request: GetMobileRechargeProviderRequest,
        successHandler: (RechargeProviderResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiOperatorFromMobile(
        request: GetMobileOperatorFromMobile,
        successHandler: (MobileOperatorFromMobile) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiMobileRecharge(
        request: MobileRechargeRequest,
        successHandler: (MobileRechargeResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiElectricityState(
        request: ElectricityStateRequest,
        successHandler: (EelectricityStateResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiElectricityBoard(
        request: ElectricityBoardRequest,
        successHandler: (ElectricityBoardResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiFetchElBillDetails(
        request: FetchElBillDetailRequest,
        successHandler: (FetchElBillDetailResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiPayElectricityBill(
        request: ElectricityBillPaymentRequest,
        successHandler: (ElectricityBillPaymentResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiRechargeDTH(
        request: ElectricityBillPaymentRequest,
        successHandler: (ElectricityBillPaymentResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiGetPancardData(
        request: PancardDataRequest,
        successHandler: (AgentPancardResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiUtiRegistration(
        request: UtiRegisterRequest,
        successHandler: (UtiRegistrationResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiGetTokenForPancard(
        request: PanTokenRequest,
        successHandler: (PanTokenResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiResetPsaId(
        request: UtiPanTokenResetRequest,
        successHandler: (UtiResetPsaIDResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiChangePassword(
        request: ChangePasswordRequest,
        successHandler: (UpdatePasswordResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiAepsTxnData(
        request: AepsTransactionRequest,
        successHandler: (AepsTnxDataResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun doRegisterUser(
        request: RegistrationRequest,
        successHandler: (UserRegistrationResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun doGetTxnHistory(
        request: TransactionHistoryRequest,
        successHandler: (TransactionResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiWalletLoadReqData(
        request: WalletRequiredDataRequest,
        successHandler: (WalletRequiredDataResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiWalletLoadReq(
        request: WalletLoadRequest,
        successHandler: (WalletLoadRequestResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiMoveWalletBank(
        request: MoveToBankWalletRequest,
        successHandler: (WalletLoadRequestResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiSearchRemitter(
        request: SearchRemitter,
        successHandler: (SearchRemitterResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiDoDmtTransaction(
        request: DmtTransferRequest,
        successHandler: (DmtTransactionResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiAddNewBene(
        request: AddNewBeneficiaryRequest,
        successHandler: (AddNewBeneficiaryResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiRegisterRemitter(
        request: RegisterRemmiterRequest,
        successHandler: (RemitterRegisterResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiGenerateOtp(
        request: GenerateOtpRequest,
        successHandler: (GenerateOtpResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiVerifyRemitter(
        request: VerifyRemitterOtpRequest,
        successHandler: (VerifyRemitterResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiMicroAtmInit(
        request: MicroAtmInitTransactionRequest,
        successHandler: (MicroAtmInitTransactionResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiMicroAtmUpdate(
        request: MicroAtmUpdateRequest,
        successHandler: (MicroAtmUpdateResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiFingPayICICITransactionMiniStatement(
        request: FingPayICICIAepsTransactionRequest,
        successHandler: (FingpayMiniStatementResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiAccountLeder(
        request: AccountLedgerRequest,
        successHandler: (AccountLedgerResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiYesAepsTransaction(
        request: FingPayICICIAepsTransactionRequest,
        successHandler: (FingPayAepsTxnResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiLogout(
        request: WalletBalanceRequest,
        successHandler: (LogoutResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiUserValidate(
        request: YesValidateUserRequest,
        successHandler: (YesValidateUserResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun doGetOtp(
        request: RegisterOtpRequest,
        successHandler: (RegOtpResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiKarzaToken(
        generateTokenFaceRequest: GenerateTokenFaceRequest,
        successHandler: (GenerateTokenFaceResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiOkyUserAadharData(
        okycUserDataRequest: OkycUserDataRequest,
        successHandler: (RegOtpResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiVerifyPan(
        verifyPancardReq: VerifyPancardRequest,
        successHandler: (VerifyPancardResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun doGetDepositeOtp(
        request: DepositeGetOtpRequest,
        successHandler: (RegOtpResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun doVerifyDepositeOtp(
        request: DepositeVerifyOtpRequest,
        successHandler: (DepositeOtpVerifyResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun doDeposite(
        request: DepositeVerifyOtpRequest,
        successHandler: (RegOtpResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiLoadWalletUpi(
        request: LoadWalletUpiRequest,
        successHandler: (LoadWalletUpiResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiValidateBank(
        request: ValidateBankDetailRequest,
        successHandler: (ValidateBankDetailResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiAddNewBank(
        request: AddNewBankRequest,
        successHandler: (AddNewBankRespones) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiOrderNewDevice(
        req: OrderDeviceRequest,
        successHandler: (OrderDeviceResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiSendOtpEkyc(
        request: SendOtpEkycRequest,
        successHandler: (SednOtpEkycResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiVerifyOtpEkyc(
        request: VerifyOtpEkycRequest,
        successHandler: (BaseResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiEkycAuthentication(
        request: EkycAuthenticationRequest,
        successHandler: (BaseResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiInitPaymentGateway(
        request: OnlinePaymentInitRequest,
        successHandler: (OnlinePaymentInitResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiUpdatePaymentGateway(
        request: UpdatePaymentStatusRequest,
        successHandler: (BaseResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiAgentEkycData(
        request: WalletBalanceRequest,
        successHandler: (AgentKycDetailResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiOnlineOrderMatm(
        req: MatmOnlineOrderRequest,
        successHandler: (MatmOnlinePaymentResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

    fun apiOnlineOrderMatmUpdate(
        req: MatmOnlineOrderDeviceUpdateRequest,
        successHandler: (BaseResponse) -> Unit,
        failerHandler: (Throwable?) -> Unit
    )

}