package com.sample.katsupay.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.sample.katsupay.R
import com.sample.katsupay.communication.CommServer
import com.sample.katsupay.data.JsonParser
import com.sample.katsupay.data.StoreInfo
import kotlinx.android.synthetic.main.all_stores.*
import java.net.HttpURLConnection

class AllStores : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.all_stores)

        val str = getStore()
        val stores = JsonParser.storesParse(str)

        if(stores == null) {
            AlertDialog.Builder(this)
                .setTitle("●通信失敗")
                .setMessage("店舗情報が取得できませんでした")
                .setPositiveButton("OK") { _, _ -> }
                .show()
            finish()
        } else {
            val ll = LinearLayout(this)
            ll.gravity = Gravity.CENTER_VERTICAL
            ll.orientation = LinearLayout.VERTICAL
            for (s in stores){
                val bt = Button(this)
                bt.id = s.storeId.toInt()
                bt.backgroundTintList = ColorStateList.valueOf(Color.rgb(228,182,198))
                bt.setOnClickListener {
                    StoreInfo.storeId = it.id.toString()
                    val stocks = Intent(this, StoreStock::class.java)
                    startActivity(stocks)
                }
                bt.textSize = 20f
                bt.text = "店舗ID："+s.storeId
                ll.addView(bt)
            }
            storescroll.addView(ll)
        }

        AllStoresReturnButton.setOnClickListener {
            finish()
        }

    }
    private fun getStore() : String {
        val commServer = CommServer()
        commServer.setUrl(CommServer.GET_STORE_INFO)
        commServer.execute(CommServer.UB)

        while(commServer.responseCode == -1) { /* wait for response */ }

        return if(commServer.responseCode == HttpURLConnection.HTTP_OK){
            Log.i("AllStores>>>", commServer.get())
            commServer.get()
        } else {
            AlertDialog.Builder(this)
                .setTitle("●通信失敗")
                .setMessage("店舗情報が取得できませんでした：${commServer.responseCode}")
                .setPositiveButton("OK") { _, _ -> }
                .show()
            ""
        }
    }
}