package com.sample.katsupay.activity

import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.sample.katsupay.R
import com.sample.katsupay.data.UserInfo
import kotlinx.android.synthetic.main.customer_qr.*

class CustomerQR : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_qr)

        val size = 500
        var customer_qr : Bitmap = BarcodeEncoder().encodeBitmap(UserInfo.customer_id,BarcodeFormat.QR_CODE,size,size,mapOf(EncodeHintType.CHARACTER_SET to "UTF-8"))
        customerQR.setImageBitmap(customer_qr)

        CustomerQRReturnButton.setOnClickListener {
            finish()
        }


    }
}