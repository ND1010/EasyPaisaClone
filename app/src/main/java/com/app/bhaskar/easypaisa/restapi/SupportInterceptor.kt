package com.app.bhaskar.easypaisa.restapi

import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor: Interceptor {

    /**
     * Interceptor class for setting of the headers for every request
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request?.newBuilder()
                ?.addHeader("Content-Type", "application/x-www-form-urlencoded")
                ?.addHeader("device_id", EasyPaisaApp.getDeviceId())
//                ?.addHeader("access_token", if (EasyPaisaApp.getLoggedInUser()==null) "" else EasyPaisaApp.getLoggedInUser().access_token)
                ?.addHeader("device_type", "android")
//                ?.addHeader("device_token", EasyPaisaApp.getDeviceToken())
                ?.build()
        return chain.proceed(request)
    }
}