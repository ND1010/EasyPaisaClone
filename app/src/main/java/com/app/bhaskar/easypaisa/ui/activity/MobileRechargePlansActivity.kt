package com.app.bhaskar.easypaisa.ui.activity

import aepsapp.easypay.com.aepsandroid.adapters.MobileRechargePlanAdapter
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.mvp.model.MobileRechargePlansModel
import com.app.bhaskar.easypaisa.mvp.presenter.MobileRechargePlanPresenterImpl
import com.app.bhaskar.easypaisa.mvp.presenter.MobileRechargePlansPresenter
import com.app.bhaskar.easypaisa.response_model.RechargeProviderResponse
import com.app.bhaskar.easypaisa.response_model.MobileRechargePlanResponse
import com.app.bhaskar.easypaisa.utils.Constants
import com.google.gson.reflect.TypeToken
import com.pa.baseframework.baseview.BaseActivity
import kotlinx.android.synthetic.main.activity_mobile_recharge_plans.*
import javax.inject.Inject


class MobileRechargePlansActivity : BaseActivity(),
    MobileRechargePlansPresenter.MobileRechargePlansView {

    override fun doRetriveModel(): MobileRechargePlansModel {
        return model
    }

    companion object {
        const val TAG = "MobileRechargePlansActivity"
    }

    private var mobile: String = ""
    private var selectedoperator: RechargeProviderResponse.Message.Provider? = null
    private lateinit var model: MobileRechargePlansModel
    @Inject
    lateinit var presenter: MobileRechargePlanPresenterImpl
    lateinit var mobileRechargePlanAdapter: MobileRechargePlanAdapter
    lateinit var layoutManager: LinearLayoutManager
    private var arrayListPlans = ArrayList<MobileRechargePlanResponse.Plan>()


    override fun getViewActivity(): Activity {
        return this@MobileRechargePlansActivity
    }

    override fun onNetworkStateChange(isConnect: Boolean) {
        if (!isConnect) {
            showToast(getString(R.string.no_internet_connection_please_check_internet_connection))
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobile_recharge_plans)
        model = MobileRechargePlansModel(getViewActivity())
        presenter = MobileRechargePlanPresenterImpl(this)
        EasyPaisaApp.component.inject(presenter)
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        if (intent.hasExtra("operator")) {
            val type = object : TypeToken<RechargeProviderResponse.Message.Provider>() {}.type
            selectedoperator = EasyPaisaApp.getGson()
                .fromJson<RechargeProviderResponse.Message.Provider>(
                    intent.getStringExtra("operator"),
                    type
                )
            mobile = intent.getStringExtra("mobile") ?: ""
        }
        tvBrowsPlan.text = "For ${selectedoperator?.name}"
        layoutManager = LinearLayoutManager(getViewActivity(), RecyclerView.VERTICAL, false)
        mobileRechargePlanAdapter = MobileRechargePlanAdapter(getViewActivity(), arrayListPlans) {
            val returnIntent = Intent()
            val type = object : TypeToken<MobileRechargePlanResponse.Plan>() {}.type
            returnIntent.putExtra("result", EasyPaisaApp.getGson().toJson(it, type))
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
        recyclerviewPlans.setHasFixedSize(true)
        recyclerviewPlans.layoutManager = layoutManager
        recyclerviewPlans.adapter = mobileRechargePlanAdapter

        val req = doRetriveModel().getMobilRechargePlanRequest().copy(
            number = mobile,
            plantype = "SPL",
            provider = selectedoperator?.id.toString(),
            token = EasyPaisaApp.getLoggedInUser()?.apptoken.toString(),
            transactionType = "planfinder",
            userId = EasyPaisaApp.getLoggedInUser()?.id.toString()
        )
        presenter.getPlans(req)

        ivClose.setOnClickListener {
            onBackPressed()
        }
    }


    override fun showProgress() {
        progressPlans.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressPlans.visibility = View.GONE
    }

    override fun onMobilePlan() {
        val res = doRetriveModel().getDomain().mobileRechargePlanResponse
        if (res.status == Constants.ApiResponse.RES_SUCCESS) {
            tvPlanMessage.visibility =View.GONE
            progressPlans.visibility = View.GONE
            recyclerviewPlans.visibility = View.VISIBLE
            arrayListPlans.addAll(res.plans)
            mobileRechargePlanAdapter.notifyDataSetChanged()
        } else {
            tvPlanMessage.visibility =View.VISIBLE
            progressPlans.visibility = View.GONE
            recyclerviewPlans.visibility = View.GONE
            showToast(res.message)
        }
    }
}
