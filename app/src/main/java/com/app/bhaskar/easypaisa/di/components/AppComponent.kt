package com.app.bhaskar.easypaisa.di.components

import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.mvp.presenter.*
import com.pa.di.modules.DatabaseModule
import com.pa.di.modules.PostRepoModule
import com.pa.di.modules.RemoteDataModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(DatabaseModule::class, RemoteDataModule::class, PostRepoModule::class))
interface AppComponent {

    fun inject(myApp: EasyPaisaApp)
    fun inject(myApp: LoginPresenterImpl)
    fun inject(presenter: RegistrationPresenterImpl)
    fun inject(presenter: HomeActivityPresenterImpl)
    fun inject(presenter: ForgotPasswordPresenterImpl)
    fun inject(presenter: HomeFragmentPresenterImpl)
    fun inject(presenter: AepsPresenterImpl)
    fun inject(presenter: WalletFragmentPresenterImpl)
    fun inject(presenter: AgentKycPresenterImpl)
    fun inject(presenter: SelectbankActivityPresenterImpl)
    fun inject(presenter: CaptureFingerPresenterImpl)
    fun inject(presenter: AePSTransactionReceiptPresenterImpl)
    fun inject(presenter: MobileRechargePresenterImpl)
    fun inject(presenter: MobileRechargePlanPresenterImpl)
    fun inject(presenter: ElectricityBillPaymentPresenterImpl)
    fun inject(presenter: PancardUtiPresenterImpl)
    fun inject(presenter: UtiRegistrationPresenterImpl)
    fun inject(presenter: ChangePassPresenterImpl)
    fun inject(presenter: DthRechargePresenterImpl)
    fun inject(presenter: TransactionFragmentPresenterImpl)
    fun inject(presenter: LoadWalletPresenterImpl)
    fun inject(presenter: SettlementRequestPresenterImpl)
    fun inject(presenter: MicroAtmIciciPresenterImpl)
    fun inject(presenter: DmtTransactionActivityPresenterImpl)
    fun inject(presenter: AddNewBeneActivityPresenterImpl)
    fun inject(presenter: RegisterRemitterPresenterImpl)
    fun inject(presenter: AccountLedgerPresenterImpl)
    fun inject(presenter: AccountFragmentPresenterImpl)
    fun inject(presenter: DepositeActivityPresenterImpl)
    fun inject(presenter: LoadWalletUpiPresenterImpl)
    fun inject(presenter: AddNewBankActivityPresenterImpl)
}