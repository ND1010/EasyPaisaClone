package com.app.bhaskar.easypaisa.ui.activity

import aepsapp.easypay.com.aepsandroid.adapters.AadharBankAdapter
import aepsapp.easypay.com.aepsandroid.adapters.BankAdapter
import aepsapp.easypay.com.aepsandroid.adapters.BankYesAdapter
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.mvp.model.SelectBankActivityModel
import com.app.bhaskar.easypaisa.mvp.presenter.SelectBankPresenter
import com.app.bhaskar.easypaisa.mvp.presenter.SelectbankActivityPresenterImpl
import com.app.bhaskar.easypaisa.response_model.UserRequiredDataResponse
import com.pa.baseframework.baseview.BaseActivity
import kotlinx.android.synthetic.main.activity_select_bank_activty.*
import kotlinx.android.synthetic.main.toolbar_simple.*
import javax.inject.Inject

class SelectBankActivty : BaseActivity(), SelectBankPresenter.SelectBankView {
    override fun doRetriveModel(): SelectBankActivityModel {
        return model
    }

    companion object {
        const val TAG = "SelectBankActivty"
    }

    private var aepsServicefor: String = ""

    @Inject
    lateinit var presenter: SelectbankActivityPresenterImpl
    lateinit var model: SelectBankActivityModel
    private var arrayBank = ArrayList<UserRequiredDataResponse.Aepsbank>()
    private var aadharArrayBank = ArrayList<UserRequiredDataResponse.Aadharbank>()
    private var aadharArrayBankYesAeps = ArrayList<UserRequiredDataResponse.Yesbank>()
    lateinit var adapterBank: BankAdapter
    lateinit var adapterBankYesBank: BankYesAdapter
    lateinit var adapterAadharBank: AadharBankAdapter
    lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_bank_activty)
        model = SelectBankActivityModel(getViewActivity())
        presenter = SelectbankActivityPresenterImpl(this)
        EasyPaisaApp.component.inject(presenter)
        tvToolbarTitle.text = getString(R.string.select_your_bank)
        ivHomeBack.setOnClickListener { onBackPressed() }
        if (intent.hasExtra("selectedAepsTransaction")) {
            aepsServicefor = intent.getStringExtra("selectedAepsTransaction") ?: ""
            when (aepsServicefor) {
                getString(R.string.ser_aadhar_pay) -> {
                    aadharArrayBank =
                        EasyPaisaApp.getUserRequiredData()?.aadharbanks as ArrayList<UserRequiredDataResponse.Aadharbank>
                    layoutManager =
                        LinearLayoutManager(getViewActivity(), RecyclerView.VERTICAL, false)
                    adapterAadharBank = AadharBankAdapter(getViewActivity(), aadharArrayBank) {
                        val intent = Intent()
                        intent.putExtra("selectedBank", it)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                    recyclerviewBank.layoutManager = layoutManager
                    recyclerviewBank.adapter = adapterAadharBank

                    edtBankSearch.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(p0: Editable?) {

                        }

                        override fun beforeTextChanged(
                            p0: CharSequence?,
                            p1: Int,
                            p2: Int,
                            p3: Int
                        ) {
                        }

                        override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            adapterAadharBank.filter.filter(char)
                        }
                    })
                }
                "yesaeps"->{
                    aadharArrayBankYesAeps =
                        EasyPaisaApp.getUserRequiredData()?.yesbanks as ArrayList<UserRequiredDataResponse.Yesbank>
                    layoutManager =
                        LinearLayoutManager(getViewActivity(), RecyclerView.VERTICAL, false)
                    adapterBankYesBank = BankYesAdapter(getViewActivity(), aadharArrayBankYesAeps) {
                        val intent = Intent()
                        intent.putExtra("selectedBank", it)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                    recyclerviewBank.layoutManager = layoutManager
                    recyclerviewBank.adapter = adapterBankYesBank

                    edtBankSearch.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(p0: Editable?) {

                        }

                        override fun beforeTextChanged(
                            p0: CharSequence?,
                            p1: Int,
                            p2: Int,
                            p3: Int
                        ) {
                        }

                        override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            adapterBankYesBank.filter.filter(char)
                        }
                    })
                }
                else -> {
                    arrayBank =
                        EasyPaisaApp.getUserRequiredData()?.aepsbanks as ArrayList<UserRequiredDataResponse.Aepsbank>
                    layoutManager =
                        LinearLayoutManager(getViewActivity(), RecyclerView.VERTICAL, false)
                    adapterBank = BankAdapter(getViewActivity(), arrayBank) {
                        val intent = Intent()
                        intent.putExtra("selectedBank", it)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                    recyclerviewBank.layoutManager = layoutManager
                    recyclerviewBank.adapter = adapterBank

                    edtBankSearch.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(p0: Editable?) {

                        }

                        override fun beforeTextChanged(
                            p0: CharSequence?,
                            p1: Int,
                            p2: Int,
                            p3: Int
                        ) {
                        }

                        override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            adapterBank.filter.filter(char)
                        }
                    })
                }
            }

        }


    }

    override fun getViewActivity(): Activity {
        return this@SelectBankActivty
    }

    override fun onNetworkStateChange(isConnect: Boolean) {

    }
}
