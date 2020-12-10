package com.sample.katsupay

import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.sign_in.*
import java.net.HttpURLConnection
import java.util.regex.Matcher
import java.util.regex.Pattern

class SignInLayout : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* テスト用のアカウント登録 */
//        UserInfo.customer_id = "1000001"
//        UserInfo.customer_id = "1"
//        UserInfo.initPassword("*")
//        UserInfo.email = "customer1@yahoo.co.jp"
//        UserInfo.birthday = "2019-01-01"
    }



    private fun checkCorrectEntered(userName:String, password:String) : Boolean {
        Log.e(">>>","USERNAME:$userName, PASSWORD:$password")

        /* 文字列が入力されていない */
        if(userName.isEmpty() || password.isEmpty()) {
            AlertDialog.Builder(this) // FragmentではActivityを取得して生成
                .setTitle("●サインイン失敗")
                .setMessage("ユーザ名もしくはパスワードが入力されていません")
                .setPositiveButton("OK") { _, _ ->
                    // TODO:Yesが押された時の挙動
                }
                .show()
            return false
        }

        /* 半角英数字以外の文字が含まれている */
        val regexAlphaNum = "^[A-Za-z0-9]+$" //*以外の文字列でもパスワードを設定可能
        var p:Pattern = Pattern.compile(regexAlphaNum)
        var mUserName:Matcher = p.matcher(userName)
        var mPassword:Matcher = p.matcher(password)
        if(!mUserName.matches() || !mPassword.matches()){
            AlertDialog.Builder(this) // FragmentではActivityを取得して生成
                .setTitle("●サインイン失敗")
                .setMessage("半角英数字以外の文字が含まれています")
                .setPositiveButton("OK") { _, _ ->
                    // TODO:Yesが押された時の挙動
                }
                .show()
            return false
        }
        return true
    }

    override fun onResume(){
        super.onResume()

        setContentView(R.layout.sign_in)
        signIn.setOnClickListener {
            val password = passwordEdit.text.toString()
            val userName = userNameEdit.text.toString()
            if(checkCorrectEntered(userName, password)) {
                UserInfo.customer_id = userName
                UserInfo.initPassword(password)
                signIn()
            }
        }

        SignUpButton.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        if(!checkCommStatus()) {
            val intent = Intent(this, Reconnect::class.java)
            startActivity(intent)
        }
    }

    private fun signIn(){

        var commServer = CommServer(this)
        commServer.setUrl(CommServer.LOGIN)
        commServer.execute(CommServer.UB)

        while(commServer.RESPONSE_CODE == -1) {
            /* wait for response */
        }

        if(commServer.RESPONSE_CODE == HttpURLConnection.HTTP_OK) {
            Log.i(">>>>", commServer.get())
            Toast.makeText(this, "${UserInfo.customer_id}さん ようこそ！", Toast.LENGTH_LONG).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            AlertDialog.Builder(this) // FragmentではActivityを取得して生成
                .setTitle("●サインイン失敗")
                .setMessage("ユーザ名もしくはパスワードが間違っています：${commServer.RESPONSE_CODE}")
                .setPositiveButton("OK") { _, _ ->
                    // TODO:Yesが押された時の挙動
                }
                .show()
        }
    }

    private fun checkCommStatus() : Boolean {
        val cm = getSystemService(ConnectivityManager::class.java)
        if (cm.allNetworks.isNotEmpty()) {
            return true
        }
        return false
    }
}