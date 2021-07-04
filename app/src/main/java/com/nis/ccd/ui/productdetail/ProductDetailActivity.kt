package com.nis.ccd.ui.productdetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.nis.ccd.utils.applySpanPo
import com.google.gson.Gson
import com.nis.ccd.R
import com.nis.ccd.database.schema.Cart
import com.nis.ccd.di.AppViewModel
import com.nis.ccd.utils.Constants.CART
import com.nis.ccd.utils.loadPic
import com.nis.ccd.utils.showToast
import kotlinx.android.synthetic.main.activity_product_detail.*

class ProductDetailActivity : AppCompatActivity() {

    private val appViewModel by lazy {
        ViewModelProvider(this, AppViewModel.Factory(this)).get(AppViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        ivBack.setOnClickListener { finish() }

        val cart: Cart = Gson().fromJson(intent.getStringExtra(CART), Cart::class.java)
        getCartByProductId(cart)

        ivPic.loadPic(cart.imageUrl, cart.type)
        tvProductName.text = cart.productName
        tvFlavor.text = cart.flavor
        tvFlavor.setCompoundDrawablesWithIntrinsicBounds(0, 0, cart.type, 0)
        tvPrice.applySpanPo(
            cart.price,
            " ${resources.getString(R.string.label_rs)}",
            R.color.red
        )

        ivMinus.setOnClickListener {
            val qty = tvQty.text.toString().toInt()
            if (qty == 1)
                tvQty.text = "1"
            else
                tvQty.text = "${qty - 1}"
        }
        ivPlus.setOnClickListener {
            val qty = tvQty.text.toString().toInt()
            if (qty == 99)
                tvQty.text = "99"
            else
                tvQty.text = "${qty + 1}"
        }

        btnAdd.setOnClickListener {
            cart.qty = tvQty.text.toString().toInt()
            cart.totalPrice = (cart.qty.toDouble() * cart.price.toDouble()).toString()
            appViewModel.addOrUpdateCart(cart)
            showToast("Added successfully")
            finish()
        }
    }

    private fun getCartByProductId(cart: Cart) {
        appViewModel.getCartByProductId(cart.productId, onSuccess = {
            tvQty.text = "${it.qty}"
            cart.qty = it.qty
        }, onError = {

        })
    }

}