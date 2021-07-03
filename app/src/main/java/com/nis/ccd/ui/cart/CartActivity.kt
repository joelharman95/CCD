package com.nis.ccd.ui.cart

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.nis.ccd.R
import com.nis.ccd.database.schema.Cart
import com.nis.ccd.di.AppViewModel
import com.nis.ccd.ui.payment.PaymentActivity
import com.nis.ccd.ui.productdetail.ProductDetailActivity
import com.nis.ccd.utils.*
import kotlinx.android.synthetic.main.activity_cart.*

class CartActivity : AppCompatActivity() {

    private val appViewModel by lazy {
        ViewModelProvider(this, AppViewModel.Factory(this)).get(AppViewModel::class.java)
    }

    private val cartList = mutableListOf<Cart>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        rvCart.adapter = CartAdapter { cart, cartView, position ->
            when (cartView) {
                CartView.DETAIL -> {
                    val intent = Intent(this, ProductDetailActivity::class.java)
                    intent.putExtra(Constants.CART, Gson().toJson(cart))
                    startActivity(intent)
                }
                CartView.DELETE -> {
                    showAlert("Sure want to remove from your cart") {
                        appViewModel.deleteCart(cart) { count ->
                            (rvCart.adapter as CartAdapter).removeProduct(cart)
                            cartList.remove(cart)
                            if (count <= 0) {
                                tvNoCart.visibility = View.VISIBLE
                                groupPayment.visibility = View.GONE
                            }
                            updatePrice(cartList)
                            showToast("Removed from your cart")
                        }
                    }
                }
                CartView.INCREASE -> {
                    cartList[position].qty += 1
                    updatePrice(cartList)
                }
                CartView.DECREASE -> {
                    cartList[position].qty -= 1
                    updatePrice(cartList)
                }
            }
        }

        ivBack.setOnClickListener { finish() }
        btnApplyCoupons.setOnClickListener {
            PromoBottomSheetDialog.showPromoBottomSheet(supportFragmentManager, "Enter coupons") {
                if (it.isNotEmpty())
                    btnApplyCoupons.text = "Coupon Applied"
                else
                    btnApplyCoupons.text = "Apply Coupons"
            }
        }

        btnPay.setOnClickListener {
            val address = appViewModel.getAddress()
            val title = if (address == "")
                "Enter address"
            else
                "Edit address"
            AddressBottomSheetDialog.showAddressBottomSheet(
                supportFragmentManager,
                title,
                address
            ) {
                appViewModel.saveAddress(it)
                appViewModel.deleteAllCart()
                finish()
                startActivity(Intent(this, PaymentActivity::class.java))
            }
        }

        btnClearAll.setOnClickListener {
            showAlert("Sure want to remove all from your cart") {
                appViewModel.deleteAllCart()
                (rvCart.adapter as CartAdapter).removeAll()
                cartList.clear()
                tvNoCart.visibility = View.VISIBLE
                groupPayment.visibility = View.GONE
                updatePrice(cartList)
                showToast("Cart cleared")
            }
        }

    }

    override fun onResume() {
        super.onResume()
        initCart()
    }

    private fun initCart() {
        tvDeliveryPrice.applySpanPo("36", " ${resources.getString(R.string.label_rs)}", R.color.red)
        tvTaxPrice.applySpanPo("18", " ${resources.getString(R.string.label_rs)}", R.color.red)
        tvDeliveryPrice.tag = "36"
        tvTaxPrice.tag = "18"
        appViewModel.getAllCart { it ->
            if (it.isNullOrEmpty()) {
                tvNoCart.visibility = View.VISIBLE
                groupPayment.visibility = View.GONE
            } else {
                cartList.addAll(it)
                (rvCart.adapter as CartAdapter).setProductList(it)
                tvNoCart.visibility = View.GONE
                updatePrice(it)
            }
            pbCart.visibility = View.GONE
        }
    }

    private fun updatePrice(cartList: List<Cart>) {
        val totalItemPrice = cartList.map { it.price.toDouble() * it.qty.toDouble() }.sum()
        tvItemTotalPrice.tag = totalItemPrice
        tvItemTotalPrice.applySpanPo(
            "$totalItemPrice",
            " ${resources.getString(R.string.label_rs)}",
            R.color.red
        )
        val total = tvItemTotalPrice.tag.toString().toDouble() +
                tvDeliveryPrice.tag.toString().toDouble() +
                tvTaxPrice.tag.toString().toDouble()
        tvTotalPrice.tag = total
        tvTotalPrice.applySpanPo(
            "$total",
            " ${resources.getString(R.string.label_rs)}",
            R.color.red
        )
    }

}