package com.sample.katsupay

internal class Password {
    private lateinit var password:String

    constructor(pass:String) {
        this.password = pass
    }

    /* パスワード認証用メソッド */
    private fun authentication(pass:String) : Boolean {
        if(pass.equals(password)){
            return true
        }
        return false
    }

    fun getPassword():String {
        return this.password
    }

    fun passwordReset(pass:String, newPass:String) : Boolean {
        if(authentication(pass)){
//            return CommServer.passwordReset(newPass)
        }
        return false
    }
}