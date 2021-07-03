package com.nis.ccd.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nis.ccd.R
import com.nis.ccd.database.AppDatabase
import com.nis.ccd.database.schema.Cart
import com.nis.ccd.preference.IPreferenceManager
import com.nis.ccd.preference.PreferenceManager
import com.nis.ccd.utils.OnError
import com.nis.ccd.utils.OnSuccess
import kotlinx.coroutines.launch

class AppViewModel(private val repository: IAppRepository, private val pref: IPreferenceManager) :
    ViewModel() {

    fun getOffers() = listOf(
        "https://www.cafecoffeeday.com/sites/default/files/Frappology-website-banner.png",
        "https://www.cafecoffeeday.com/sites/default/files/Heritage-Banner-1920X760.jpg",
        "https://www.cafecoffeeday.com/sites/default/files/1920x760%20%281%29.jpg"
    )

    fun getProducts() = listOf(
        Cart(
            productId = 1,
            productName = "Café Frappe",
            flavor = "Cold Coffee Delight",
            type = R.drawable.ic_coffee,
            imageUrl = "https://www.cafecoffeeday.com/sites/default/files/Cafe-frappe_2.png",
            price = "260"
        ),
        Cart(
            productId = 2,
            productName = "Rasmalai Smoothie",
            flavor = "Swanky Smoothles",
            type = R.drawable.ic_coffee,
            imageUrl = "https://www.cafecoffeeday.com/sites/default/files/Rasmalai-smoothie_2.png",
            price = "270"
        ),
        Cart(
            productId = 3,
            productName = "Black Forest Cake",
            flavor = "Celebration Cakes",
            type = R.drawable.ic_cake,
            imageUrl = "https://www.cafecoffeeday.com/sites/default/files/500x378-Black-Forest_0.jpg",
            price = "650"
        ),
        Cart(
            productId = 4,
            productName = "Pineapple cake",
            flavor = "Celebration Cakes",
            type = R.drawable.ic_cake,
            imageUrl = "https://www.cafecoffeeday.com/sites/default/files/500x378-pineapple-cake_0.jpg",
            price = "580"
        ),
        Cart(
            productId = 5,
            productName = "Crunchy Frappe",
            flavor = "Cold Coffee Delight",
            type = R.drawable.ic_coffee,
            imageUrl = "https://www.cafecoffeeday.com/sites/default/files/Crunchy-Frappe_2.png",
            price = "310"
        ),
        Cart(
            productId = 6,
            productName = "Glide With Green Mint",
            flavor = "Infusion",
            type = R.drawable.ic_coffee,
            imageUrl = "https://www.cafecoffeeday.com/sites/default/files/4_500x378_green-mint_1.jpg",
            price = "269"
        ),
        Cart(
            productId = 7,
            productName = "Kesari Delight Milkshake",
            flavor = "Milkshakes",
            type = R.drawable.ic_coffee,
            imageUrl = "https://www.cafecoffeeday.com/sites/default/files/kesar.jpg",
            price = "250"
        ),
    )

    fun saveAddress(address: String) {
        viewModelScope.launch {
            pref.saveAddress(address)
        }
    }

    fun getAddress() = pref.getAddress()

    fun addCart(cart: Cart) {
        viewModelScope.launch {
            repository.addCart(cart)
        }
    }

    fun deleteCart(cart: Cart, onSuccess: OnSuccess<Int>) {
        viewModelScope.launch {
            repository.deleteCart(cart, onSuccess)
        }
    }

    fun deleteAllCart() {
        viewModelScope.launch {
            repository.deleteAllCart()
        }
    }

    fun addOrUpdateCart(cart: Cart) {
        viewModelScope.launch {
            repository.addOrUpdateCart(cart)
        }
    }

    fun getAllCart(onSuccess: OnSuccess<List<Cart>>) {
        viewModelScope.launch {
            repository.getAllCart(onSuccess)
        }
    }

    fun getCartCount(onSuccess: OnSuccess<Int>) {
        viewModelScope.launch {
            repository.getCartCount(onSuccess)
        }
    }

    fun getCartByProductId(productId: Int, onSuccess: OnSuccess<Cart>, onError: OnError<Unit>) {
        viewModelScope.launch {
            repository.getCartByProductId(productId, onSuccess, onError)
        }
    }


    class Factory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
        private val repository by lazy {
            IAppRepository.getAppRepository(AppDatabase.create(context))
        }
        private val pref by lazy {
            PreferenceManager(context)
        }

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AppViewModel(repository, pref) as T
        }
    }

}