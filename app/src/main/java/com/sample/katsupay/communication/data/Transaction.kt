package com.sample.katsupay.communication.data

import com.fasterxml.jackson.annotation.JsonProperty
import com.sample.katsupay.data.TransactionInfo
import kotlin.system.exitProcess

data class Transaction(
    @JsonProperty("customerId")      val customerId:String,
    @JsonProperty("storeId")         val storeId:String,
    @JsonProperty("transactionTime") val time:String,
    @JsonProperty("price")           val price:String,
    @JsonProperty("productInfoList") val products:Array<String>?,
    @JsonProperty("charge")          val isCharge:String
) {
    override fun toString() : String {
        var prods = ""
        return if(products != null) {
            products.forEach {
                prods += "$it, "
            }
            prods = prods.substring(0, prods.length - 2)
            "顧客ID：$customerId\n購入店舗ID：$storeId\n時間：$time\n取引金額：$price\n購入商品：$prods\n"
        } else "顧客ID：$customerId\n購入店舗ID：$storeId\n時間：$time\nチャージ金額：$price\n"
    }

//    companion object{
//        fun getUserInfoTransaction() :Transaction {
//            val customerid = TransactionInfo.customer_id ?: exitProcess(-1)
//            val storeid = TransactionInfo.store_id ?: exitProcess(-1)
//            return Transaction(
//                customerid,
//                storeid,
//                TransactionInfo.time,
//                TransactionInfo.price,
//                TransactionInfo.products,
//                TransactionInfo.isCharge
//            )
//        }
//    }
}