package com.app.bhaskar.easypaisa.mvp.presenter

import android.content.Intent
import android.view.View
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.model.APIError
import com.app.bhaskar.easypaisa.model.HomeData
import com.app.bhaskar.easypaisa.repositories.EasyPaisaRepository
import com.app.bhaskar.easypaisa.request_model.AepsRequest
import com.app.bhaskar.easypaisa.request_model.SearchRemitter
import com.app.bhaskar.easypaisa.request_model.WalletBalanceRequest
import com.app.bhaskar.easypaisa.response_model.AgentKycDetailResponse
import com.app.bhaskar.easypaisa.restapi.RestApi
import com.app.bhaskar.easypaisa.ui.activity.*
import com.app.bhaskar.easypaisa.ui.dialog.DialogMicroTransaction
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.google.gson.reflect.TypeToken
import org.greenrobot.eventbus.Subscribe
import java.util.*
import javax.inject.Inject

class HomeFragmentPresenterImpl(val view: HomeFragmentPresenter.HomeView) : HomeFragmentPresenter {


    private val mEventBus = EasyPaisaApp.getDefault()

    @Inject
    lateinit var repository: EasyPaisaRepository


    override fun gotoAgentKycScreen() {
        view.getViewActivity()
            .startActivity(Intent(view.getViewActivity(), AgentKycRegistration::class.java))
    }

    @Subscribe(sticky = true)
    fun onUAResponse(event: APIError) {
        view.hideProgress()
        mEventBus.removeStickyEvent(event)
        Utils.showAlert(view.getViewActivity(),event.message, View.OnClickListener {
            EasyPaisaApp.setClearSession()
            view.getViewActivity().startActivity(Intent(view.getViewActivity(),LoginActivity::class.java))
            view.getViewActivity().finishAffinity()
        })
    }

    override fun goGetMainWalletBalance() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            val request = view.doRetriveModel().getWalletBalanceReq()
            request.apptoken = EasyPaisaApp.getLoggedInUser()!!.apptoken
            request.userId = EasyPaisaApp.getLoggedInUser()!!.id.toString()

