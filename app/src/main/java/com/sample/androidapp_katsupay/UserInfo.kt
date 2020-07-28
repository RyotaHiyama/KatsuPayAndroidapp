package com.sample.androidapp_katsupay;

import java.util.*

/*
ユーザ情報に関する処理を行うためのクラス
 */
public class UserInfo {
    //staticなメンバ変数
    companion object {
        private var id:String = ""
        private var password:Password = Password()
        private var birthday:Date = Date()
        private var group:String = ""
        private var purchaseHistory:String = ""
        private var Balance:String = ""
    }

    //初期登録を行う
    public fun initialEntry(id:String, password:Password, eMail:String, birthday:Date): Boolean {
        return true //登録完了：true 予期せぬエラーで登録不可：false
    }

    //サーバーから最新の情報を取得する
    public fun userInfoUpdate(userInfo:UserInfo):Boolean {
        return true //更新完了：true 予期せぬエラーで更新不可：false
    }

    //購入履歴を表示する
    public fun printPurchaseHistory() {
        println(purchaseHistory) //購入履歴の表示
    }

}