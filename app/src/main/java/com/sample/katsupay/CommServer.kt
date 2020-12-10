package com.sample.katsupay

import android.app.Activity
import android.net.ConnectivityManager
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class CommServer  : AsyncTask<Uri.Builder, Void, String> {
    companion object {
        const val CUSTOMER = "customer"
        const val ACCOUNT = "account"
        const val TRANSACTION = "transaction"
        const val GET = "GET"
        const val POST = "POST"
        const val LOGIN = 0
        const val GET_CUSTOMER_INFO = 1
        const val GET_PURCHASE = 2
        val UB:Uri.Builder = Uri.Builder()
    }
    private val ipAddress = "165.242.108.54"
    private val port = "8090"
//    private val ipAddress = "10.0.2.2" //androidエミュレータからホストに対するアドレス
//    private val port = "8080" //androidエミュレータとホスト間で通信する時のデフォルトポート
    private var COMM_MODE = ""
    private var REQUEST = ""
    private var URL = ""
    var RESPONSE_CODE = -1

    fun setCommMode(mode:String) {
        if(mode.equals(ACCOUNT)) COMM_MODE = ACCOUNT
        else if(mode.equals(CUSTOMER)) COMM_MODE = CUSTOMER
        else if(mode.equals(TRANSACTION)) COMM_MODE = TRANSACTION
        else {
            Log.e("ERROR", "存在しない通信モードです．")
        }
    }

    fun setRequest(req:String) {
        if(req.equals(GET) || req.equals(POST)) {
            this.REQUEST = req
        } else {
            Log.e("ERROR", "存在しないリクエストモードです．")
        }
    }

    private lateinit var mainActivity: Activity
    constructor(activity: Activity) {
        this.mainActivity = activity
    }

    override fun doInBackground(vararg builder: Uri.Builder): String {
        if(COMM_MODE.equals(ACCOUNT) || COMM_MODE.equals(CUSTOMER) || COMM_MODE.equals(TRANSACTION)){
            return get("127.0.0.1", "UTF-8")
        } else return ""
    }

    fun setUrl(mode:Int) {
        when(mode) {
            LOGIN -> {
                setCommMode(CUSTOMER)
                setRequest(GET)
                URL = "http://$ipAddress:$port/$COMM_MODE/login/${UserInfo.customer_id}/${UserInfo.getPassword()}"
            }
            GET_CUSTOMER_INFO -> {
                setCommMode(ACCOUNT)
                setRequest(GET)
                URL = "http://$ipAddress:$port/$COMM_MODE/${UserInfo.customer_id}"
            }
            GET_PURCHASE -> {
                setCommMode(TRANSACTION)
                setRequest(GET)
                URL = "http://$ipAddress:$port/$COMM_MODE/$CUSTOMER/${UserInfo.customer_id}"
            }
        }
    }

    operator fun get(endpoint: String, encoding: String): String {

        val TIMEOUT_MILLIS = 5000// タイムアウトミリ秒：0は無限

        val sb = StringBuffer("")

        var httpConn: HttpURLConnection? = null
        var br: BufferedReader? = null
        var ism: InputStream? = null
        var isr: InputStreamReader? = null

//            var url = URL("http://localhost:8080/customer/1000001")
        var url = URL(URL)
        Log.i("checkAccessURL","Access to URL: ${url.toString()}")
        var huc = url.openConnection() as HttpURLConnection
        huc.run {
            this.connectTimeout = TIMEOUT_MILLIS// 接続にかかる時間
            this.readTimeout = TIMEOUT_MILLIS// データの読み込みにかかる時間
            this.requestMethod = REQUEST // HTTPメソッド
            this.useCaches = false// キャッシュ利用
            this.doOutput = (REQUEST == POST) // リクエストのボディの送信を許可(GETのときはfalse,POSTのときはtrueにする)
            this.doInput = true// レスポンスのボディの受信を許可

            this.connect()

            val responseCode = this.responseCode
            RESPONSE_CODE = responseCode

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
                // If responseCode is not HTTP_OK
            }
        }
        return sb.toString()
    }

}