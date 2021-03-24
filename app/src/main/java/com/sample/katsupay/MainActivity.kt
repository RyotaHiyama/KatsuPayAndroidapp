package com.sample.katsupay

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.sample.katsupay.communication.CommServer
import com.sample.katsupay.data.data.UserInfo
import com.sample.katsupay.signin_up.SignIn
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        balance.text = getBalanceFromServer() + " 円"

        purchaseHistoryButton.setOnClickListener {
            val intent = Intent(this, PurchaseHistory::class.java)
            startActivity(intent)
        }

        RIButton.setOnClickListener {
            val intent = Intent(this, RegistrationInformation::class.java)
            startActivity(intent)
        }

        SSbutton.setOnClickListener {
            val ss = Intent(this,StoreStock::class.java)
            startActivity(ss)
        }

        update.setOnClickListener {
            balance.text = getBalanceFromServer() + " 円"
            Toast.makeText(this, "更新完了", Toast.LENGTH_LONG).show()
        }

        logout.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage("ログアウトしてもよろしいですか？")
                .setPositiveButton("YES") { _, _ ->
                    UserInfo.delete()
                    val intent = Intent(this, SignIn::class.java)
                    Toast.makeText(this, "ログアウトしました", Toast.LENGTH_LONG).show()
                    startActivity(intent)
                }
                .setNegativeButton("CANCEL") { _, _ -> }
                .show()
        }
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setMessage("ログアウトしてもよろしいですか？")
            .setPositiveButton("YES") { _, _ ->
                UserInfo.delete()
                val intent = Intent(this, SignIn::class.java)
                Toast.makeText(this, "ログアウトしました", Toast.LENGTH_LONG).show()
                startActivity(intent)
            }
            .setNegativeButton("CANCEL") { _, _ -> }
            .show()
    }

    private fun getBalanceFromServer() : String? {
        val commServer = CommServer()
        commServer.setUrl(CommServer.GET_CUSTOMER_INFO)
        commServer.execute(CommServer.UB)
        while(commServer.RESPONSE_CODE == -1) { /* wait for response */ }
        val balance = commServer.get()
        return if(balance.isEmpty()) "0" else balance
    }
}