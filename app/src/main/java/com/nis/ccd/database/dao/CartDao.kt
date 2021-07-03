package com.nis.ccd.database.dao

import androidx.room.*
import com.nis.ccd.database.schema.Cart

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCart(vararg cart: Cart)

    @Query("Select * from cart")
    fun getAllCart(): List<Cart>

    @Query("Select * from cart where product_id =:productId")
    fun getCartByProductId(productId: Int): Cart

    @Query("update cart set qty =:quantity, total_price =:totalPrice where product_id =:productId")
    fun updateCart(
        productId: Int,
        quantity: Int,
        totalPrice: String,
    )

    @Delete
    fun delete(vararg cart: Cart)

    @Query("delete from cart")
    fun deleteAllCart()

    @Query("select sum(total_price) as total from cart")
    fun getTotalAmount(): Double

    @Query("select count(*) from cart")
    fun getCartCount(): Int

    @Transaction
    fun updateAndGetTotalPrice(productId: Int, qty: Int, totalPrice: String): Double {
        updateCart(productId, qty, totalPrice)
        return getTotalAmount()
    }

    @Transaction
    fun getTotalAmountWithCount(): Double {
        return if (getCartCount() > 0)
            getTotalAmount()
        else 0.0
    }

    @Transaction
    fun deleteCart(cart: Cart): Int {
        delete(cart)
        return getCartCount()
    }

    @Transaction
    fun addOrUpdateCart(card: Cart) {
        if (getCartByProductId(card.productId) == null)
            addCart(card)
        else
            updateCart(card.productId, card.qty, card.totalPrice)
    }
}