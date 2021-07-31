package com.app.bhaskar.easypaisa.ui.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.response_model.UserRequiredDataResponse
import com.app.bhaskar.easypaisa.utils.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_help_desk.*
import java.util.*

class HelpDeskActivity : AppCompatActivity() {

    private var userData :UserRequiredDataResponse?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_desk)
        val type = object : TypeToken<UserRequiredDataResponse>() {}.type
        val strUserData = intent.getStringExtra(Constants.UI.USER_DATA)
        userData = Gson().fromJson(strUserData,type)
        fillData()
        copyrights.text = String.format(getString(R.string.copyright_2021_easypaisa),getCurrentYear())
        callSales1.setOnClickListener {
            val phone = "+91${tvSales1Call.text}"
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
            startActivity(intent)
        }
        callSales2.setOnClickListener {
            val phone = "+91${tvSales2Call.text}"
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
            startActivity(intent)
        }
        callSales3.setOnClickListener {
            val phone = "+918102926201"
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
            startActivity(intent)
        }

        callSupport.setOnClickListener {
            val phone = "+91${tvSupport}"
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
            startActivity(intent)
        }
        tvEmailSupport.setOnClickListener {
            openEmailIntent()
        }
    }

    private fun fillData() {
        tvEmailSupport.text = userData?.supportemail
        val contact = userData?.supportnumber
        if (!contact.isNullOrEmpty()){
            val listContact = contact.split(",")
            tvSales1Call.text = listContact[0].split("-")[1]
            tvSales2Call.text = listContact[1].split("-")[1]
            tvSupport.text = listContact[2].split("-")[1]
        }
    }

    private fun openEmailIntent() {
        val emailId = arrayOf(userData?.supportemail)

        val subject = "Easy Paisa Help| ${EasyPaisaApp.getLoggedInUser()?.name}"
        val bodyText = "Your query here...\n\n\n"+
                "\nName: ${EasyPaisaApp.getLoggedInUser()?.name}" +
                "\nMobile Number: ${EasyPaisaApp.getLoggedInUser()?.mobile}" +
                "\n" +
                "----------------------------------------------------\n"
        val mailto = "info@easypaisa.in" +
                "?subject=" + Uri.encode(subject) +
                "&body=" + Uri.encode(bodyText)

        try {
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            emailIntent.putExtra(Intent.EXTRA_EMAIL, emailId)
            emailIntent.putExtra(Intent.EXTRA_TEXT, bodyText)
            emailIntent.data = Uri.parse("mailto:")

            startActivity(emailIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this@HelpDeskActivity, "There are no email clients installed.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCurrentYear(): String {
        return java.lang.String.valueOf(Calendar.getInstance().get(Calendar.YEAR))
    }
}