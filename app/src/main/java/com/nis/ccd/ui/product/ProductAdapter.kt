package com.nis.ccd.ui.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nis.ccd.utils.applySpanPo
import com.nis.ccd.R
import com.nis.ccd.database.schema.Cart
import com.nis.ccd.utils.loadPic
import kotlinx.android.synthetic.main.layout_product_item.view.*

typealias product = (Cart) -> Unit

class ProductAdapter(private val product: product) :
    RecyclerView.Adapter<ProductAdapter.OfferHolder>() {

    private val productList = mutableListOf<Cart>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferHolder {
        return OfferHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_product_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: OfferHolder, position: Int) {
        holder.bindUI(position)
    }

    override fun getItemCount() = productList.size

    fun setProductList(_productList: List<Cart>) {
        productList.clear()
        productList.addAll(_productList)
        notifyDataSetChanged()
    }

    inner class OfferHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bindUI(position: Int) {
            view.apply {
                productList[position].let { products ->
                    ivPic.loadPic(products.imageUrl)
                    tvProductName.text = products.productName
                    tvFlavor.text = products.flavor
                    tvFlavor.setCompoundDrawablesWithIntrinsicBounds(0, 0, products.type, 0)
                    tvPrice.applySpanPo(
                        products.price,
                        " ${resources.getString(R.string.label_rs)}",
                        R.color.red
                    )

                    itemView.setOnClickListener { product.invoke(products) }
                }
            }
        }
    }
}