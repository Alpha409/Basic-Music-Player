package com.example.musicplayer.common.extensionFunctions

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri

object NetworkExtensionF {
    fun Context.isNetworkAvailable(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as
                ConnectivityManager
        return cm.activeNetworkInfo?.isConnectedOrConnecting == true
    }

    fun Context.openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

}