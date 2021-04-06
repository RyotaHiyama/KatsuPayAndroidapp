package com.sample.katsupay

import android.content.ClipData
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.sample.katsupay.communication.CommServer
import com.sample.katsupay.data.JsonParser
import com.sample.katsupay.data.transData.Product
import kotlinx.android.synthetic.main.store_stock.*
import java.net.HttpURLConnection

class StoreStock : AppCompatActivity() {
    private var products:List<Product>? = arrayListOf()
    private var showStocks:Boolean = false
    private var skeyword:String = ""
    private var onlystock:Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.store_stock)

        stockinfo.isSingleLine = false

        val str = getStock()
        products = JsonParser.stockParse(str)

        if(products == null) {
            AlertDialog.Builder(this)
                .setTitle("●通信失敗")
                .setMessage("在庫情報が取得できませんでした")
                .setPositiveButton("OK") { _, _ -> }
                .show()
            finish()
        } else {
            if(products!!.isEmpty()) {
                AlertDialog.Builder(this)
                    .setMessage("在庫情報がありません")
                    .setPositiveButton("OK") { _, _ -> finish() }
                    .show()
            }

            makeScreen(products!!,showStocks)
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

    fun productsFilter(products: List<Product>, keyword: String) : List<Product>? {
        val hitProducts:MutableList<Product> = arrayListOf()

        if (keyword.isEmpty()) return products

        for(p in products) {
            if(p.getProductString().contains(keyword)){
                Log.i("add>>>", p.toString())
                hitProducts.add(p)
            }
        }

        return if(hitProducts.isEmpty()) null
        else hitProducts
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.stock_menu, menu)
        val searchItem = menu?.findItem(R.id.stock_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.queryHint = getString(R.string.検索)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                //text changed
                Log.i("searchView>>>",newText)
                if(products == null) return false

                if (newText != null) {
                    skeyword = newText
                    Log.i("if>>>",newText)
                    makeScreen(productsFilter(products!!, skeyword),showStocks)
                }

                return false
            }
            override fun onQueryTextSubmit(query: String?): Boolean {
                //submit button pressed
                return false
            }
        })
        

        return true
    }

    private fun makeScreen(products: List<Product>?, mode :Boolean) { //1: show only in stock  else: show stock
        if(products == null){
            stockinfo.text = getString(R.string.検索なし)
            return
        }

        stockinfo.text = ""
        stockinfo.typeface = Typeface.DEFAULT_BOLD
        var text = ""
        products.forEach{
            if (mode) {
                if (it.on_sale.toBoolean() && it.stock > 0 ){
                    text = "${text}商品名：${it.name}<br>"
                    text = "${text}値段：${it.price}<br>"
                    text = "${text}在庫：" + judgeStock(it.stock) + "<br><br>"
                }
            } else {
                if (it.on_sale.toBoolean()){
                    text = "${text}商品名：${it.name}<br>"
                    text = "${text}値段：${it.price}<br>"
                    text = "${text}在庫：" + judgeStock(it.stock) + "<br><br>"
                }
            }
        }
        stockinfo.text = Html.fromHtml(text)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.search_switch -> {
                if(onlystock){
                    //On
                    Log.i("on",onlystock.toString())
                    Toast.makeText(this,"在庫ありのみを表示", Toast.LENGTH_SHORT).show()
                    onlystock = false
                    showStocks = true
                    makeScreen(productsFilter(products!!,skeyword),showStocks)
                    item.title = "すべての在庫を表示"
                } else {
                    //Off
                    Log.i("off",onlystock.toString())
                    Toast.makeText(this,"すべての在庫を表示", Toast.LENGTH_SHORT).show()
                    onlystock = true
                    showStocks = false
                    makeScreen(productsFilter(products!!, skeyword),showStocks)
                    item.title = "在庫ありのみを表示"
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

//    private fun ProductImage() : View? {
//       return pro1000duct_image.setImageResource(R.mipmap.ic_launcher)
//    }
}