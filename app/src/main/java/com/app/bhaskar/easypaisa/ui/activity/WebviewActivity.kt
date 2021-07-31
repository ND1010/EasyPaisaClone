package com.app.bhaskar.easypaisa.ui.activity

import android.app.Activity
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.webkit.*
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.utils.Utils
import com.pa.baseframework.baseview.BaseActivity
import kotlinx.android.synthetic.main.activity_webview.*
import org.apache.http.HttpResponse
import org.apache.http.NameValuePair
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.conn.scheme.Scheme
import org.apache.http.conn.ssl.SSLSocketFactory
import org.apache.http.impl.client.BasicResponseHandler
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicNameValuePair
import java.net.URLEncoder


class WebviewActivity : BaseActivity() {

    var url: String ="google.com"
    var shopName: String =""
    var panCardNo: String =""
    var mobileNumber: String =""
    var cpCode: String =""
    var cs: String =""
    var sscode: String =""
    var agentCode: String =""
    private var currentUrl: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        initView()
    }

    override fun getViewActivity(): Activity {
        return this@WebviewActivity
    }

    private fun initView() {
        url  = intent.getStringExtra("loadWebUrl")?:""
        shopName  = intent.getStringExtra("shopName")?:""
        panCardNo  = intent.getStringExtra("panCardNo")?:""
        mobileNumber  = intent.getStringExtra("mobileNumber")?:""
        cpCode  = intent.getStringExtra("cpCode")?:""
        cs  = intent.getStringExtra("cs")?:""
        sscode  = intent.getStringExtra("sscode")?:""
        agentCode  = intent.getStringExtra("agentCode")?:""


        val appCachePath = applicationContext.cacheDir.absolutePath
        //Cookie manager for the webview
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        webview.settings.allowFileAccess =true
        webview.settings.javaScriptEnabled = true
        webview.settings.domStorageEnabled = true
        webview.settings.setSupportMultipleWindows(true)
        webview.settings.javaScriptCanOpenWindowsAutomatically = true


        webview.webChromeClient = WebChromeClient()
        if (Build.VERSION.SDK_INT >= 21) {
            webview.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            CookieManager.getInstance().setAcceptThirdPartyCookies(webview, true)
        }

        loadWebview()
        swipToRefresh.isEnabled =false
        /*swipToRefresh.setOnRefreshListener {
            webview.loadUrl(url)
            if (currentUrl.isNotEmpty()) {
                webview.loadUrl(currentUrl)
            } else {
                webview.loadUrl(url)
            }
        }*/
    }

    private fun loadWebview() {

        webview.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                //swipToRefresh.isRefreshing = false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                currentUrl = url!!
                hideProgress()
                Log.e("TAG", "onPageFinished: $currentUrl")
                view?.loadUrl("javascript:load();")
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                /*swipToRefresh.visibility = View.VISIBLE*/
                //progressBar.visibility = View.VISIBLE
                showProgress()
                Log.e("TAG", "onPageStarted: $url")
            }
        }

