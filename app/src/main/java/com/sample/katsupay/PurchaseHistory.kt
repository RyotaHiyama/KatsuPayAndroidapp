package com.sample.katsupay

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ContentLoadingProgressBar
import com.sample.katsupay.communication.CommServer
import com.sample.katsupay.datas.JsonParser
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
            AlertDialog.Builder(this)
                .setTitle("●通信失敗")
                .setMessage("購入情報が取得できませんでした")
                .setPositiveButton("OK") { _, _ -> }
                .show()
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
                purchase.text = "${purchase.text}${it.toString()}\n"
                counter++
            }

//            purchase.text = getPurchase()

            PurchaseReturnButton.setOnClickListener {
                finish()
            }
        }
    }

    private fun getPurchase() : String {

        val commServer = CommServer()
        commServer.setUrl(CommServer.GET_PURCHASE)
        commServer.execute(CommServer.UB)

        while(commServer.RESPONSE_CODE == -1) { /* wait for response */ }

        if(commServer.RESPONSE_CODE == HttpURLConnection.HTTP_OK) {
            Log.i("purchase>>>", commServer.get())
            return commServer.get()
        } else {
            AlertDialog.Builder(this)
                .setTitle("●通信失敗")
                .setMessage("購入情報が取得できませんでした：${commServer.RESPONSE_CODE}")
                .setPositiveButton("OK") { _, _ -> }
                .show()
        }
        return ""
    }
}