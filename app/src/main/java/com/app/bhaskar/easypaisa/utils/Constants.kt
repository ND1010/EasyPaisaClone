package com.app.bhaskar.easypaisa.utils

object Constants {

    //    API const
    const val API_LOGIN = "userLogin"
    const val API_SIGN_UP = "UserSignup"

    //    api header
    const val HEADER_CONTENT_TYPE = "Content-Type: application/x-www-form-urlencoded"

    interface ApiHeaders {
        companion object {
            const val API_TYPE_JSON = "application/json"
            const val AUTHORIZATION = "authorization"
        }
    }

    interface  MediaType{
        companion object{
            const val IMAGE = 0
            const val DOWNLOAD = 2
        }
    }

    interface MicroAtm {
        companion object{
            const val MICRO_WITHDRWAL =2
            const val MICRO_BALANCE =4
            const val MICRO_MINI_STATEMENT =7
            const val MICRO_DEPOSIT =3
            const val MICRO_RESET_PIN =10
            const val MICRO_CHANGE_PIN =8

            const val LIST_DATA = "LIST"
            const val SUPER_MERCHANTID = "SUPER_MERCHANTID"
            const val MERCHANT_USERID = "MERCHANT_USERID"
            const val MERCHANT_PASSWORD = "MERCHANT_PASSWORD"
            const val AMOUNT = "AMOUNT"
            const val AMOUNT_EDITABLE = "AMOUNT_EDITABLE"
            const val REMARKS = "REMARKS"
            const val TXN_ID = "TXN_ID"
            const val MOBILE_NUMBER = "MOBILE_NUMBER"
            const val IMEI = "IMEI"
            const val LATITUDE = "LATITUDE"
            const val LONGITUDE = "LONGITUDE"
            const val TYPE = "TYPE"
            const val MICROATM_MANUFACTURER = "MICROATM_MANUFACTURER"
            const val MICROATM_DATE_PREF = "MICROATM_DATE_PREF"
            const val RES_CODE = 12345

            const val DEVICE_MAC = "DEVICE_MAC"
            const val DOWNLOAD_AID_PREF = "DOWNLOAD_AID_PREF"
            const val EMV_TAG_PREF = "EMV_TAG_PREF"
            const val TRANS_ID = "TRANS_ID"
            const val TRANS_AMOUNT = "TRANS_AMOUNT"
            const val RRN = "RRN"
            const val MESSAGE = "MESSAGE"
            const val TRANS_STATUS = "TRANS_STATUS"
            const val TRANS_TYPE = "TRANS_TYPE"
            const val BALANCE_AMOUNT = "BALANCE_AMOUNT"
            const val FP_TRANS_ID = "FP_TRANS_ID"
            const val LIST = "LIST"
            const val CARD_TYPE = "CARD_TYPE"
            const val BANK_NAME = "BANK_NAME"
            const val CARD_NUM = "CARD_NAME"
            const val TERMINAL_ID = "TERMINAL_ID"
            const val ERROR_MSG = "ERROR_MSG"
        }
    }
    interface ApiMethod {
        companion object {
            const val API_LOGIN = "auth/check"
            const val API_SEND_TOKEN = "auth/reset/request"
            const val API_FORGOT_PASSWORD = "auth/reset/update"
            const val API_WALLET_BAL = "user/getbalance"
            const val API_USER_REQ_DATA = "faeps/getrequireddata"
            const val API_AGNET_KYC = "user/complete/kyc"
            const val API_FING_PAY_AEPS = "faeps/transaction"
            const val API_MOBILE_PROVIDER = "recharge/provider"
            const val API_MOBILE_RECHARGE = "recharge/transaction"
            const val API_ELECTRICITY_STATE = "billpay/state"
            const val API_ELECTRICITY_BOARD = "billpay/provider"
            const val API_ELECTRICITY_PAYBILL = "billpay/payment"
            const val API_PANCARD_DETAIL = "pancard/providerdata"
            const val API_UTI_TRANSACTION = "pancard/transaction"
            const val API_CHANGE_PASS = "user/password/change"
            const val API_AEPS_TXN = "faeps/easy/transaction"
            const val API_REGISTRATION = "auth/register"
            const val API_TXN_HISTORY = "user/transaction"
            const val API_FUND_TRANSACTION = "fund/transaction"
            const val API_LOAD_DATA = "fund/getdata"
            const val API_DMT_TXN = "dmt/transaction"
            const val API_MICRO_ATM = "matm/initiate"
            const val API_MICRO_ATM_UPDATE = "matm/update"
            const val API_YES_BANK_AEPS = "faeps/yesbank/transaction"
            const val API_GET_OTP = "auth/register/otp"
            const val API_LOGOUT = "auth/logout"
            const val API_OKYC_DATA = "aadhardata/set"
            const val API_VERIFY_PAN = "user/pan/verify"
            //Karza
            const val URL_GENERATE_TOKEN = "v3/get-jwt"
            const val URL_ORDER_DEVICE = "stock/transaction"
            const val URL_EKYC_AUTH = "faeps/registration"
            const val API_AGENT_KYC_DETAIL = "user/yes/kyc"

        }
    }

    interface AepsServices {
        companion object {
            const val AEPS_WITHDRAW = "aeps_withdraw"
            const val AEPS_BAL_INFO = "aeps_bal_info"
        }
    }

