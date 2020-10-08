package com.sample.katsupay

import android.util.Log
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
}