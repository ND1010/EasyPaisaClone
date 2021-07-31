package com.app.bhaskar.easypaisa.ui.fragment


import aepsapp.easypay.com.aepsandroid.adapters.HomeAdapter
import aepsapp.easypay.com.aepsandroid.adapters.SliderImageAdapter
import aepsapp.easypay.com.aepsandroid.fragments.BottomDmtUserSearchFragment
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.bhaskar.easypaisa.MainActivity
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.model.HomeData
import com.app.bhaskar.easypaisa.mvp.model.HomeFragmentModel
import com.app.bhaskar.easypaisa.mvp.presenter.HomeFragmentPresenter
import com.app.bhaskar.easypaisa.mvp.presenter.HomeFragmentPresenterImpl
import com.app.bhaskar.easypaisa.response_model.SearchRemitterResponse
import com.app.bhaskar.easypaisa.response_model.UserRequiredDataResponse
import com.app.bhaskar.easypaisa.ui.activity.*
import com.app.bhaskar.easypaisa.ui.dialog.DialogOtpVerification
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.google.gson.reflect.TypeToken
import com.morpho.android.usb.USBManager
import com.pa.baseframework.baseview.BaseFragment
import kotlinx.android.synthetic.main.activity_help_desk.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_home_main.*
import kotlinx.android.synthetic.main.layout_no_net_connection.*
import java.text.DecimalFormat
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : BaseFragment(), HomeFragmentPresenter.HomeView {
    override fun doRetriveModel(): HomeFragmentModel {
        return model
    }

    companion object {
        const val TAG = "HomeFragment"
    }

    private var karzaToken: String = ""
    private var alertDialog: AlertDialog? = null
    private var verifiyOtpDialog: DialogOtpVerification? = null
    private var remitterMobile: String = ""
    private var dialogDmt: BottomDmtUserSearchFragment? = null
    private var userData: UserRequiredDataResponse? = null

    @Inject
    lateinit var presenter: HomeFragmentPresenterImpl
    private var arrayHome: ArrayList<HomeData> = ArrayList()
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapterHome: HomeAdapter
    private lateinit var model: HomeFragmentModel
    private lateinit var bannerImageAdapter: SliderImageAdapter
    private var arrayListSlider: ArrayList<UserRequiredDataResponse.Slides> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        model = HomeFragmentModel(getViewActivity())
        presenter = HomeFragmentPresenterImpl(this)
        EasyPaisaApp.component.inject(presenter)
        arrayHome = presenter.doGetHomeList()
        presenter.registerBus()
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    fun alertbox() {
        activity?.let {
            val builder: AlertDialog.Builder = AlertDialog.Builder(it)
            builder.setMessage("Look like you have an older version of application.Please update to get latest features and best experience.")
                .setCancelable(false)
                .setTitle(getString(R.string.new_version_available))
                .setPositiveButton("Update Now",
                    DialogInterface.OnClickListener { dialog, id ->
                        val appPackageName: String = it.packageName
                        try {
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=$appPackageName")
                                )
                            )
                        } catch (anfe: ActivityNotFoundException) {
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                                )
                            )
                        }
                        presenter.doLogout()
                    })
            if (alertDialog == null) {
                alertDialog = builder.create()
            }
            if (!it.isFinishing) {
                alertDialog?.setOnShowListener {
                    alertDialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(
                        ContextCompat.getColor(
                            activity!!,
                            R.color.colorBlack
                        )
                    )
                }

                alertDialog?.let { d ->
                    if (!d.isShowing)
                        d.show()
                }
            }
        }
    }

    private fun chatWithWhatsApp() {
        try {
            val contact = userData?.supportnumber
            var contactTO = "+91 8102926199" // use country code with your phone number
            if (!contact.isNullOrEmpty()) {
                val listContact = contact.split(",")
                contactTO = "+91 " + listContact[0].split("-")[1].trim()
            }

            val url = "https://api.whatsapp.com/send?phone=$contactTO"
            try {
                val pm = getViewActivity().packageManager
                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            } catch (e: PackageManager.NameNotFoundException) {
                Toast.makeText(
                    getViewActivity(),
                    "Whatsapp app not installed in your phone",
                    Toast.LENGTH_SHORT
                ).show()
                e.printStackTrace()
            }
        } catch (e: Exception) {
        }

    }

    private fun initView() {

        linearWhatsApp.setOnClickListener {
            chatWithWhatsApp()
        }
        btnContactUs.setOnClickListener {
            userData?.let {
                val type = object : TypeToken<UserRequiredDataResponse>() {}.type
                startActivity(
                    Intent(getViewActivity(), HelpDeskActivity::class.java).putExtra(
                        Constants.UI.USER_DATA,
                        EasyPaisaApp.getGson().toJson(it, type)
                    )
                )
            }
        }
        tvNotice.isSelected = true
        layoutManager = LinearLayoutManager(getViewActivity(), RecyclerView.VERTICAL, false)
        recyclerviewHome.setHasFixedSize(true)
        recyclerviewHome.layoutManager = layoutManager
        adapterHome = HomeAdapter(getViewActivity(), arrayHome) {

            if (EasyPaisaApp.getLoggedInUser() != null
                && EasyPaisaApp.getLoggedInUser()!!.appversion != Utils.getAppVersionName(
                    activity!!
                )
            ) {
                alertbox()
                return@HomeAdapter
            }

            if (userData == null) {
                return@HomeAdapter
            }

            if (userData?.resetpwd == "default") {
                Utils.showAlert(
                    getViewActivity(),
                    "Password has expired, Please update your password to access the services",
                    "Easy Paisa",
                    View.OnClickListener {
                        //Goto password change screen
                        //presenter.gotoUpdatePassword()
                        startActivityForResult(
                            Intent(
                                getViewActivity(),
                                ChangePasswordActivity::class.java
                            ), Constants.UI.UPDATE_PASS
                        )
                    })
                return@HomeAdapter
            }

            if (userData?.kyc == "pending") {
                Utils.showAlert(
                    getViewActivity(),
                    "Your AePS Service is not active,Please contact to Easy Paisa Team or submit your KYC",
                    "Easy Paisa",
                    View.OnClickListener {
                        //Non KYC agent
                        presenter.gotoAgentKycScreen()
                    })
                return@HomeAdapter
            }

            if (userData?.kyc == "submitted") {
                Utils.showAlert(
                    getViewActivity(),
                    "You are not allowed to access the service.Submitted KYC documents are under process,Please contact to Easy Paisa team",
                    "Easy Paisa",
                    View.OnClickListener {
                    })
                return@HomeAdapter
            }

            if (userData?.kyc == "rejected") {
                Utils.showAlert(
                    getViewActivity(),
                    "Your submitted KYC document has been rejected by the bank,To use services please re-submit your KYC document.",
                    "Easy Paisa",
                    View.OnClickListener {
                        //Non KYC agent
                        presenter.gotoAgentKycScreen()
                    })
                return@HomeAdapter
            }
            presenter.proceedAction(it)
        }
        recyclerviewHome.adapter = adapterHome

        if (!Utils.isNetworkConnected(getViewActivity())) {
            constraintLayoutHomeToolbar.visibility = View.GONE
            layoutMainHome.visibility = View.GONE
            layoutNetConnection.visibility = View.VISIBLE
        } else {
            //getMainWalletBal()
            getHomeData()
        }

        btnRetry.setOnClickListener {
            if (!Utils.isNetworkConnected(getViewActivity())) {
                return@setOnClickListener
            }
            constraintLayoutHomeToolbar.visibility = View.VISIBLE
            layoutMainHome.visibility = View.VISIBLE
            layoutNetConnection.visibility = View.GONE
            //getMainWalletBal()
            getHomeData()
        }

    }

    private fun getMainWalletBal() {
        //get the wallet balance
        activity?.let {
            if (!it.isFinishing) {
                gpMainBalance.visibility = View.GONE
                progressBarMainBalance.visibility = View.VISIBLE
                presenter.goGetMainWalletBalance()
            }
        }
    }

    private fun getHomeData() {
        //get user data
        activity?.let {
            if (!it.isFinishing) {
                progressBarHome.visibility = View.VISIBLE
                layoutMainHome.visibility = View.GONE
                presenter.getUserRequiredData()
            }
        }
    }

    override fun onWalletBalance() {
        val res = doRetriveModel().getLoginDomain().walletBalanceResponse
        gpMainBalance.visibility = View.VISIBLE
        progressBarMainBalance.visibility = View.GONE
        if (res.status == Constants.ApiResponse.RES_SUCCESS) {
            val decim = DecimalFormat("0.00")
            val amount = decim.format(res.data.mainwallet)
            tvWalletMainBalance.text = Utils.formatAmount(amount.toDouble())
        } else {
            tvWalletMainBalance.text = Utils.formatAmount(0.0)
        }
    }

    override fun onUserData() {
        userData = doRetriveModel().getLoginDomain().userRequiredDataResponse
        EasyPaisaApp.setUserRequiredData(userData!!)
        progressBarHome.visibility = View.GONE
        layoutMainHome.visibility = View.VISIBLE

        userData?.let {
            tvNotice.text = it.news
            tvNoticeBoard.text = it.notice
            arrayListSlider.addAll(it.slides)
            bannerImageAdapter = SliderImageAdapter(activity!!, arrayListSlider) {
            }
            sliderViewBanners.sliderAdapter = bannerImageAdapter
            ivNotice.setOnClickListener {
                if (userData?.notice.toString().trim().isEmpty()) {
                    return@setOnClickListener
                }
                var noticeNote = ""
                noticeNote = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(userData?.notice, Html.FROM_HTML_MODE_COMPACT)
                        .toString()
                } else {
                    Html.fromHtml(userData?.notice).toString()
                }
                activity?.let {
                    Utils.showAlert(it, noticeNote, "", View.OnClickListener { })
                }
            }
            linearNotice.setOnClickListener {
                ivNotice.performClick()
            }

            if (EasyPaisaApp.getLoggedInUser()!!.firstTime) {
                val userLoginData = EasyPaisaApp.getLoggedInUser()
                userLoginData?.let { loginData ->
                    loginData.firstTime = false
                    EasyPaisaApp.setLoggedInUser(loginData)
                }
                ivNotice.performClick()
            }
        }

        if (userData?.resetpwd == "default") {
            Utils.showAlert(
                getViewActivity(),
                "Password has expired, Please update your password to access the services",
                "Easy Paisa",
                View.OnClickListener {
                    //Goto password change screen
                    presenter.gotoUpdatePassword()
                })
            return
        }

        if (userData?.kyc == "pending") {
            //Non KYC agent
            presenter.gotoAgentKycScreen()
            return
        }

        if (userData?.kyc == "submitted") {
            Utils.showAlert(
                getViewActivity(),
                "You are not allowed to access the service.Submitted KYC documents are under process,Please contact to Easy Paisa team",
                "Easy Paisa",
                View.OnClickListener {
                })
            return
        }

        if (userData?.kyc == "rejected") {
            Utils.showAlert(
                getViewActivity(),
                "Your submitted KYC document has been rejected by the bank,To use services please re-submit your KYC document.",
                "Easy Paisa",
                View.OnClickListener {
                    //Non KYC agent
                    presenter.gotoAgentKycScreen()
                })
            return
        }

        if (userData?.agent?.everify == "pending") {
            Utils.showAlert(
                getViewActivity(),
                "eKYC verification are needed in order to access the services",
                "Proceed",
                "Cancel",
                View.OnClickListener {
                    //Non KYC agent
                    presenter.gotoAgenteKycScreen()
                }, View.OnClickListener {
                })
            return
        }
    }

    override fun initiateAtmTransaction(type: Int) {
        activity?.let {
            val intent = Intent(getViewActivity(), MicroAtmIciciActivty::class.java)
            intent.putExtra("type", type)
            startActivity(intent)
        }
    }

    override fun openSearchUserForDmt() {
        dialogDmt = BottomDmtUserSearchFragment(getViewActivity()) {
            remitterMobile = it.mobile
            presenter.searchRemitter(it)
        }
        dialogDmt?.showNow(childFragmentManager, "sender")
    }

    override fun onResume() {
        super.onResume()
        if (EasyPaisaApp.getLoggedInUser() != null
            && EasyPaisaApp.getLoggedInUser()!!.appversion != Utils.getAppVersionName(activity!!)
        ) {
            alertbox()
            return
        }
        //getMainWalletBal()
        if (EasyPaisaApp.getNeedUpdateUserDetail()) {
            if (!Utils.isNetworkConnected(getViewActivity())) {
                constraintLayoutHomeToolbar.visibility = View.GONE
                layoutMainHome.visibility = View.GONE
                layoutNetConnection.visibility = View.VISIBLE
            } else {
                EasyPaisaApp.setNeedUpdateUserDetail(false)
                getHomeData()
            }
        }
    }

    override fun onRemitterVerify() {
        val response = doRetriveModel().getLoginDomain().searchRemitterResponse

        when (response.status) {
            Constants.ApiResponse.RES_SUCCESS -> {
                dialogDmt?.let {
                    dialogDmt?.dismiss()
                    dialogDmt = null
                }
                val type = object : TypeToken<SearchRemitterResponse>() {}.type
                val intent = Intent(getViewActivity(), DmtTransactionActivity::class.java)
                intent.putExtra(
                    Constants.UI.REMITTER_RESPONSE,
                    EasyPaisaApp.getGson().toJson(response, type)
                )
                getViewActivity().startActivity(intent)
            }
            Constants.ApiResponse.RES_TXNOTP -> {
                /*Utils.showAlert(
                    getViewActivity(),
                    "OTP verification required to proceed.",
                    "Easy Paisa",
                    View.OnClickListener {
                        dialogDmt?.let {
                            dialogDmt?.dismiss()
                            dialogDmt = null
                        }
                        //Open OTP verification dialog

                    },
                    View.OnClickListener {
                    })*/

                val responsecode = response.transdata.transid

                verifiyOtpDialog = DialogOtpVerification(getViewActivity())
                verifiyOtpDialog?.showOTPDialog({
                    doRetriveModel().getVerifyRemitterReq().token =
                        EasyPaisaApp.getLoggedInUser()?.apptoken.toString()
                    doRetriveModel().getVerifyRemitterReq().userId =
                        EasyPaisaApp.getLoggedInUser()?.id.toString()
                    doRetriveModel().getVerifyRemitterReq().mobile =
                        remitterMobile
                    doRetriveModel().getVerifyRemitterReq().otp = it
                    doRetriveModel().getVerifyRemitterReq().type = "customerverification"
                    doRetriveModel().getVerifyRemitterReq().responsecode = responsecode.toString()
                    presenter.doVerifyRemitter()
                }, {
                    if (verifiyOtpDialog != null) {
                        verifiyOtpDialog?.dismiss()
                        verifiyOtpDialog = null
                    }
                    generateOtp(doRetriveModel().getVerifyRemitterReq().mobile)
                }, {
                    if (verifiyOtpDialog != null) {
                        verifiyOtpDialog?.dismiss()
                        verifiyOtpDialog = null
                    }
                    //finish()
                })
            }
            Constants.ApiResponse.RES_CNF -> {
                Utils.showAlert(
                    getViewActivity(),
                    "Customer Not Found, Do you want to register?",
                    "Easy Paisa",
                    View.OnClickListener {
                        dialogDmt?.let {
                            dialogDmt?.dismiss()
                            dialogDmt = null
                        }
                        startActivity(
                            Intent(
                                getViewActivity(),
                                RegisterRemitterActivity::class.java
                            ).putExtra(Constants.UI.MOBILE_NO, remitterMobile)
                        )
                    }, View.OnClickListener {

                    })
            }
            else -> {

            }
        }
    }

    override fun onVerifyRemitter() {
        val response = doRetriveModel().getLoginDomain().verifyRemitterResponse
        if (response.message == Constants.ApiResponse.RES_SUCCESS) {
            if (verifiyOtpDialog != null && verifiyOtpDialog?.isShowing!!) {
                verifiyOtpDialog?.dismiss()
                verifiyOtpDialog = null
            }
            Utils.showSuccessAlert(getViewActivity(), response.message, View.OnClickListener {

            })
        } else {
            Utils.showAlert(getViewActivity(), response.message, "", View.OnClickListener { })
        }
    }

    private fun generateOtp(mobile: String) {
        doRetriveModel().getGenerateOtpReq().token = EasyPaisaApp.getLoggedInUser()?.apptoken!!
        doRetriveModel().getGenerateOtpReq().userId = EasyPaisaApp.getLoggedInUser()?.id.toString()
        doRetriveModel().getGenerateOtpReq().type = "generateOtp"
        doRetriveModel().getGenerateOtpReq().mobile = mobile
        presenter.sendOtp()
    }


    override fun onGenerateOtp() {
        val response = doRetriveModel().getLoginDomain().generateOtpResponse
        if (response.status == Constants.ApiResponse.RES_TXNOTP) {
            val responsecode = response.transdata.transid

            verifiyOtpDialog = DialogOtpVerification(getViewActivity())
            verifiyOtpDialog?.showOTPDialog({
                doRetriveModel().getVerifyRemitterReq().token =
                    EasyPaisaApp.getLoggedInUser()?.apptoken.toString()
                doRetriveModel().getVerifyRemitterReq().userId =
                    EasyPaisaApp.getLoggedInUser()?.id.toString()
                doRetriveModel().getVerifyRemitterReq().mobile =
                    remitterMobile
                doRetriveModel().getVerifyRemitterReq().otp = it
                doRetriveModel().getVerifyRemitterReq().type = "customerverification"
                doRetriveModel().getVerifyRemitterReq().responsecode = responsecode.toString()
                presenter.doVerifyRemitter()
            }, {
                if (verifiyOtpDialog != null) {
                    verifiyOtpDialog?.dismiss()
                    verifiyOtpDialog = null
                }
                generateOtp(remitterMobile)
            }, {
                if (verifiyOtpDialog != null) {
                    verifiyOtpDialog?.dismiss()
                    verifiyOtpDialog = null
                }
            })

        } else {
            Utils.showAlert(getViewActivity(), response.message, "", View.OnClickListener { })
        }
    }

    override fun onAppLogout() {
        activity?.let {
            if (!it.isFinishing) {
                EasyPaisaApp.setClearSession()
                activity!!.finishAffinity()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unRegisterBus()
    }

    override fun getUserData(): UserRequiredDataResponse? {
        return userData
    }
}
