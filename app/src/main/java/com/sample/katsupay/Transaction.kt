package com.sample.katsupay

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp

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
        if(products != null) {
            products.forEach {
                prods += "${it.toString()}, "
            }
            prods = prods.substring(0, prods.length - 2)
            return "顧客ID：$customerId\n購入店舗ID：$storeId\n時間：$time\n取引金額：$price\n購入商品：$prods\n"
        } else return "顧客ID：$customerId\n購入店舗ID：$storeId\n時間：$time\nチャージ金額：$price\n"
    }
}