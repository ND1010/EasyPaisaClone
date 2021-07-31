package com.app.bhaskar.easypaisa.application

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings
import androidx.multidex.MultiDex
import androidx.room.Room
import com.app.bhaskar.easypaisa.AppDatabase
import com.app.bhaskar.easypaisa.BuildConfig
import com.app.bhaskar.easypaisa.di.components.AppComponent
import com.app.bhaskar.easypaisa.di.components.DaggerAppComponent
import com.app.bhaskar.easypaisa.response_model.LoginResponse
import com.app.bhaskar.easypaisa.response_model.UserLocationLatLng
import com.app.bhaskar.easypaisa.response_model.UserRequiredDataResponse
import com.app.bhaskar.easypaisa.utils.Constants
import com.easypay.epmoney.epmoneylib.application.PaisaNikalApp
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pa.di.modules.DB_FILE_NAME
import com.pa.di.modules.DatabaseModule
import com.pa.di.modules.PostRepoModule
import com.pa.di.modules.RemoteDataModule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.greenrobot.eventbus.EventBus
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class EasyPaisaApp : PaisaNikalApp() {

    companion object {
        lateinit var component: AppComponent
        private var mGson: Gson? = null
        private var mGsonSimple: Gson? = null
        private var mDeviceId: String? = null
        private var mInstance: EasyPaisaApp? = null
        //Instance of OkHttpclient
        private var client: OkHttpClient? = null
        private var eventBus: EventBus? = null

        /**
         * Method for Gson With Expose
         *
         * @return
         */
        fun getGsonWithExpose(): Gson {
            if (mGson != null) {
                mGson = null
            }
            mGson = GsonBuilder().setLenient().create()
            return mGson!!
        }

        fun getGson(): Gson {
            if (mGsonSimple != null) {
                mGsonSimple = null

            }
            mGsonSimple = Gson()
            return mGsonSimple!!
        }

        fun getDefault(): EventBus {
            if (eventBus == null) {
                synchronized(EventBus::class.java) {
                    if (eventBus == null) {
                        eventBus = EventBus()
                    }
                }
            }
            return eventBus!!
        }

        fun getDB():AppDatabase{
            return Room.databaseBuilder(getAppInstance(), AppDatabase::class.java, DB_FILE_NAME).build()
        }

        @SuppressLint("HardwareIds")
        fun getDeviceId(): String {
            if (mDeviceId == null) {
                mDeviceId = Settings.Secure.getString(
                    getAppInstance().contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            }
            return mDeviceId!!
        }

        fun getClient(): OkHttpClient {

            if (client == null) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                client = OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .addInterceptor(interceptor)
                    //.addInterceptor(HeaderInterceptor())
                    .build()
            }
            return client!!
        }


        /**
         * Method for save Logged in user location data in to Preferences
         *
         * @return
         */
        fun setUserLatLng(userLatlng: UserLocationLatLng) {
            val sharedPrefEditor = getSharedPreferenceEditor(Constants.SharedPrefKey.PREF_FILE)
            sharedPrefEditor.putString(
                Constants.SharedPrefKey.USER_LOCATION,
                getGson().toJson(userLatlng)
            ).apply()
        }

        fun getUserLatLng(): UserLocationLatLng? {
            val sharedPref = getSharedPreference(Constants.SharedPrefKey.PREF_FILE)
            val user = sharedPref.getString(Constants.SharedPrefKey.USER_LOCATION, "")
            return getGson().fromJson<UserLocationLatLng>(
                user,
                UserLocationLatLng::class.java
            )
        }

        fun removeUserLatLong() {
            getSharedPreferenceEditor(Constants.SharedPrefKey.PREF_FILE).remove(Constants.SharedPrefKey.USER_LOCATION).apply()
        }

        /**
         * Method for get current instance of Application
         *
         * @return
         */
        fun getAppInstance(): EasyPaisaApp {
            return mInstance!!
        }

        /**
         * Method for get data in Preference
         *
         * @param name
         * @return
         */
        fun getSharedPreference(name: String): SharedPreferences {
            return getAppInstance().getSharedPreferences(name, Context.MODE_PRIVATE)
        }

        /**
         * Method for get SharedPreferences for edit
         *
         * @param name
         * @return
         */
        fun getSharedPreferenceEditor(name: String): SharedPreferences.Editor {
            val preferences = getAppInstance().getSharedPreferences(name, Context.MODE_PRIVATE)
            return preferences.edit()
        }

        /**
         * Method for save Login User data in to Preferences
         *
         * @return
         */
        fun setLoggedInUser(user: LoginResponse.Userdata) {
            val sharedPrefEditor = getSharedPreferenceEditor(Constants.SharedPrefKey.PREF_FILE)
            sharedPrefEditor.putString(
                Constants.SharedPrefKey.LOGGEDIN_USER,
                getGsonWithExpose().toJson(user)
            ).apply()
        }



        /**
         * Method for save Login User data in to Preferences
         *
         * @return
         */
        fun setClearSession() {
            getSharedPreferenceEditor(Constants.SharedPrefKey.PREF_FILE).clear().apply()
        }

        /**
         * Method for get Login User data from Preferences
         *
         * @return
         */
        @SuppressLint("NewApi")
        @JvmStatic
        fun getLoggedInUser(): LoginResponse.Userdata? {
            val sharedPref = getSharedPreference(Constants.SharedPrefKey.PREF_FILE)
            val user = sharedPref.getString(Constants.SharedPrefKey.LOGGEDIN_USER, "")
            return getGsonWithExpose().fromJson<LoginResponse.Userdata>(
                user,
                LoginResponse.Userdata::class.java
            )
        }

        fun setUserRequiredData(user: UserRequiredDataResponse) {
            val sharedPrefEditor = getSharedPreferenceEditor(Constants.SharedPrefKey.PREF_FILE)
            sharedPrefEditor.putString(
                Constants.SharedPrefKey.USER_REQUIRED_DATA,
                getGsonWithExpose().toJson(user)
            ).apply()
        }

        fun setNeedUpdateUserDetail(needUpdate: Boolean) {
            val sharedPrefEditor = getSharedPreferenceEditor(Constants.SharedPrefKey.PREF_FILE)
            sharedPrefEditor.putBoolean(Constants.SharedPrefKey.PREFF_NEED_UPDATE,needUpdate).apply()
        }

        fun getNeedUpdateUserDetail():Boolean {
            val sharedPrefEditor = getSharedPreference(Constants.SharedPrefKey.PREF_FILE)
            return sharedPrefEditor.getBoolean(Constants.SharedPrefKey.PREFF_NEED_UPDATE,false)
        }


        @SuppressLint("NewApi")
        @JvmStatic
        fun getUserRequiredData(): UserRequiredDataResponse? {
            val sharedPref = getSharedPreference(Constants.SharedPrefKey.PREF_FILE)
            val user = sharedPref.getString(Constants.SharedPrefKey.USER_REQUIRED_DATA, "")
            return getGsonWithExpose().fromJson<UserRequiredDataResponse>(
                user,
                UserRequiredDataResponse::class.java
            )
        }


        fun provideRetrofit(): Retrofit {
            val gson = GsonBuilder().create()
            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_KARZA_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getClient())
                .build()
        }

    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        init(this)
        setupGraph()
    }

    private fun setupGraph() {
        component =
            DaggerAppComponent.builder()
                .databaseModule(DatabaseModule(applicationContext))
                .remoteDataModule(RemoteDataModule(BuildConfig.BASE_URL))
                .postRepoModule(PostRepoModule())
                .build()
    }


    override fun attachBaseContext(context: Context?) {
        super.attachBaseContext(context)
        MultiDex.install(this)
    }

}