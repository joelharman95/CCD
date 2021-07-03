package com.nis.ccd.database.schema

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class Cart(
    @PrimaryKey var sNo: Long? = null,
    @ColumnInfo(name = "product_id") var productId: Int = 0,
    @ColumnInfo(name = "product_name") var productName: String? = null,
    @ColumnInfo(name = "flavor") var flavor: String? = null,
    @ColumnInfo(name = "type") var type: Int = 0,
    @ColumnInfo(name = "price") var price: String = "0.0",
    @ColumnInfo(name = "qty") var qty: Int = 0,
    @ColumnInfo(name = "total_price") var totalPrice: String = "0.0",
    @ColumnInfo(name = "image_url") var imageUrl: String = "",
)