package com.sample.katsupay.datas

import android.content.Context
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import java.time.YearMonth
import java.util.regex.Pattern

object DataChecker {
    /* パスワードは8~12文字の英数字に制限 */
    fun isPassword(str:String, context:Context) : Boolean {
        if("*" in str) { // '*'は使用不可
            AlertDialog.Builder(context)
                .setTitle("●サインイン失敗")
                .setMessage("不適切な文字が含まれています: *")
                .setPositiveButton("OK") { _, _ -> }
                .show()
            return false
        }

        val alpha = "[A-Za-z0-9]{8,40}" // 英数字8~12文字の制限
        val p = Pattern.compile(alpha)
        val m = p.matcher(str)
        return if(m.matches()) {
            true
        } else {
            AlertDialog.Builder(context)
                .setTitle("●サインイン失敗")
                .setMessage("パスワードは英数字8~40文字で入力してください")
                .setPositiveButton("OK") { _, _ -> }
                .show()
            false
        }

        return true
    }

    /* ユーザが広島市立大学の学生であることを仮定し"7文字の数字"でチェック */
    fun isUserId(str:String, context:Context) : Boolean {
        val alpha = "[0-9]{7}"
        val p = Pattern.compile(alpha)
        val m = p.matcher(str)
        return if(m.matches()) {
            true
        } else {
            AlertDialog.Builder(context)
                .setTitle("●サインイン失敗")
                .setMessage("ユーザ名は数字7文字で入力してください")
                .setPositiveButton("OK") { _, _ -> }
                .show()
            false
        }
    }

    fun isBirthDay(pYear:NumberPicker, pMonth:NumberPicker, pDays:NumberPicker, context:Context) : Boolean {
        val yearMonthObject
                = YearMonth.of(pYear.displayedValues[pYear.value].toInt(), pMonth.displayedValues[pMonth.value].toInt())
        val numOfDays = yearMonthObject.lengthOfMonth()

        if(numOfDays < pDays.displayedValues[pDays.value].toInt()) {
            AlertDialog.Builder(context)
                .setTitle("不適切な誕生日")
                .setMessage("存在しない日です")
                .setPositiveButton("OK"){ _, _ -> }
                .show()
            return false
        }

        return true
    }

    fun isMailAddress(emailAddress:String, context:Context) : Boolean {
        /* include '@' */
        if("@" !in emailAddress) {
            AlertDialog.Builder(context)
                .setTitle("不適切なメールアドレス")
                .setMessage("メールアドレスを確認してください")
                .setPositiveButton("OK"){ _, _ -> }
                .show()
            return false
        }
        return true
    }
}