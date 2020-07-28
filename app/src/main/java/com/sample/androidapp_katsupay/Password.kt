package com.sample.androidapp_katsupay;

/*
パスワードを保持するためのクラス
 */
public class Password {
    companion object {
        private var password:String = ""

        //パスワード再設定用のメソッド
        public fun passwordReset(id:String, passeord:Password, newPassword:String): Boolean {
            return true //パスワードの再設定完了：true 予期せぬエラー：false
        }
    }
}