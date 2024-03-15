package com.cs4520.assignment4.model.api

import androidx.lifecycle.LiveData
import retrofit2.http.GET
import com.cs4520.assignment4.model.database.Product
import retrofit2.Response
import retrofit2.http.Query

interface ApiService {
    @GET("prod/")
    suspend fun getProducts(@Query("page") page: Int? = null): Response<List<Product>>
}