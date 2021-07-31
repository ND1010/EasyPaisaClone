package com.app.bhaskar.easypaisa.ui.activity

import aepsapp.easypay.com.aepsandroid.adapters.AdapterBeneficiary
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.response_model.SearchRemitterResponse
import com.app.bhaskar.easypaisa.utils.Constants
import com.google.gson.reflect.TypeToken
import com.pa.baseframework.baseview.BaseActivity
import kotlinx.android.synthetic.main.activity_beneficiary.*
import kotlinx.android.synthetic.main.toolbar_simple.*


class BeneficiaryActivity : BaseActivity() {

    companion object{
        const val TAG ="BeneficiaryActivity"
    }
    private val arraylistBene: ArrayList<SearchRemitterResponse.Userdata.Benelist> = ArrayList()
    private lateinit var adapterBeneficiary: AdapterBeneficiary
    lateinit var remitterResponse: SearchRemitterResponse
    private lateinit var layoutManager : LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beneficiary)
        initViews()
    }

    private fun initViews() {
        ivHomeBack.setOnClickListener { onBackPressed() }
            tvToolbarTitle.text = "Select Beneficiary"
        val type = object : TypeToken<SearchRemitterResponse>() {}.type
        remitterResponse = EasyPaisaApp.getGson()
            .fromJson(intent.getStringExtra(Constants.UI.REMITTER_RESPONSE), type)

        layoutManager = LinearLayoutManager(this@BeneficiaryActivity)
        adapterBeneficiary = AdapterBeneficiary(this@BeneficiaryActivity,arraylistBene){
            val returnIntent = Intent()
            val type = object : TypeToken<SearchRemitterResponse.Userdata.Benelist>() {}.type
            val json  =  EasyPaisaApp.getGson().toJson(it,type)
            returnIntent.putExtra(Constants.UI.SELECT_BENE, json)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
        recyclerviewBene.setHasFixedSize(true)
        recyclerviewBene.layoutManager = layoutManager
        recyclerviewBene.adapter = adapterBeneficiary
        arraylistBene.addAll(remitterResponse.userdata.benelist)
        adapterBeneficiary.setBank(arraylistBene)

        if (arraylistBene.isEmpty()){
            linear_add_new_bene.visibility = View.VISIBLE
        }else{
            linear_add_new_bene.visibility = View.GONE
        }
        linear_add_new_bene.setOnClickListener {
            val type = object : TypeToken<SearchRemitterResponse>() {}.type
            val json  = EasyPaisaApp.getGson().toJson(remitterResponse,type)
            startActivityForResult(Intent(getViewActivity(),AddNewBeneActivity::class.java).putExtra(Constants.UI.REMITTER_RESPONSE,json),Constants.UI.CODE_ADD_BENE)
        }

        tvAddBene.setOnClickListener {
            linear_add_new_bene.performClick()
        }
    }

    override fun onNetworkStateChange(isConnect: Boolean) {
    }

    override fun getViewActivity(): Activity {
        return this@BeneficiaryActivity
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.UI.CODE_ADD_BENE && resultCode == Activity.RESULT_OK){
            if (data!=null && data.hasExtra(Constants.UI.NEW_BENE)){
                val type = object : TypeToken<SearchRemitterResponse.Userdata.Benelist>() {}.type
                arraylistBene.add(EasyPaisaApp.getGson().fromJson(data.getStringExtra(Constants.UI.NEW_BENE),type))
                adapterBeneficiary.notifyDataSetChanged()
            }
            if (arraylistBene.isEmpty()){
                linear_add_new_bene.visibility = View.VISIBLE
            }else{
                linear_add_new_bene.visibility = View.GONE
            }
        }
    }
}