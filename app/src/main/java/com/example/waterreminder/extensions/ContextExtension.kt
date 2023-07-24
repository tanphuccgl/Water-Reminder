package com.example.waterreminder.extensions

import android.content.Context
import android.net.ConnectivityManager
import java.text.SimpleDateFormat
import java.util.Locale

fun Context.hasNetworkConnection(): Boolean {
    var haveConnectedWifi = false
    var haveConnectedMobile = false
    val cm =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo = cm.allNetworkInfo
    for (ni in netInfo) {
        if (ni.typeName.equals("WIFI", ignoreCase = true))
            if (ni.isConnected) haveConnectedWifi = true
        if (ni.typeName.equals("MOBILE", ignoreCase = true))
            if (ni.isConnected) haveConnectedMobile = true
    }
    return haveConnectedWifi || haveConnectedMobile
}

fun Context.setLocale(language: String?) {
    val configuration = resources.configuration
    val locale = if (language.isNullOrEmpty()) {
        Locale.getDefault()
    } else {
        Locale(language)
    }
    configuration.setLocale(locale)
    configuration.setLayoutDirection(locale)
    createConfigurationContext(configuration)
}

fun String.toTime(format: String): Long {
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())

    return dateFormat.parse(this)?.time ?: 0
}