package com.sample.katsupay.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.sample.katsupay.R
import com.sample.katsupay.communication.CommServer
import com.sample.katsupay.data.JsonParser
import kotlinx.android.synthetic.main.purchase_history.*
import java.net.HttpURLConnection

class PurchaseHistory : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.purchase_history)

        purchase.isSingleLine = false // 複数行表示できるように変更

        val str = getPurchase()
        val transactions = JsonParser.transactionParse(str)

        if(transactions == null) {
            purchaseAlert()
            finish()
        } else {
            if(transactions.isEmpty()) {
                AlertDialog.Builder(this)
                    .setMessage("まだ購入履歴がありません")
                    .setPositiveButton("OK") { _, _ -> finish() }
                    .show()
            }

            purchase.text = ""
            var counter = 1
            transactions.forEach {
                purchase.text = "${purchase.text}===== 取引情報[$counter] =====\n"
                purchase.text = "${purchase.text}$it\n"
                counter++
            }
        }

        PurchaseReturnButton.setOnClickListener {
            finish()
        }
    }

    private fun getPurchase() : String {

        val commServer = CommServer()
        commServer.setUrl(CommServer.GET_PURCHASE)
        commServer.execute(CommServer.UB)

        while(commServer.responseCode == -1) { /* wait for response */ }

        return if(commServer.responseCode == HttpURLConnection.HTTP_OK) {
            Log.i("purchase>>>", commServer.get())
            commServer.get()
        } else {
            purchaseAlert()
            ""
        }
    }

    private fun purchaseAlert(){
        val commServer = CommServer()
        AlertDialog.Builder(this)
            .setTitle("●通信失敗")
            .setMessage("購入情報が取得できませんでした：${commServer.responseCode}")
            .setPositiveButton("OK") { _, _ -> }
            .show()
    }
}