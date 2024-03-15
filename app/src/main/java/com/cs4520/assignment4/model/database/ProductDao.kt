package com.cs4520.assignment4.model.database

import androidx.lifecycle.LiveData
import androidx.room.*
import retrofit2.http.GET

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(product: Product)

    @Update
    fun update(product: Product)

    @Delete
    fun delete(product: Product)

    @Insert
    suspend fun insertAll(products: List<Product>)

    @Query("SELECT * FROM product_table LIMIT :pageSize OFFSET :offset")
    suspend fun getProductsByPage(pageSize: Int, offset: Int): List<Product>

}