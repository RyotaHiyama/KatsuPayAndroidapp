package com.sample.katsupay.data

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.sample.katsupay.data.transData.Customer
import com.sample.katsupay.data.transData.Product
import com.sample.katsupay.data.transData.Store
import com.sample.katsupay.data.transData.Transaction
import org.json.JSONException
import org.json.JSONObject

object JsonParser {
    fun parse(str:String, query:String) : String? {
        var value = ""
        try {
            val jsonObjRoot = JSONObject(str)
            value = jsonObjRoot.getString(query)
        } catch (e:JSONException) {
            Log.e("ERROR", "NOT FOUND VALUE")
            return null
        }
        return value
    }

    fun transactionParse(str:String) : List<Transaction>? {
        if(str == "") return null // 何も文字列が含まれていない
        else {
            val mapper = jacksonObjectMapper()
            val jObjList:List<Transaction> = mapper.readValue(str)
            return jObjList
        }
    }

    fun customerParse(str:String) : Customer? {
        return if(str == "") null
        else {
            val mapper = jacksonObjectMapper()
            val customer:Customer = mapper.readValue(str)
            customer
        }
    }

    fun stockParse(str: String) : List<Product>? {
        return if (str == "") null
        else {
            val mapper = jacksonObjectMapper()
            val products:List<Product> = mapper.readValue(str)
            products
        }
    }

    fun storesParse(str: String) : List<Store>? {
        return if(str == "") null
        else {
            val mapper = jacksonObjectMapper()
            val stores:List<Store> = mapper.readValue(str)
            stores
        }
    }
}