//        postData()
//        webview.postUrl("https://paisanikal1e.easypay.co.in/", getPostData().toByteArray())

        Utils.showAlertDevice(
            getViewActivity(),
            "Use only Mantra device for eKYC verification in mobile.",
            "Proceed",
            "Cancel",
            View.OnClickListener {
                //Non KYC agent
                webview.loadData(formData(),"text/html", "UTF-8")
            },View.OnClickListener {
                finish()
            })
    }

    override fun onNetworkStateChange(isConnect: Boolean) {
        if (isConnect) {
            swipToRefresh.visibility = View.VISIBLE
            webview.visibility = View.VISIBLE
            ivNoInternet.visibility = View.GONE
            progressBar.visibility = View.GONE
            hideProgress()
            //webview.loadUrl(url)
        } else {
            swipToRefresh.visibility = View.GONE
            webview.visibility = View.GONE
            ivNoInternet.visibility = View.VISIBLE
        }
    }

    fun postData() {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val nameValuePairs: MutableList<NameValuePair> = ArrayList()
        nameValuePairs.add(BasicNameValuePair("sid", "7463803480"))
        nameValuePairs.add(BasicNameValuePair("ts", "1621914831000"))
        nameValuePairs.add(BasicNameValuePair("cs", "hKm/aWMvBr2KH4sqbLvbvcrVbf8T2aXFX8YVI5dlg73LH/3CHuRRND+zMdsEbMeWWeJTXwJA+U5I6bDuvoic5Q=="))
        nameValuePairs.add(BasicNameValuePair("panCardNo", "AWRPN6159N"))
        nameValuePairs.add(BasicNameValuePair("mobileNumber", mobileNumber))
        nameValuePairs.add(BasicNameValuePair("aid", "EPM35354193453434"))
        nameValuePairs.add(BasicNameValuePair("cid", "EASNAV6409"))
        nameValuePairs.add(BasicNameValuePair("shopName", "E-Security"))
        nameValuePairs.add(BasicNameValuePair("returnUrl", "https://retail.easypaisa.in/&v=3&its=1621914832658"))
        val httpclient: HttpClient = DefaultHttpClient()
        val httppost = HttpPost(url)
        httppost.addHeader("content-type", "application/x-www-form-urlencoded")
        httpclient.getConnectionManager().getSchemeRegistry().register(
            Scheme("https", SSLSocketFactory.getSocketFactory(), 443)
        )
        httppost.setEntity(UrlEncodedFormEntity(nameValuePairs))

        val response: HttpResponse = httpclient.execute(httppost)
        val data = BasicResponseHandler().handleResponse(response)
        webview.loadData(data, "text/html", "utf-8");
    }

    private fun getPostData():String{
        return "shopName=${URLEncoder.encode(shopName, "UTF-8")}" +
                "&panCardNo=${URLEncoder.encode(panCardNo, "UTF-8")}"+
                "&mobileNumber=${URLEncoder.encode(mobileNumber, "UTF-8")}" +
                "&cs=${URLEncoder.encode(cs, "UTF-8")}"+
                "&cpCode=${URLEncoder.encode(cpCode, "UTF-8")}"+
                "&sscode=${URLEncoder.encode(sscode, "UTF-8")}"+
                "&agentCode=${URLEncoder.encode(agentCode, "UTF-8")}"
    }


    //https://paisanikal1e.easypay.co.in/epyesbc/agent/login/ekyc/sso-agent
    private fun formData():String{
        return "<script>\n" +
                "function load()\n" +
                "{\n" +
                "document.EKYCFORM.submit()\n" +
                "document.getElementById(\"btnsubmit\").style.visibility = \"hidden\";"+
                "}\n" +
                "</script>" +
                "<body>"+
                "<form id=\"EKYCFORM\" name=\"EKYCFORM\" action =\"$url\" method=\"POST\">\n" +
                "        <input name=\"shopName\" value=\"$shopName\"  type=\"hidden\">\n" +
                "        <input name=\"panCardNo\" value=\"$panCardNo\"   type=\"hidden\">\n" +
                "        <input name=\"mobileNumber\" value=\"$mobileNumber\"  type=\"hidden\">\n" +
                "        <input name=\"cs\" value=\"$cs\"  type=\"hidden\">\n" +
                "        <input name=\"sscode\" value=\"$sscode\"  type=\"hidden\">\n" +
                "        <input name=\"cpCode\" value=\"$cpCode\"  type=\"hidden\">\n" +
                "        <input name=\"agentCode\" value=\"$agentCode\"  type=\"hidden\">\n" +
                "        <button type=\"submit\" form=\"EKYCFORM\" id=\"btnsubmit\" type=\"hidden\">submit</button>" +
                "    </form>"+
                "</body>"
    }

}
