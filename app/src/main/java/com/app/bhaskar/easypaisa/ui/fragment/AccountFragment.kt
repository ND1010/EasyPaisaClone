package com.app.bhaskar.easypaisa.ui.fragment


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.application.EasyPaisaApp.Companion.setClearSession
import com.app.bhaskar.easypaisa.mvp.model.AccountFragmentModel
import com.app.bhaskar.easypaisa.mvp.presenter.AccountFragmentPresenter
import com.app.bhaskar.easypaisa.mvp.presenter.AccountFragmentPresenterImpl
import com.app.bhaskar.easypaisa.ui.activity.AgentKycRegistration
import com.app.bhaskar.easypaisa.ui.activity.ChangePasswordActivity
import com.app.bhaskar.easypaisa.ui.activity.HelpDeskActivity
import com.app.bhaskar.easypaisa.ui.activity.LoginActivity
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.pa.baseframework.baseview.BaseFragment
import kotlinx.android.synthetic.main.fragment_accountragment.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class AccountFragment : BaseFragment(), View.OnClickListener,
    AccountFragmentPresenter.AccountFragmentView {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        model = AccountFragmentModel(getViewActivity())
        presenter = AccountFragmentPresenterImpl(this)
        EasyPaisaApp.component.inject(presenter)
        return inflater.inflate(R.layout.fragment_accountragment, container, false)
    }

    private lateinit var model: AccountFragmentModel

    @Inject
    lateinit var presenter: AccountFragmentPresenterImpl


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvLblUserPass.setOnClickListener(this)
        tvUserPass.setOnClickListener(this)
        btnKyc.setOnClickListener(this)
        btnContactUs.setOnClickListener(this)

        fillDetails()
    }

    override fun onResume() {
        super.onResume()
        if (EasyPaisaApp.getUserRequiredData()!=null
            && (EasyPaisaApp.getUserRequiredData()!!.kyc == "pending"
                    ||EasyPaisaApp.getUserRequiredData()!!.kyc == "rejected") ) {
            cardCompleteYourKyc.visibility = View.VISIBLE
            cardKycDetail.visibility = View.GONE
        } else {
            cardCompleteYourKyc.visibility = View.GONE
            cardKycDetail.visibility = View.VISIBLE
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tvLblUserPass -> {
                val intent = Intent(getViewActivity(), ChangePasswordActivity::class.java)
                startActivity(intent)
            }
            R.id.tvUserPass -> {
                val intent = Intent(getViewActivity(), ChangePasswordActivity::class.java)
                startActivity(intent)
            }
            R.id.btnKyc -> {
                startActivity(Intent(getViewActivity(), AgentKycRegistration::class.java))
            }
            R.id.btnContactUs -> {
                startActivity(Intent(getViewActivity(), HelpDeskActivity::class.java))
            }
            R.id.tvUserLogout -> {
                logoutFromApp()
            }
            R.id.tvLblUserLogout -> {
                logoutFromApp()
            }
        }
    }

    private fun logoutFromApp() {
        Utils.showAlert(
            getViewActivity(),
            "Are you sure you wants to logout from Easy Paisa?",
            "Logout",
            View.OnClickListener {
                presenter.doLogout()
            }, View.OnClickListener {
            })
    }

    private fun fillDetails() {
        val user = EasyPaisaApp.getLoggedInUser()!!
        tvUserName.text = user.name
        tvUserNameTool.text = user.name
        tvUserAgentCode.text = user.state
        tvUserMobile.text = user.mobile
        tvUserEmail.text = user.email
        tvUserAddress.text = user.address
        tvUserShop.text = user.shopname
        tvUserPan.text = user.pancard
        tvUserAadhar.text = user.aadharcard

        tvLblUserPass.setOnClickListener(this)
        tvUserPass.setOnClickListener(this)
        tvUserLogout.setOnClickListener(this)
        tvLblUserLogout.setOnClickListener(this)

        if (EasyPaisaApp.getUserRequiredData()!=null
            && (EasyPaisaApp.getUserRequiredData()!!.kyc == "pending"
                    ||EasyPaisaApp.getUserRequiredData()!!.kyc == "rejected") ) {
            cardCompleteYourKyc.visibility = View.VISIBLE
            cardKycDetail.visibility = View.GONE
        } else {
            cardCompleteYourKyc.visibility = View.GONE
            cardKycDetail.visibility = View.VISIBLE
        }
    }

    override fun doRetriveModel(): AccountFragmentModel {
        return model
    }

    override fun onLogoutSuccess() {
        setClearSession()
        startActivity(Intent(getViewActivity(), LoginActivity::class.java))
        activity!!.finishAffinity()
    }
}
