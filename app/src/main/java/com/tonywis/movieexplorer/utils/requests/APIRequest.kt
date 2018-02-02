package com.tonywis.movieexplorer.utils.requests

/**
 * Created by Tony on 02/02/2018.
 */

import android.content.Context
import android.util.Log
import android.widget.Toast

import com.android.volley.NetworkResponse
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.Response.Listener
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.tonywis.movieexplorer.BuildConfig
import com.tonywis.movieexplorer.R
import com.tonywis.movieexplorer.models.Answer

import java.io.UnsupportedEncodingException
import java.lang.reflect.Type
import java.nio.charset.Charset
import java.util.HashMap


class APIRequest<TypeData>(private val context: Context, private val resultClass: Type, private val taskComplete: TaskComplete<TypeData>?) {
    private val params: HashMap<String, String> = HashMap()
    private var method: Int = 0
    private var answer: Answer<TypeData>? = null

    init {
        method = -99999
        answer = Answer()
    }

    fun addParam(key: String, value: String) {
        params[key] = value
    }

    fun setMethod(meth: Int) {
        method = meth
    }

    fun execute(url: String) {
        if (method == -99999) {
            Log.e("Schedule Rep APIRequest", "Method request missing ! (url: $url)")
            return
        }
        if (!Network.isConnected(context)) {
            Toast.makeText(context, R.string.not_connect, Toast.LENGTH_SHORT).show()
            return
        }
        if (BuildConfig.DEBUG)
            showLogRequest(url)
        val queue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(method, url, Listener { response ->
            val gson = Gson()
            if (BuildConfig.DEBUG)
                showLogResponse(url, response)
            answer = gson.fromJson<Answer<TypeData>>(response, resultClass)
            if (taskComplete != null) {
                taskComplete.result = answer
                taskComplete.run()
            }
        }, Response.ErrorListener { error ->
            val response = error.networkResponse
            val res: String
            if (response != null) {
                if (response.data != null) {
                    try {
                        HttpHeaderParser.parseCharset(response.headers, Charsets.UTF_8.displayName())
                        res = String(response.data, Charset.forName(HttpHeaderParser.parseCharset(response.headers, Charsets.UTF_8.displayName())))
                        Log.e("Error", res)
                    } catch (e: UnsupportedEncodingException) {
                        e.printStackTrace()
                    }

                }
            } else {
                Log.d(TAG, "Le serveur ne répond pas !")
            }
            if (taskComplete != null) {
                taskComplete.result = null
                taskComplete.run()
            }
        }) {
            override fun getParams(): Map<String, String> {
                return this@APIRequest.params
            }

            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/x-www-form-urlencoded; charset=utf-8"
                return headers
            }
        }
        queue.add(stringRequest)
    }

    private fun showLogRequest(url: String) {
        val log = StringBuilder(url + " params: {")
        var i = 0
        for ((key, value) in params) {
            if (i > 0)
                log.append(", ")
            log.append(key).append(": ").append(value)
            i++
        }
        log.append("}")
        Log.d(TAG + " REQUEST (" + METHOD_LOG[method] + ")", log.toString())
    }

    private fun showLogResponse(url: String, response: String) {
        Log.d(TAG + " RESPONSE (" + METHOD_LOG[method] + ")", "(url: $url) -> $response")
    }

    companion object {

        private val TAG = "APIRequest"
        private val METHOD_LOG = arrayOf("GET", "POST", "PUT", "DELETE")
    }
}
