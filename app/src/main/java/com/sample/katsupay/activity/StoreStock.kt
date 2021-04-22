package com.sample.katsupay.activity

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.sample.katsupay.R
import com.sample.katsupay.communication.CommServer
import com.sample.katsupay.data.JsonParser
import com.sample.katsupay.communication.data.Product
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



//    private val spinnerItems = arrayOf("商品名","値段が安い順","値段が高い順")

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

//            makeScreen(products!!,showStocks)
            pullDownMenu()
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

        while(commServer.responseCode == -1) { /* wait for response */ }

        if(commServer.responseCode == HttpURLConnection.HTTP_OK){
            Log.i("products>>>", commServer.get())
            return commServer.get()
        } else {
            AlertDialog.Builder(this)
                .setTitle("●通信失敗")
                .setMessage("在庫情報が取得できませんでした：${commServer.responseCode}")
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
            Stock <= 3 -> {
                "<big><font color=#ffa500>△</font></big>"
            }
            else       -> {
                "〇"
            }
        }
    }

    private fun sortFiler(products: List<Product>, mode: Int): List<Product>? {
        return when (mode) {
            PRODUCTNAME -> {
//                nameSort(products)
                products
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
                Log.i("searchView>>>",newText)
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

    private fun makeScreen(products: List<Product>?, mode :Boolean) { //1: show only in stock  else: show stock
        if(products == null){
            stockinfo.text = getString(R.string.検索なし)
            return
        }

        stockinfo.text = ""
        stockinfo.typeface = Typeface.DEFAULT_BOLD
        var text = ""
        products.forEach{
            var oneStockText = ""
            if (it.on_sale.toBoolean()){
                oneStockText = "${oneStockText}商品名：${it.name}<br>"
                oneStockText = "${oneStockText}値段：${it.price}<br>"
                oneStockText = "${oneStockText}在庫：" + judgeStock(it.stock) + "<br><br>"
            }
            text += if (mode && it.stock <= 0) "" else oneStockText

        }
        stockinfo.text = Html.fromHtml(text)
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
                        makeScreen(products!!,showStocks)
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

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun priceSort(products: List<Product>?) : List<Product> {
        return products!!.sortedBy { it.price }
    }

//    private fun nameSort(products: List<Product>?) : List<Product> {
//        return products!!.sortedBy { it.name }
//    }

//    private fun ProductImage() : View? {
//       return pro1000duct_image.setImageResource(R.mipmap.ic_launcher)
//    }
}