    interface AvailableService {
        companion object {
            const val SERVICE_EASYPAY_AEPS = 1
            const val SERVICE_AEPS_ICICI_EP = 6
            const val SERVICE_FINO_AEPS = 2
            const val SERVICE_ICICI_AEPS = 3
            const val SERVICE_MICRO_ATM = 4
            const val SERVICE_MONEY_TRANS = 5

            const val SERVICE_MOBILE_RECHARGE = 7
            const val SERVICE_BILL_ELECTRICITY = 8
            const val SERVICE_DTH = 9
            const val SERVICE_DATA_CARD = 10
            const val SERVICE_UTI_PAN = 11

            const val SERVICE_AADHARPAY = 12
            const val SERVICE_MINI_STATEMENT = 13
            const val SERVICE_DEPOSITE = 14

            const val SERVICE_LPG_GAS = 15
            const val SERVICE_PIPED_GAS = 16
            const val SERVICE_INSURANCE = 17
            const val SERVICE_WATER = 18
            const val SERVICE_POSTPAID = 19
            const val SERVICE_ORDER_DEVICE = 20
            const val SERVICE_EKYC_AUTH = 21


        }
    }

    interface ApiResponse {
        companion object {
            const val RES_SUCCESS = "TXN"
            const val RES_SUCCESS_TPAY = "TPAY"
            const val RES_UNAUTHORISED = "UA"
            const val RES_ERROR: String = "ERR"
            const val RES_FAIL: String = "TXF"
            const val RES_PENDING: String = "TUP"
            const val RES_TXNOTP: String = "TXNOTP"
            const val RES_CNF: String = "CNF"
        }
    }


    interface DIRECTORY {
        companion object {

            // Internal Storage Folder Name
            val ROOT_DIR = "EasyPaisa"
            val PROFILE_DIR = "ProofDetail"
            val IMAGE_DIR = "Image"
        }
    }

    interface SharedPrefKey {
        companion object {
            const val LOGGEDIN_USER = "user_info"
            const val USER_LOCATION = "user_location"
            const val USER_REQUIRED_DATA = "user_required_date"
            const val DEVICE_TOKEN = "device_token"
            const val USER_LATLNG = "USER_LATLNG"
            const val PREF_FILE = "PREF_FILE_EASY_PAISA"
            const val PREFF_NEED_UPDATE = "PREFF_NEED_UPDATE"
        }
    }

    interface APP_PERMISSION {
        companion object {
            const val PERMISSIONS_SMS_READ = 301
            const val PERMISSIONS_CURRENT_LOCATION = 302
            const val GPS_ENABLED = 303
            const val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
        }
    }

    interface UI {
        companion object {
            const val FOR_START_TIME = 0
            const val FINGERSCAN_CODE = 1037
            const val UPDATE_PASS = 1455
            const val TYPE_PHOTO_PICK_FROM_FILE = 123
            const val MY_PERMISSIONS_REQUEST_CAMERA = 1212
            const val MY_PERMISSIONS_REQUEST_STORAGE_READ = 1234
            const val TYPE_PHOTO_PICK_FROM_CAMERA = 1236
            const val FOR_END_TIME = 1
            const val FILE_SELECT_CODE = 1010
            const val CODE_ADD_BENE = 123
            const val RC_SIGN_IN = 1011
            const val LOCATION_FOR_PICKUP = 1012
            const val LOCATION_FOR_DROP_OFF = 1013
            const val EDIT_PROFILE = 1014
            const val PERMISSIONS_CURRENT_LOCATION = 5656
            const val SELECT_BANK = 1015
            const val SELECT_BANK_YES = 1017
            const val SELECT_AADHAR_BANK = 1016
            const val ADD_BANk = 10178
            const val NEW_BENE = "new_bene"
            const val MOBILE_NO = "mobile_no"
            const val MOBILE_RESPONSE = "mobile_response"
            const val ELECTRICITY_BILL_RESPONSE = "el_bill_response"
            const val GAS_RESPONSE = "gas_response"
            const val LPG_RESPONSE = "lpg_response"
            const val POSTPAID_RESPONSE = "postpaid_response"
            const val WATER_RESPONSE = "water_response"
            const val INSURANCE = "insurance"
            const val DTH_RESPONSE = "dth_response"
            const val SET_LOCATION = "SET_LOCATION"
            const val PLACE = "PLACE"
            const val PA_ID = "pa_id"
            const val CUSTOMER_ID = "customer_id"
            const val PAYMENTHISTORY = "PaymentHistory"
            const val EDIT_CUSTOMER_TASK = "EDIT_CUSTOMER_TASK"
            const val MOBILE_NUMBER = "MOBILE_NUMBER"
            const val MINI_STATEMENT_ICICI = "MINI_STATEMENT_ICICI"
            const val AEPSREQUEST = "aepsRequest"
            const val AEPS_RESPONSE = "aeps_response"
            const val AEPS_MINI_RESPONSE = "aeps_mini_response"
            const val AEPS_REQUEST = "aeps_request"
            const val REMITTER_RESPONSE = "remitter_response"
            const val USER_DATA = "user_data"
            const val DMT_TXN_RESPONSE = "dmt_txn_response"
            const val SELECT_BENE = "select_bene"
            const val SERVICE_FOR = "service_for"
        }
    }

}