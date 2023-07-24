package com.example.waterreminder.helper

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

interface SharedPreferenceHelper{
    fun setString(key: String, value: String)
    fun getString(key: String): String?

    fun setInt(key: String, value: Int)
    fun getInt(key: String): Int?

    fun setBoolean(key: String, value: Boolean)
    fun getBoolean(key: String): Boolean?
}

@Singleton
class PreferenceHelper @Inject constructor(
    @ApplicationContext context: Context
) : SharedPreferenceHelper {
    companion object {
        const val APP_PREFS = "app_prefs"
        const val LAST_RATE_PREFS = "last_rate_prefs"
        const val PREF_CURRENT_LANGUAGE = "pref_current_language"
        const val PREF_SHOWED_START_LANGUAGE = "pref_showed_start_language"
        const val PREF_FIRST_APP_OPENED = "pref_first_app_opened"
        const val PREF_LAST_BASE = "pref_last_base"  // Last base selected
        const val PREF_BASE_RATE = "pref_base_rate"  // Base of last rate

        const val PREF_SELECTED_GENDER = "pref_selected_gender"
        const val PREF_WEIGHT = "pref_weight"
        const val PREF_UNIT_WEIGHT = "pref_unit_weight"
        const val PREF_WAKE_UP_TIME_HOUR = "pref_wake_up_time_hour"
        const val PREF_WAKE_UP_TIME_MINUTE = "pref_wake_up_time_minute"
        const val PREF_SLEEP_TIME_HOUR = "pref_sleep_time_hour"
        const val PREF_SLEEP_TIME_MINUTE = "pref_sleep_time_minute"
        const val PREF_QUANTITY_WATER = "pref_quantity_water"
        const val PREF_DAILY_WATER = "pref_daily_water"
        const val PREF_CURRENT_PERCENT = "pref_current_percent"
        const val SELECTED_CUP_KEY = "selected_cup"
        const val SELECTED_IMAGE_RES_ID_KEY = "selected_image_id"
        const val PREF_NUMBER_DRINK_WATER = "pref_number_drink_water"
        const val PREF_CAPACITY = "pref_capacity"
        const val PREF_IS_CHECK = "pref_is_check"
        const val PREF_SOUND = "pref_sound"


    }

    private var masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val sharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            APP_PREFS,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override fun setString(key: String, value: String) {
        sharedPreferences
            .edit()
            .putString(key, value)
            .apply()
    }

    override fun getString(key: String): String? {
        return sharedPreferences.getString(key, "")
    }

    override fun setInt(key: String, value: Int) {
        sharedPreferences
            .edit()
            .putInt(key, value)
            .apply()
    }

    override fun getInt(key: String): Int {
        return sharedPreferences.getInt(key, 0)
    }

    override fun setBoolean(key: String, value: Boolean) {
        sharedPreferences
            .edit()
            .putBoolean(key, value)
            .apply()
    }

    override fun getBoolean(key: String): Boolean? {
        return sharedPreferences.getBoolean(key, false)
    }

    /*** Last Rate SharedPreference : Get from Api when network is connected ***/
    private val lastRateSharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            LAST_RATE_PREFS,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun setLastRatesString(key: String, value: String){
        lastRateSharedPreferences
            .edit()
            .putString(key, value)
            .apply()
    }

}
