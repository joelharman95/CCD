package com.nis.ccd.ui.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nis.ccd.R
import com.nis.ccd.database.schema.Cart
import com.nis.ccd.utils.CartView
import com.nis.ccd.utils.applySpanPo
import com.nis.ccd.utils.loadPic
import kotlinx.android.synthetic.main.layout_cart_item.view.*

typealias product = (Cart, CartView, Int) -> Unit

class CartAdapter(private val product: product) :
    RecyclerView.Adapter<CartAdapter.CartHolder>() {

    private val productList = mutableListOf<Cart>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartHolder {
        return CartHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_cart_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CartHolder, position: Int) {
        holder.bindUI(position)
    }

    override fun getItemCount() = productList.size

    fun setProductList(_productList: List<Cart>) {
        productList.clear()
        productList.addAll(_productList)
        notifyDataSetChanged()
    }

    fun removeProduct(_product: Cart) {
        productList.remove(_product)
        notifyDataSetChanged()
    }

    fun removeAll() {
        productList.clear()
        notifyDataSetChanged()
    }

    inner class CartHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bindUI(position: Int) {
            view.apply {
                productList[position].let { products ->
                    ivPic.loadPic(products.imageUrl)
                    tvProductName.text = products.productName
                    tvQty.text = products.qty.toString()
                    tvTotalPrice.setTotalPrice(products.price, products.qty.toString())

                    ivMinus.setOnClickListener {
                        val qty = tvQty.text.toString().toInt()
                        if (qty == 1)
                            tvQty.text = "1"
                        else {
                            tvQty.text = "${qty - 1}"
                            product.invoke(products, CartView.DECREASE, position)
                        }
                        tvTotalPrice.setTotalPrice(products.price, tvQty.text.toString())
                    }
                    ivPlus.setOnClickListener {
                        val qty = tvQty.text.toString().toInt()
                        if (qty == 99)
                            tvQty.text = "99"
                        else {
                            tvQty.text = "${qty + 1}"
                            product.invoke(products, CartView.INCREASE, position)
                        }
                        tvTotalPrice.setTotalPrice(products.price, tvQty.text.toString())
                    }
                    itemView.setOnClickListener { product.invoke(products, CartView.DETAIL, position) }
                    ivDelete.setOnClickListener { product.invoke(products, CartView.DELETE, position) }
                }
            }
        }

        private fun TextView.setTotalPrice(price: String, qty: String) {
            this.applySpanPo(
                (price.toDouble() * qty.toDouble()).toString(),
                " ${resources.getString(R.string.label_rs)}",
                R.color.red
            )
        }

    }
}