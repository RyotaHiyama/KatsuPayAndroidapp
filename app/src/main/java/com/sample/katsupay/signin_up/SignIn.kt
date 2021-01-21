package com.sample.katsupay.signin_up

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.sample.katsupay.*
import com.sample.katsupay.communication.CommServer
import com.sample.katsupay.datas.JsonParser
import com.sample.katsupay.datas.datas.UserInfo
import kotlinx.android.synthetic.main.sign_in.*
import java.net.HttpURLConnection
import java.util.regex.Matcher
import java.util.regex.Pattern


class SignIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in)
    }

    override fun onResume(){
        super.onResume()
        signIn.setOnClickListener {
            val password = passwordEdit.text.toString()
            val userName = userNameEdit.text.toString()
            if(checkCorrectEntered(userName, password)) {
                UserInfo.customer_id = userName
                UserInfo.initPassword(password)
                try {
                    signIn()
                } catch(e:Exception) {
                    AlertDialog.Builder(this)
                        .setTitle("●サインイン失敗")
                        .setMessage("サーバへアクセスできませんでした．ネットワーク環境を確認してください．")
                        .setPositiveButton("OK") { _, _ -> }
                        .show()
                }
            }
        }

        SignUpButton.setOnClickListener {
            val intent = Intent(this, ScanQRCode::class.java)
            startActivity(intent)
        }

        contact.setOnClickListener {
            val cu = Intent(this, ContactUs::class.java)
            startActivity(cu)
        }

        if(!checkCommStatus()) {
            val intent = Intent(this, Reconnect::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        /* do nothing */
    }

    private fun signIn() {
        val commServer = CommServer()
        commServer.setUrl(CommServer.LOGIN)
        commServer.execute(CommServer.UB)

        while(commServer.RESPONSE_CODE == -1) { /* wait for response */ }

        if(commServer.RESPONSE_CODE == HttpURLConnection.HTTP_OK) {
            val response = commServer.get() ?: return
//            if(response == null) {
//                AlertDialog.Builder(this)
//                    .setTitle("●サインイン失敗")
//                    .setMessage("ユーザ名もしくはパスワードが間違っています：${commServer.RESPONSE_CODE}")
//                    .setPositiveButton("OK") { _, _ -> }
//                    .show()
//                return
//            }

            Log.i("RETURN VALUE FROM SERVER", " VALUE: $response")
            if(response != "null") {
                val customer = JsonParser.customerParse(response)
                if(customer == null) {
                    Toast.makeText(this, "ログインの際にサーバから予期せぬメッセージを受信しました", Toast.LENGTH_LONG).show()
                } else {
                    UserInfo.initialize(customer)
                    Toast.makeText(this, "${UserInfo.customer_id}さん ようこそ！", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            } else {
                AlertDialog.Builder(this)
                    .setTitle("●サインイン失敗")
                    .setMessage("ユーザ名もしくはパスワードが間違っています：${commServer.RESPONSE_CODE}")
                    .setPositiveButton("OK") { _, _ -> }
                    .show()
            }
        } else {
            AlertDialog.Builder(this)
                .setTitle("●サインイン失敗")
                .setMessage("ユーザ名もしくはパスワードが間違っています：${commServer.RESPONSE_CODE}")
                .setPositiveButton("OK") { _, _ -> }
                .show()
        }
    }

    private fun checkCommStatus() : Boolean {
        val cm = getSystemService(ConnectivityManager::class.java) ?: return false
        if (cm.allNetworks.isNotEmpty()) {
            return true
        }
        return false
    }

    private fun checkCorrectEntered(userName:String, password:String) : Boolean {
        Log.e(">>>","USERNAME:$userName, PASSWORD:$password")

        /* 文字列が入力されていない */
        if(userName.isEmpty() || password.isEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("●サインイン失敗")
                .setMessage("ユーザ名もしくはパスワードが入力されていません")
                .setPositiveButton("OK") { _, _ -> }
                .show()
            return false
        }

        /* 半角英数字以外の文字が含まれている */
        val regexAlphaNum = "^[A-Za-z0-9]+$" //*以外の文字列でもパスワードを設定可能
        val p:Pattern = Pattern.compile(regexAlphaNum)
        val mUserName:Matcher = p.matcher(userName)
        val mPassword:Matcher = p.matcher(password)
        if(!mUserName.matches() || !mPassword.matches()){
            AlertDialog.Builder(this)
                .setTitle("●サインイン失敗")
                .setMessage("半角英数字以外の文字が含まれています")
                .setPositiveButton("OK") { _, _ -> }
                .show()
            return false
        }
        return true
    }
}