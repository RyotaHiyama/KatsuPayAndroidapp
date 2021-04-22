package com.sample.katsupay.communication

import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.sample.katsupay.data.StoreInfo
import com.sample.katsupay.data.UserInfo
import com.sample.katsupay.communication.data.Customer
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class CommServer : AsyncTask<Uri.Builder, Void, String>() {
    companion object {
        const val TIMEOUT_MILLIS = 5000// タイムアウトまで5秒

        const val CUSTOMER = "customer"
        const val ACCOUNT = "account"
        const val TRANSACTION = "transaction"
        const val STORE = "store"
        const val PRODUCT = "product"

        const val GET = "GET"
        const val POST = "POST"

        const val LOGIN = 0
        const val GET_CUSTOMER_INFO = 1
        const val GET_PURCHASE = 2
        const val SIGN_UP = 3
        const val GET_ACCOUNT_INFO = 4
        const val GET_STOCK_INFO = 5
        const val GET_STORE_INFO = 6

        val UB:Uri.Builder = Uri.Builder()
    }
//    private val ipAddress = "165.242.108.54"
//    private val port = "8090"
    private val ipAddress = "10.0.2.2" //androidエミュレータからホストに対するアドレス
    private val port = "8080" //androidエミュレータとホスト間で通信する時のデフォルトポート
    private var request = ""
    private var url = ""
    private var postData = ""
    var responseCode = -1

    private fun setRequest(req:String) {
        if(req == GET || req == POST) {
            this.request = req
        } else {
            Log.e("ERROR", "存在しないリクエストモードです．")
        }
    }

    override fun doInBackground(vararg builder: Uri.Builder): String {
        return try {
            get("UTF-8")
        } catch(e:Exception) {
            responseCode = -2 // timeout
            ""
        }
    }

    fun setUrl(mode:Int) {
        when(mode) {
            LOGIN -> {
                setRequest(POST)
                url = "http://$ipAddress:$port/$CUSTOMER/login"
                postData = jacksonObjectMapper().writeValueAsString(Customer.getUserInfoAsCustomer())
            }
            GET_CUSTOMER_INFO -> {
                setRequest(GET)
                url = "http://$ipAddress:$port/$ACCOUNT/balance/${UserInfo.customer_id}"
            }
            GET_ACCOUNT_INFO -> {
                setRequest(GET)
                url = "http://$ipAddress:$port/$CUSTOMER/${UserInfo.customer_id}"
            }
            GET_PURCHASE -> {
                setRequest(GET)
                url = "http://$ipAddress:$port/$TRANSACTION/$CUSTOMER/${UserInfo.customer_id}"
            }
            SIGN_UP -> {
                setRequest(POST)
                url = "http://$ipAddress:$port/$CUSTOMER/signup/${UserInfo.customer_id}"
                postData = jacksonObjectMapper().writeValueAsString(Customer.getUserInfoAsCustomer())
            }
            GET_STOCK_INFO -> {
                setRequest(GET)
                url = "http://$ipAddress:$port/$PRODUCT/${StoreInfo.storeId}"
            }
            GET_STORE_INFO -> {
                setRequest(GET)
                url = "http://$ipAddress:$port/$STORE/all"
            }
        }
    }

    private fun get(encoding: String): String {
        val sb = StringBuffer("")
        val esb = StringBuffer("")
        var br: BufferedReader? = null
        var ism: InputStream? = null
        var isr: InputStreamReader? = null

        val url = URL(url)
        Log.i("checkAccessURL","Access to URL: $url")
        val huc = url.openConnection() as HttpURLConnection
        huc.run {
            this.connectTimeout = TIMEOUT_MILLIS// 接続にかかる時間
            this.readTimeout = TIMEOUT_MILLIS// データの読み込みにかかる時間
            this.requestMethod = request // HTTPメソッド
            this.useCaches = false// キャッシュ利用
            this.doOutput = (request == POST) // リクエストのボディの送信を許可(GETのときはfalse,POSTのときはtrueにする)
            this.doInput = true// レスポンスのボディの受信を許可
//            this.setRequestProperty("Accept-Language", "jp")
            this.setRequestProperty("Content-Type", "application/json; charset=utf-8")

            this.connect()

            if(request == POST) {
                val outputStream = this.outputStream
                outputStream.write(postData.toByteArray())
                outputStream.flush()
                Log.i("SEND DATA TO SERVER","DATA: $postData")
            }

            val responseCode = this.responseCode
            this@CommServer.responseCode = responseCode
            Log.i("GET RESPONSE","CODE: ${this@CommServer.responseCode}")

            if (responseCode == HttpURLConnection.HTTP_OK) {
                ism = this.inputStream
                isr = InputStreamReader(ism, encoding)
                br = BufferedReader(isr)
                var line: String? = ""
                while (line != null) {
                    sb.append(line)
                    line = br!!.readLine()
                }
            } else {
                ism = this.errorStream
                isr = InputStreamReader(ism, encoding)
                br = BufferedReader(isr)
                var line: String? = ""
                while (line != null) {
                    esb.append(line)
                    line = br!!.readLine()
                }
            }
        }
        return sb.toString()
    }

}