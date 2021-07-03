package com.nis.ccd.ui.product

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import com.nis.ccd.R
import com.nis.ccd.database.schema.Cart
import com.nis.ccd.di.AppViewModel
import com.nis.ccd.ui.cart.CartActivity
import com.nis.ccd.ui.productdetail.ProductDetailActivity
import com.nis.ccd.utils.Constants.CART
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    /* for activity
    implementation "androidx.activity:activity-ktx:1.2.2"
    for fragment
    implementation "androidx.fragment:fragment-ktx:1.3.2"
    private val appViewModel: AppViewModel by viewModels { AppViewModel.Factory(this) }*/

    private val appViewModel by lazy {
        ViewModelProvider(this, AppViewModel.Factory(this)).get(AppViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvProduct.requestFocus()
        initOffer(appViewModel.getOffers())
        initProduct(appViewModel.getProducts())

        ivCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() == "") {
                    (rvProduct.adapter as ProductAdapter).setProductList(appViewModel.getProducts())
                } else {
                    val filteredList = appViewModel.getProducts()
                        .filter { cart ->
                            cart.flavor.toString().toLowerCase(Locale.getDefault())
                                .contains("$s")
                        }
                    if (filteredList.isEmpty())
                        tvNoProduct.visibility = View.VISIBLE
                    else
                        tvNoProduct.visibility = View.GONE
                    (rvProduct.adapter as ProductAdapter).setProductList(filteredList)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

    }

    override fun onResume() {
        super.onResume()
        appViewModel.getCartCount {
            if (it != 0)
                tvCartCount.text = "${it}"
            else
                tvCartCount.text = ""
        }
    }

    private fun initProduct(products: List<Cart>) {
        rvProduct.adapter = ProductAdapter {
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra(CART, Gson().toJson(it))
            startActivity(intent)
        }
        (rvProduct.adapter as ProductAdapter).setProductList(products)
    }

    private var currentPage = 0
    private fun initOffer(offersList: List<String>) {
        vpOffer.adapter = OfferAdapter(offersList)
        vpOffer.offscreenPageLimit = 3
        vpOffer.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        vpOffer.currentItem = 0
        showDots(offersList.size, 0)
        vpOffer.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                showDots(offersList.size, position)
                super.onPageSelected(position)
            }
        })
        val handler = Handler(Looper.getMainLooper())
        val update = label@ Runnable {
            if (currentPage == offersList.size)
                currentPage = 0
            vpOffer.setCurrentItem(currentPage++, true)
        }
        Timer().schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, 3000, 3000)
    }

    private fun showDots(size: Int, position: Int) {
        if (llDots.childCount > 0)
            llDots.removeAllViews()
        val dots = arrayOfNulls<ImageView>(size)
        for (i in 0 until size) {
            dots[i] = ImageView(this)
            if (i == position) dots[i]?.setImageResource(R.drawable.dot_active)
            else dots[i]!!.setImageResource(R.drawable.dot_inactive)
            val layoutParams = LinearLayoutCompat.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(4, 0, 4, 0)
            llDots.addView(dots[i], layoutParams)
        }
    }

}