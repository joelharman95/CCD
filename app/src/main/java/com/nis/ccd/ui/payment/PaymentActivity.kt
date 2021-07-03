package com.nis.ccd.ui.payment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nis.ccd.R
import kotlinx.android.synthetic.main.activity_payment.*

class PaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        ivBack.setOnClickListener { finish() }
        btnOrderMore.setOnClickListener { finish() }

    }
}