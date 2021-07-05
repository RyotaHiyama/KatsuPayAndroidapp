package com.sample.katsupay.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.sample.katsupay.R
import com.sample.katsupay.communication.CommServer
import com.sample.katsupay.data.JsonParser
import com.sample.katsupay.communication.data.Product
import com.sample.katsupay.data.ProductInfo
import kotlinx.android.synthetic.main.store_stock.*
import java.net.HttpURLConnection

class StoreStock : AppCompatActivity() {
    companion object{
        const val PRODUCTNAME    = 0
        const val CHEAPPRICE     = 1
        const val EXPENSIVEPRICE = 2
    }
    private var products:List<Product>? = arrayListOf()
    private var showStocks:Boolean = false
    private var skeyword:String = ""
    private var sortmode:Int = PRODUCTNAME
    private val commServer = CommServer()
    private val productImageMap = mutableMapOf<String,String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.store_stock)

        val str = getStock()
        products = JsonParser.stockParse(str)

        products?.forEach {
            val commServer2  = CommServer()
            ProductInfo.product_id = it.product_id
            productImageMap[it.product_id] = getStockImage(commServer2)
        }

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
            pullDownMenu()
        }

        StoreStockReturnButton.setOnClickListener{
            finish()
        }
    }

    private fun getStock() : String {
        commServer.setUrl(CommServer.GET_STOCK_INFO)
        commServer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,CommServer.UB)

        while(commServer.responseCode == -1) { /* wait for response */ }

        if(commServer.responseCode == HttpURLConnection.HTTP_OK){
            Log.i("products>>>", commServer.get())
            return commServer.get()
        } else {
            stockAlert()
        }
        return ""
    }

    private fun getStockImage(commserver :CommServer) : String {
        commserver.setUrl(CommServer.GET_STOCK_IMAGE)
        commserver.execute(CommServer.UB)
        while(commserver.responseCode == -1) { /* wait for response */ }

        if (commserver.responseCode == HttpURLConnection.HTTP_OK){
            return commserver.get()
        } else if (commserver.responseCode == HttpURLConnection.HTTP_NOT_FOUND){
            stockAlert()
        }
        return ""
    }

    private fun stockAlert() {
        AlertDialog.Builder(this)
            .setTitle("●通信失敗")
            .setMessage("在庫情報が取得できませんでした：${commServer.responseCode}")
            .setPositiveButton("OK") { _, _ -> }
            .show()
    }

    //在庫の数によって3段階で表示
    private fun judgeStock(Stock:Int) : String {
        return when {
            Stock <= 0 -> {
                "<big><font color=\"red\">×</font></big>"
            }
            Stock <= 3 -> {
                "<big><font color=#ffa500>△</font></big>"
            }
            else       -> {
                "〇"
            }
        }
    }

    //現在どの並べ替えがされているか
    private fun sortFiler(products: List<Product>, mode: Int): List<Product>? {
        return when (mode) {
            PRODUCTNAME -> {
                nameSort(products)
            }
            CHEAPPRICE -> {
                priceSort(products)
            }
            EXPENSIVEPRICE -> {
                priceSort(products).reversed()
            }
            else -> null
        }
    }

    //検索機能
    fun productsFilter(products: List<Product>, keyword: String, mode: Int) : List<Product>? {
        val hitProducts:MutableList<Product> = arrayListOf()
        if (keyword.isEmpty()) return sortFiler(products, mode)

        for(p in products) {
            if(p.getProductString().contains(keyword)){
                Log.i("add>>>", p.toString())
                hitProducts.add(p)
            }
        }

        return if(hitProducts.isEmpty()) null
        else sortFiler(hitProducts, mode)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.stock_menu, menu)
        val searchItem = menu?.findItem(R.id.stock_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.queryHint = getString(R.string.検索)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                //text changed
//                Log.i("searchView>>>",newText)
                if(products == null) return false

                if (newText != null) {
                    skeyword = newText
                    Log.i("if>>>",newText)
                    makeScreen(productsFilter(products!!, skeyword, sortmode),showStocks)
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

    //在庫を表示
    private fun makeScreen(products: List<Product>?, mode :Boolean) { //1: show only in stock  else: show stock
        ll.removeAllViews()
        if(products == null){
            val stockInfo = TextView(this)
            stockInfo.text = getString(R.string.検索なし)
            ll.addView(stockInfo)
            return
        }
        var text: String
        products.forEach{
            val stockInfo = TextView(this)
            var oneStockText = ""
            if (it.on_sale.toBoolean()){
                val stockImage = ImageView(this)
                stockInfo.typeface = Typeface.DEFAULT_BOLD
                stockInfo.textSize = 25f
                stockInfo.isSingleLine = false
                oneStockText = "${oneStockText}商品名：${it.name}<br>"
                oneStockText = "${oneStockText}値段：${it.price}<br>"
                oneStockText = "${oneStockText}在庫：" + judgeStock(it.stock) + "<br><br>"
                text = if (mode && it.stock <= 0) {
                    ""
                } else {
                    stockImage.setImageBitmap(productImage(productImageMap[it.product_id]))  //画像を設定
                    ll.addView(stockImage)  //画像を追加
                    oneStockText
                }
                stockInfo.text = Html.fromHtml(text)
                ll.addView(stockInfo)  //在庫情報の追加
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.search_switch -> {
                if(showStocks){
                    //On
                    Log.i("off",showStocks.toString())
                    Toast.makeText(this,"すべての在庫を表示", Toast.LENGTH_SHORT).show()
                    showStocks = false
                    makeScreen(productsFilter(products!!, skeyword, sortmode),showStocks)
                    item.title = "在庫ありのみを表示"
                } else {
                    //Off
                    Log.i("on",showStocks.toString())
                    Toast.makeText(this,"在庫ありのみを表示", Toast.LENGTH_SHORT).show()
                    showStocks = true
                    makeScreen(productsFilter(products!!,skeyword, sortmode),showStocks)
                    item.title = "すべての在庫を表示"
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun pullDownMenu() {
        val spinnerItems = arrayOf("商品名", "値段が安い順", "値段が高い順")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (parent?.selectedItem as String) {
                    "商品名" -> {
                        sortmode = PRODUCTNAME
                        makeScreen(productsFilter(nameSort(products), skeyword, sortmode),showStocks)
                    }
                    "値段が安い順" -> {
                        sortmode = CHEAPPRICE
                        makeScreen(productsFilter(priceSort(products), skeyword, sortmode),showStocks)
                    }
                    "値段が高い順" -> {
                        sortmode = EXPENSIVEPRICE
                        makeScreen(productsFilter(priceSort(products).reversed(), skeyword, sortmode),showStocks)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    //値段で並べ替え
    private fun priceSort(products: List<Product>?) : List<Product> {
        return products!!.sortedBy { it.price }
    }

    //名前で並べ替え
    private fun nameSort(products: List<Product>?) : List<Product> {
        return products!!.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.name })
    }

    private fun productImage(base64productImage : String?) : Bitmap{
        return if (base64productImage == ""){
            BitmapFactory.decodeResource(resources, R.drawable.noimage)
        } else {
            val decodeImage = Base64.decode(base64productImage, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodeImage, 0, decodeImage.size)
        }
    }

}