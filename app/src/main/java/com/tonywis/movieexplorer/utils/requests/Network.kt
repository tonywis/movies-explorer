package com.tonywis.movieexplorer.utils.requests

/**
 * Created by Tony on 02/02/2018.
 */

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo


object Network {
    fun isConnected(c: Context): Boolean {
        val cm = c.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo: NetworkInfo?
        netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }
}
