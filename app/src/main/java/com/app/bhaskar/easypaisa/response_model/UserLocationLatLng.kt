/**
 *Created by dhruv on 25-06-2019.
 */
package com.app.bhaskar.easypaisa.response_model


/**
 *Created by dhruv on 25-06-2019.
 */
data class UserLocationLatLng(
        var latitude: Double = 0.0,
        var longitude: Double = 0.0,
        var isMockLocation: Boolean=false)