            repository.apiWalletBalance(request, {
                view.doRetriveModel().getLoginDomain().walletBalanceResponse = it!!
                view.onWalletBalance()
            }, {
                if (it?.message != null) {
                    view.showError(it.message!!)
                }
            })
        } else {
            view.showError(view.getViewActivity().getString(R.string.no_internet_message))
        }
    }

    override fun getUserRequiredData() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            val request = view.doRetriveModel().getUserReuiredDataRequest()
            request.apptoken = EasyPaisaApp.getLoggedInUser()!!.apptoken
            request.userId = EasyPaisaApp.getLoggedInUser()!!.id.toString()

            repository.apiUserReqData(request, {
                view.doRetriveModel().getLoginDomain().userRequiredDataResponse = it
                view.onUserData()
            }, {
                if (it?.message != null) {
                    view.showError(it.message!!)
                }
            })
        } else {
            view.showError(view.getViewActivity().getString(R.string.no_internet_message))
        }
    }

    override fun onError(message: String) {
    }

    override fun registerBus() {
        mEventBus.register(this)
    }

    override fun unRegisterBus() {
        mEventBus.unregister(this)
    }

    override fun doGetHomeList(): ArrayList<HomeData> {
        val arrayHome = ArrayList<HomeData>()

        //Banking services
        val homeData1 = HomeData()
        homeData1.categoryName = "Services"
        val serAepsY = HomeData.Servi()
        serAepsY.serviceIcon = R.drawable.ic_biometeric
        serAepsY.serviceName = view.getViewActivity().getString(R.string.yes_aeps)
        serAepsY.serviceId = Constants.AvailableService.SERVICE_EASYPAY_AEPS

        //Easypay  ICICI
        val serAepsIF = HomeData.Servi()
        serAepsIF.serviceIcon = R.drawable.ic_aeps_aadhaarpay
        serAepsIF.serviceName = view.getViewActivity().getString(R.string.yes_aeps_icici_ep)
        serAepsIF.serviceId = Constants.AvailableService.SERVICE_AEPS_ICICI_EP

        val serAepsF = HomeData.Servi()
        serAepsF.serviceIcon = R.drawable.ic_biometeric
        serAepsF.serviceName = view.getViewActivity().getString(R.string.fino_aeps)
        serAepsF.serviceId = Constants.AvailableService.SERVICE_FINO_AEPS

        val serAepsI = HomeData.Servi()
        serAepsI.serviceIcon = R.drawable.ic_biometeric
        serAepsI.serviceName = view.getViewActivity().getString(R.string.nsdl_aeps)
        serAepsI.serviceId = Constants.AvailableService.SERVICE_ICICI_AEPS

        val serAepsM = HomeData.Servi()
        serAepsM.serviceIcon = R.drawable.ic_matm
        serAepsM.serviceName = view.getViewActivity().getString(R.string.micro_atm)
        serAepsM.serviceId = Constants.AvailableService.SERVICE_MICRO_ATM

        val serAepsMTfr = HomeData.Servi()
        serAepsMTfr.serviceIcon = R.drawable.ic_money_transfer
        serAepsMTfr.serviceName = view.getViewActivity().getString(R.string.money_transfer)
        serAepsMTfr.serviceId = Constants.AvailableService.SERVICE_MONEY_TRANS

        val serAadharPay = HomeData.Servi()
        serAadharPay.serviceIcon = R.drawable.ic_aadharpay_txn
        serAadharPay.serviceName = view.getViewActivity().getString(R.string.aadhar_pay)
        serAadharPay.serviceId = Constants.AvailableService.SERVICE_AADHARPAY

        val serMiniStatement = HomeData.Servi()
        serMiniStatement.serviceIcon = R.drawable.ic_ministatement_bank
        serMiniStatement.serviceName = view.getViewActivity().getString(R.string.mini_statment)
        serMiniStatement.serviceId = Constants.AvailableService.SERVICE_MINI_STATEMENT

        val serAepsDeposite = HomeData.Servi()
        serAepsDeposite.serviceIcon = R.drawable.ic_cash_deposite
        serAepsDeposite.serviceName = view.getViewActivity().getString(R.string.cash_deposit)
        serAepsDeposite.serviceId = Constants.AvailableService.SERVICE_DEPOSITE


        //Fixme 22/7/21 removed Fingpay ICICI
        homeData1.servi.add(serAepsI)//FingPay AePS
        homeData1.servi.add(serAepsY)//EasyPay
//        homeData1.servi.add(serAepsF)//Fino Aeps

        homeData1.servi.add(serAadharPay)//Aadharpay Aeps
        homeData1.servi.add(serMiniStatement)//Ministatement Aeps
        homeData1.servi.add(serAepsDeposite)//Deposite Aeps

        homeData1.servi.add(serAepsM)//ICICI Micro ATM
        homeData1.servi.add(serAepsMTfr)//DMT

        //Bill Pay and Recharge

        val homeData2 = HomeData()
        homeData2.categoryName = "Bill Pay and Recharge"

        val serAepsEl = HomeData.Servi()
        serAepsEl.serviceIcon = R.drawable.ic_electricity_txn
        serAepsEl.serviceName = view.getViewActivity().getString(R.string.electricity)
        serAepsEl.serviceId = Constants.AvailableService.SERVICE_BILL_ELECTRICITY

        val serAepsmobile = HomeData.Servi()
        serAepsmobile.serviceIcon = R.drawable.ic_mobile_recharge
        serAepsmobile.serviceName = view.getViewActivity().getString(R.string.recharge)
        serAepsmobile.serviceId = Constants.AvailableService.SERVICE_MOBILE_RECHARGE

        val serAepsdth = HomeData.Servi()
        serAepsdth.serviceIcon = R.drawable.ic_dth_bill
        serAepsdth.serviceName = view.getViewActivity().getString(R.string.dth)
        serAepsdth.serviceId = Constants.AvailableService.SERVICE_DTH

        val serPostpaid = HomeData.Servi()
        serPostpaid.serviceIcon = R.drawable.ic_mobile_bill
        serPostpaid.serviceName = view.getViewActivity().getString(R.string.postpaid)
        serPostpaid.serviceId = Constants.AvailableService.SERVICE_POSTPAID

        val serGas = HomeData.Servi()
        serGas.serviceIcon = R.drawable.ic_gas_bottle
        serGas.serviceName = view.getViewActivity().getString(R.string.lpg_gas)
        serGas.serviceId = Constants.AvailableService.SERVICE_LPG_GAS

        val serPipedGas = HomeData.Servi()
        serPipedGas.serviceIcon = R.drawable.ic_piped_gas
        serPipedGas.serviceName = view.getViewActivity().getString(R.string.piped_gas)
        serPipedGas.serviceId = Constants.AvailableService.SERVICE_PIPED_GAS

        val serWater = HomeData.Servi()
        serWater.serviceIcon = R.drawable.ic_water_bill
        serWater.serviceName = view.getViewActivity().getString(R.string.water)
        serWater.serviceId = Constants.AvailableService.SERVICE_WATER

        val serInsurance = HomeData.Servi()
        serInsurance.serviceIcon = R.drawable.ic_insurance
        serInsurance.serviceName = view.getViewActivity().getString(R.string.insurance_primium)
        serInsurance.serviceId = Constants.AvailableService.SERVICE_INSURANCE

        val serUtiPancard = HomeData.Servi()
        serUtiPancard.serviceIcon = R.drawable.ic_pan_card
        serUtiPancard.serviceName = view.getViewActivity().getString(R.string.uti_pancard)
        serUtiPancard.serviceId = Constants.AvailableService.SERVICE_UTI_PAN

        val serOrderDevice = HomeData.Servi()
        serOrderDevice.serviceIcon = R.drawable.ic_atm_device
        serOrderDevice.serviceName = view.getViewActivity().getString(R.string.order_device)
        serOrderDevice.serviceId = Constants.AvailableService.SERVICE_ORDER_DEVICE

        homeData1.servi.add(serAepsI)//FingPay AePS
        homeData1.servi.add(serAepsY)//EasyPay
        homeData1.servi.add(serAadharPay)//Aadharpay Aeps
        homeData1.servi.add(serMiniStatement)//Ministatement Aeps
        homeData1.servi.add(serAepsDeposite)//Deposite Aeps
        homeData1.servi.add(serAepsM)//ICICI Micro ATM
        homeData1.servi.add(serAepsMTfr)//DMT
        homeData1.servi.add(serAepsEl)
        homeData1.servi.add(serAepsmobile)
        homeData1.servi.add(serPostpaid)
        homeData1.servi.add(serAepsdth)
        homeData1.servi.add(serInsurance)
        homeData1.servi.add(serGas)
        homeData1.servi.add(serPipedGas)
        homeData1.servi.add(serWater)
        homeData1.servi.add(serUtiPancard)
        homeData1.servi.add(serOrderDevice)

        //All service at home arraylist
        arrayHome.add(homeData1)

        return arrayHome

    }

    override fun proceedAction(it: HomeData.Servi) {
        val intent = Intent(view.getViewActivity(), AepsActivity::class.java)
        when (it.serviceId) {
            Constants.AvailableService.SERVICE_EASYPAY_AEPS -> {
                //Fixme Removed eKyc verofication as no needed 12/0821
                /*if (view.getUserData()?.agent!=null &&
                    view.getUserData()?.yesagent?.status != "approved") {
                    Utils.showAlert(
                        view.getViewActivity(),
                        "eKYC verification are needed in order to access the services",
                        "Proceed",
                        "Cancel",
                        View.OnClickListener {
                            apiAgentData()
                            //Non KYC agent
                        },View.OnClickListener {
                        })
                    return
                }*/
                intent.putExtra("selectedService", Constants.AvailableService.SERVICE_EASYPAY_AEPS)
                view.getViewActivity().startActivity(intent)
            }
            Constants.AvailableService.SERVICE_AEPS_ICICI_EP -> {
                //Fixme Removed eKyc verofication as no needed 12/0821
                /*if (view.getUserData()?.agent?.everify == "pending") {
                    Utils.showAlert(
                        view.getViewActivity(),
                        "eKYC verification are needed in order to access the services",
                        "Proceed",
                        "Cancel",
                        View.OnClickListener {
                            //Non KYC agent
                            gotoAgenteKycScreen()
                        },View.OnClickListener {
                        })
                    return
                }*/
                intent.putExtra("selectedService", Constants.AvailableService.SERVICE_AEPS_ICICI_EP)
                view.getViewActivity().startActivity(intent)
            }
            Constants.AvailableService.SERVICE_FINO_AEPS -> {
                intent.putExtra("selectedService", Constants.AvailableService.SERVICE_FINO_AEPS)
                view.getViewActivity().startActivity(intent)
            }
            Constants.AvailableService.SERVICE_ICICI_AEPS -> {
                //FingPay AePS transactions
                //Fixme Removed eKyc verofication as no needed 12/0821
                /*if (view.getUserData()?.agent?.everify == "pending") {
                    Utils.showAlert(
                        view.getViewActivity(),
                        "eKYC verification are needed in order to access the services",
                        "Proceed",
                        "Cancel",
                        View.OnClickListener {
                            //Non KYC agent
                            gotoAgenteKycScreen()
                        },View.OnClickListener {
                        })
                    return
                }*/
                intent.putExtra("selectedService", Constants.AvailableService.SERVICE_ICICI_AEPS)
                view.getViewActivity().startActivity(intent)
            }
            Constants.AvailableService.SERVICE_MICRO_ATM -> {
                if (view.getUserData()?.agent?.everify == "pending") {
                    Utils.showAlert(
                        view.getViewActivity(),
                        "eKYC verification are needed in order to access Micro ATM services",
                        "Proceed",
                        "Cancel",
                        View.OnClickListener {
                            //Non KYC agent
                            gotoAgenteKycScreen()
                        },View.OnClickListener {
                        })
                    return
                }
                val dialog = DialogMicroTransaction(view.getViewActivity())
                dialog.showMicroAtmTransactionDialog {
                    view.initiateAtmTransaction(it)
                }
            }

            Constants.AvailableService.SERVICE_MONEY_TRANS -> {
                view.openSearchUserForDmt()
            }

            Constants.AvailableService.SERVICE_AADHARPAY -> {
                //Fixme Removed eKyc verofication as no needed 12/0821
                /*if (view.getUserData()?.agent?.everify == "pending") {
                    Utils.showAlert(
                        view.getViewActivity(),
                        "eKYC verification are needed in order to access the services",
                        "Proceed",
                        "Cancel",
                        View.OnClickListener {
                            //Non KYC agent
                            gotoAgenteKycScreen()
                        },View.OnClickListener {
                        })
                    return
                }*/
                intent.putExtra("selectedService", Constants.AvailableService.SERVICE_EASYPAY_AEPS)
                intent.putExtra(
                    "transactionFor",
                    view.getViewActivity().getString(R.string.ser_aadhar_pay)
                )
                view.getViewActivity().startActivity(intent)
            }

            Constants.AvailableService.SERVICE_MINI_STATEMENT -> {
                //Fixme Removed eKyc verofication as no needed 12/0821
                /*if (view.getUserData()?.agent?.everify == "pending") {
                    Utils.showAlert(
                        view.getViewActivity(),
                        "eKYC verification are needed in order to access the services",
                        "Proceed",
                        "Cancel",
                        View.OnClickListener {
                            //Non KYC agent
                            gotoAgenteKycScreen()
                        },View.OnClickListener {
                        })
                    return
                }*/
                intent.putExtra("selectedService", Constants.AvailableService.SERVICE_EASYPAY_AEPS)
                intent.putExtra(
                    "transactionFor",
                    view.getViewActivity().getString(R.string.ser_mini_statement)
                )
                view.getViewActivity().startActivity(intent)
            }
            Constants.AvailableService.SERVICE_DEPOSITE -> {
                //Fixme Removed eKyc verofication as no needed 12/0821
                /*if (view.getUserData()?.agent?.everify == "pending") {
                    Utils.showAlert(
                        view.getViewActivity(),
                        "eKYC verification are needed in order to access the services",
                        "Proceed",
                        "Cancel",
                        View.OnClickListener {
                            //Non KYC agent
                            gotoAgenteKycScreen()
                        },View.OnClickListener {
                        })
                    return
                }*/
                val intent = Intent(view.getViewActivity(),DepositeCashActivity::class.java)
                intent.putExtra("selectedService", Constants.AvailableService.SERVICE_ICICI_AEPS)
                intent.putExtra(
                    "transactionFor",
                    view.getViewActivity().getString(R.string.ser_deposit)
                )
                view.getViewActivity().startActivity(intent)
            }

            Constants.AvailableService.SERVICE_UTI_PAN -> {
                val intentPanCard = Intent(view.getViewActivity(), PancardUtiActivity::class.java)
                intent.putExtra("selectedService", Constants.AvailableService.SERVICE_UTI_PAN)
                view.getViewActivity().startActivity(intentPanCard)
            }
            Constants.AvailableService.SERVICE_ORDER_DEVICE -> {
                val intentPanCard = Intent(view.getViewActivity(), PancardUtiActivity::class.java)
                intentPanCard.putExtra("selectedService", Constants.AvailableService.SERVICE_ORDER_DEVICE)
                view.getViewActivity().startActivity(intentPanCard)
            }
            Constants.AvailableService.SERVICE_MOBILE_RECHARGE -> {
                val intentMobile =
                    Intent(view.getViewActivity(), MobileRechargeActivity::class.java)
                intentMobile.putExtra(
                    "selectedService",
                    Constants.AvailableService.SERVICE_MOBILE_RECHARGE
                )
                view.getViewActivity().startActivity(intentMobile)
            }
            Constants.AvailableService.SERVICE_BILL_ELECTRICITY -> {
                val intentMobile =
                    Intent(view.getViewActivity(), ElectricityBillPaymentActivity::class.java)
                intentMobile.putExtra(Constants.UI.SERVICE_FOR,Constants.AvailableService.SERVICE_BILL_ELECTRICITY)
                view.getViewActivity().startActivity(intentMobile)
            }
            Constants.AvailableService.SERVICE_LPG_GAS -> {
                val intentMobile =
                    Intent(view.getViewActivity(), ElectricityBillPaymentActivity::class.java)
                intentMobile.putExtra(Constants.UI.SERVICE_FOR,Constants.AvailableService.SERVICE_LPG_GAS)
                view.getViewActivity().startActivity(intentMobile)
            }
            Constants.AvailableService.SERVICE_PIPED_GAS -> {
                val intentMobile =
                    Intent(view.getViewActivity(), ElectricityBillPaymentActivity::class.java)
                intentMobile.putExtra(Constants.UI.SERVICE_FOR,Constants.AvailableService.SERVICE_PIPED_GAS)
                view.getViewActivity().startActivity(intentMobile)
            }
            Constants.AvailableService.SERVICE_WATER -> {
                val intentMobile =
                    Intent(view.getViewActivity(), ElectricityBillPaymentActivity::class.java)
                intentMobile.putExtra(Constants.UI.SERVICE_FOR,Constants.AvailableService.SERVICE_WATER)
                view.getViewActivity().startActivity(intentMobile)
            }
            Constants.AvailableService.SERVICE_POSTPAID -> {
                val intentMobile =
                    Intent(view.getViewActivity(), ElectricityBillPaymentActivity::class.java)
                intentMobile.putExtra(Constants.UI.SERVICE_FOR,Constants.AvailableService.SERVICE_POSTPAID)
                view.getViewActivity().startActivity(intentMobile)
            }
            Constants.AvailableService.SERVICE_INSURANCE -> {
                val intentMobile =
                    Intent(view.getViewActivity(), ElectricityBillPaymentActivity::class.java)
                intentMobile.putExtra(Constants.UI.SERVICE_FOR,Constants.AvailableService.SERVICE_INSURANCE)
                view.getViewActivity().startActivity(intentMobile)
            }

            Constants.AvailableService.SERVICE_DTH -> {
                val intentDth =
                    Intent(view.getViewActivity(), DthRechargeActivity::class.java)
                view.getViewActivity().startActivity(intentDth)
            }
        }
    }

    private fun apiAgentData() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress("Wait for while...")
            val request  = WalletBalanceRequest()
            request.userId =EasyPaisaApp.getLoggedInUser()?.id.toString()
            request.apptoken =EasyPaisaApp.getLoggedInUser()?.apptoken.toString()
            repository.apiAgentEkycData(request, {
                view.hideProgress()
                gotoYeabnakEkyc(it)
            }, {
                view.hideProgress()
                if (it?.message != null) {
                    view.showError(it.message!!)
                }
            })
        } else {
            view.showError(view.getViewActivity().getString(R.string.no_internet_message))
        }
    }

    private fun gotoYeabnakEkyc(agentKycDetailResponse: AgentKycDetailResponse) {
        val  webIntent = Intent(view.getViewActivity(),WebviewActivity::class.java)
        webIntent.putExtra("shopName",agentKycDetailResponse.data.shopName?:"")
        webIntent.putExtra("panCardNo",agentKycDetailResponse.data.panCardNo)
        webIntent.putExtra("mobileNumber",agentKycDetailResponse.data.mobileNumber)
        webIntent.putExtra("cpCode",agentKycDetailResponse.data.cpCode)
        webIntent.putExtra("cs",agentKycDetailResponse.data.cs)
        webIntent.putExtra("sscode",agentKycDetailResponse.data.sscode)
        webIntent.putExtra("agentCode",agentKycDetailResponse.data.agentCode)
        webIntent.putExtra("loadWebUrl",agentKycDetailResponse.data.url)
        view.getViewActivity().startActivity(webIntent)
    }

    override fun searchRemitter(it: SearchRemitter) {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress("Verifying information...")
            repository.apiSearchRemitter(it, {
                view.hideProgress()
                view.doRetriveModel().getLoginDomain().searchRemitterResponse = it
                view.onRemitterVerify()
            }, {
                view.hideProgress()
                if (it?.message != null) {
                    view.showError(it.message!!)
                }
            })
        } else {
            view.showError(view.getViewActivity().getString(R.string.no_internet_message))
        }
    }

    override fun doVerifyRemitter() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress("Verifying wait for while....")
            val request = view.doRetriveModel().getVerifyRemitterReq()
            repository.apiVerifyRemitter(request, {
                view.hideProgress()
                view.doRetriveModel().getLoginDomain().verifyRemitterResponse = it
                view.onVerifyRemitter()
            }, {
                view.hideProgress()
                if (it?.message != null) {
                    view.showError(it.message!!)
                }
            })
        } else {
            view.showError(view.getViewActivity().getString(R.string.no_internet_message))
        }
    }

    override fun sendOtp() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress()
            val request = view.doRetriveModel().getGenerateOtpReq()
            repository.apiGenerateOtp(request, {
                view.hideProgress()
                view.doRetriveModel().getLoginDomain().generateOtpResponse = it
                view.onGenerateOtp()
            }, {
                view.hideProgress()
                if (it?.message != null) {
                    view.showError(it.message!!)
                }
            })
        } else {
            view.showError(view.getViewActivity().getString(R.string.no_internet_message))
        }
    }

    override fun gotoUpdatePassword() {
        view.getViewActivity()
            .startActivity(Intent(view.getViewActivity(), ChangePasswordActivity::class.java))
    }

    override fun doLogout() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            val request = view.doRetriveModel().getLogoutReq()
            request.apptoken = EasyPaisaApp.getLoggedInUser()?.apptoken!!
            request.userId = EasyPaisaApp.getLoggedInUser()?.id!!.toString()
            repository.apiLogout(request, {
                view.doRetriveModel().getLoginDomain().logoutResponse = it
                view.onAppLogout()
            }, {
                if (it?.message != null) {
                    view.showError(it.message!!)
                }
            })
        }
    }

    override fun gotoAgenteKycScreen() {
        val type = object : TypeToken<AepsRequest>() {}.type
        val aepsRequest = AepsRequest()
        aepsRequest.aadharNo= ""
        aepsRequest.aepsServiceFor = Constants.AvailableService.SERVICE_ICICI_AEPS
        aepsRequest.amount =""
        aepsRequest.transactionFor = view.getViewActivity().getString(R.string.ser_auth_biometric)
        val intent = Intent(view.getViewActivity(), CaptureFingerActivity::class.java)
        intent.putExtra(Constants.UI.AEPSREQUEST, EasyPaisaApp.getGson().toJson(aepsRequest, type))
        view.getViewActivity().startActivity(intent)
    }
}