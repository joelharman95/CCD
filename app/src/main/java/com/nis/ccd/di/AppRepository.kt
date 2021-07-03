package com.nis.ccd.di

import com.nis.ccd.database.AppDatabase
import com.nis.ccd.database.schema.Cart
import com.nis.ccd.utils.OnError
import com.nis.ccd.utils.OnSuccess
import kotlinx.coroutines.*

class AppRepository(private val appDatabase: AppDatabase) : IAppRepository {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun addCart(cart: Cart) {
        scope.launch {
            appDatabase.cartDao().addCart(cart)
        }
    }

    override fun deleteCart(cart: Cart, onSuccess: OnSuccess<Int>) {
        scope.launch {
            val count = appDatabase.cartDao().deleteCart(cart)
            withContext(Dispatchers.Main) { onSuccess(count) }
        }
    }

    override fun deleteAllCart() {
        scope.launch {
            appDatabase.cartDao().deleteAllCart()
        }
    }

    override fun addOrUpdateCart(cart: Cart) {
        scope.launch {
            appDatabase.cartDao().addOrUpdateCart(cart)
        }
    }

    override fun getAllCart(onSuccess: OnSuccess<List<Cart>>) {
        scope.launch {
            val cartList: List<Cart> = appDatabase.cartDao().getAllCart()
            withContext(Dispatchers.Main) { onSuccess(cartList) }
        }
    }

    override fun getCartCount(onSuccess: OnSuccess<Int>) {
        scope.launch {
            val cartCount = appDatabase.cartDao().getCartCount()
            withContext(Dispatchers.Main) { onSuccess(cartCount) }
        }
    }

    override fun getCartByProductId(
        productId: Int,
        onSuccess: OnSuccess<Cart>,
        onError: OnError<Unit>
    ) {
        scope.launch {
            val cart = appDatabase.cartDao().getCartByProductId(productId)
            withContext(Dispatchers.Main) {
                if (cart != null)
                    onSuccess(cart)
                else
                    onError(Unit)
            }
        }
    }

}


interface IAppRepository {

    fun addCart(cart: Cart)
    fun deleteCart(cart: Cart, onSuccess: OnSuccess<Int>)
    fun deleteAllCart()
    fun addOrUpdateCart(cart: Cart)
    fun getAllCart(onSuccess: OnSuccess<List<Cart>>)
    fun getCartByProductId(productId: Int, onSuccess: OnSuccess<Cart>, onError: OnError<Unit>)
    fun getCartCount(onSuccess: OnSuccess<Int>)

    companion object {
        fun getAppRepository(appDatabase: AppDatabase): IAppRepository {
            return AppRepository(appDatabase)
        }
    }
}