package com.cs4520.assignment4.model.database

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository(context: Context) {

    private var productDao: ProductDao
    private val database = ProductDatabase.getInstance(context)

    init {
        productDao = database.productDao()
    }

    suspend fun insertProduct(product: Product) {
        withContext(Dispatchers.IO) {
            productDao.insert(product)
        }
    }

    suspend fun getProducts(page: Int ): List<Product> {
        val pageSize = 30
        val offset = (page - 1) * pageSize
        return productDao.getProductsByPage(page, offset)
    }
}
