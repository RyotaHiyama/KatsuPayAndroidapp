package com.sample.katsupay

import android.app.Activity
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
        val CUSTOMER = "customer"
        val ACCOUNT = "account"
        val UB:Uri.Builder = Uri.Builder()
    }
    private val ipAddress = "165.242.108.54"
    private val port = "8090"
//    private val ipAddress = "10.0.2.2" //androidエミュレータからホストに対するアドレス
//    private val port = "8080" //androidエミュレータとホスト間で通信する時のデフォルトポート
    private var COMM_MODE = ""
    var RESPONSE_CODE = -1

    fun setCommMode(mode:String) {
        if(mode.equals(ACCOUNT)) COMM_MODE = mode
        else if(mode.equals(CUSTOMER)) COMM_MODE = mode
        else {
            Log.e("ERROR", "存在しない通信モードです．")
        }
    }

    private lateinit var mainActivity: Activity
    constructor(activity: Activity) {
        this.mainActivity = activity
    }

    override fun doInBackground(vararg builder: Uri.Builder): String {
        if(COMM_MODE.equals(ACCOUNT) || COMM_MODE.equals(CUSTOMER)){
            return get("127.0.0.1", "UTF-8")
        } else return ""
    }

    operator fun get(endpoint: String, encoding: String): String {

        val TIMEOUT_MILLIS = 5000// タイムアウトミリ秒：0は無限

        val sb = StringBuffer("")

        var httpConn: HttpURLConnection? = null
        var br: BufferedReader? = null
        var ism: InputStream? = null
        var isr: InputStreamReader? = null

//            var url = URL("http://localhost:8080/customer/1000001")
        var url = URL("http://$ipAddress:$port/$COMM_MODE/${UserInfo.customer_id}")
        Log.i("checkAccessURL","Access to URL: ${url.toString()}")
        var huc = url.openConnection() as HttpURLConnection
        huc.run {
            this.connectTimeout = TIMEOUT_MILLIS// 接続にかかる時間
            this.readTimeout = TIMEOUT_MILLIS// データの読み込みにかかる時間
            this.requestMethod = "GET"// HTTPメソッド
            this.useCaches = false// キャッシュ利用
            this.doOutput = false// リクエストのボディの送信を許可(GETのときはfalse,POSTのときはtrueにする)
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