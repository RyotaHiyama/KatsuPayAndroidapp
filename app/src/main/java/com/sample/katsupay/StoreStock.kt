package com.sample.katsupay

import android.graphics.Color
import android.graphics.Color.RED
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.sample.katsupay.communication.CommServer
import com.sample.katsupay.data.JsonParser
import com.sample.katsupay.data.transData.Product
import kotlinx.android.synthetic.main.purchase_history.*
import kotlinx.android.synthetic.main.store_stock.*
import java.net.HttpURLConnection

class StoreStock : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.store_stock)

        stockinfo.isSingleLine = false

        val str = getStock()
        val products = JsonParser.stockParse(str)

        if(products == null) {
            AlertDialog.Builder(this)
                .setTitle("●通信失敗")
                .setMessage("在庫情報が取得できませんでした")
                .setPositiveButton("OK") { _, _ -> }
                .show()
            finish()
        } else {
            if(products.isEmpty()) {
                AlertDialog.Builder(this)
                    .setMessage("在庫情報がありません")
                    .setPositiveButton("OK") { _, _ -> finish() }
                    .show()
            }

            stockinfo.text = ""
            stockinfo.typeface = Typeface.DEFAULT_BOLD
            var text = ""
            products.forEach{
                text = "${text}商品名：${it.name}<br>"
                text = "${text}値段：${it.price}<br>"
                text = "${text}在庫：" + judgeStock(it.stock) + "<br><br>"
            }
            stockinfo.text = Html.fromHtml(text)
        }


            StoreStockReturnButton.setOnClickListener{
            finish()
        }
        //stockinfo.textSize = 200F  //scroll test
    }
    private fun getStock() : String {
        val commServer = CommServer()
        commServer.setUrl(CommServer.GET_STOCK_INFO)
        commServer.execute(CommServer.UB)

        while(commServer.RESPONSE_CODE == -1) { /* wait for response */ }

        if(commServer.RESPONSE_CODE == HttpURLConnection.HTTP_OK){
            Log.i("products>>>", commServer.get())
            return commServer.get()
        } else {
            AlertDialog.Builder(this)
                .setTitle("●通信失敗")
                .setMessage("在庫情報が取得できませんでした：${commServer.RESPONSE_CODE}")
                .setPositiveButton("OK") { _, _ -> }
                .show()
        }
        return ""
    }

    private fun judgeStock(Stock:Int) : String {
        return when {
            Stock <= 0 -> {
                "<big><font color=\"red\">×</font></big>"
            }
            Stock <= 3 -> "<big><font color=#ffa500>△</font></big>"
            else       -> "〇"
        }
    }
}