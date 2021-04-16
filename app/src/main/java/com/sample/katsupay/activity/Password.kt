package com.sample.katsupay.activity

internal class Password(pass: String) {
    private var password:String = pass

    /* パスワード認証用メソッド */
//    private fun authentication(pass:String) : Boolean {
//        return pass == password
//    }

    fun getPassword():String {
        return this.password
    }

//    fun passwordReset(pass:String, newPass:String) : Boolean {
//        return if(authentication(pass)){
//            this.password = newPass
//            true
//        } else false
//    }
}