package com.cs4520.assignment4.modelview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cs4520.assignment4.model.api.ProductApiRequest
import com.cs4520.assignment4.model.database.Product
import com.cs4520.assignment4.model.database.ProductRepository
import kotlinx.coroutines.launch
import java.util.logging.Logger

class ProductListViewModel(application: Application) : AndroidViewModel(application) {

    private val logger = Logger.getLogger("MyLogger")
    private val productApi = ProductApiRequest()

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _page = MutableLiveData<Int>()
    val page: LiveData<Int> = _page

    private var repository: ProductRepository


    init {
        repository = ProductRepository(application.applicationContext)
    }

    fun setPage(pageNumber: Int) {
        _page.value = pageNumber
    }

    // insert the product into database
    fun insertProduct(products: List<Product>) {
        viewModelScope.launch {
            products.forEach { product ->
                repository.insertProduct(product)
            }
        }
    }

    // fetch the project from the api in the case of there is no product we will be fetch from the database
    fun fetchProducts() {
        _loading.value = true
        val currentPage = _page.value ?: 1

        viewModelScope.launch {
            try {
                val fetchedProducts = productApi.fetchProducts(currentPage)
                logger.info("page being retrieve: ${currentPage}")
                logger.info("checking fetch product ")
                logger.info("current product size ${fetchedProducts.size}")

                if (!fetchedProducts.isEmpty()){
                    try{
                        insertProduct(fetchedProducts)
                        logger.info("successfully add product to the database")
                        _products.value = fetchedProducts
                    }catch (_e: Exception){
                        logger.warning("fail to add to the database: ${_e}")
                    }
                }else {
                    fetchProductsFromDatabase()
                }
                _loading.value = false
            } catch (e: Exception) {
                logger.warning("Failed to fetch products from API: ${e.message}")
                logger.info("trying to talk to the database")
                fetchProductsFromDatabase()
                _loading.value = false
            }
        }
    }


    // in case of there is no internet retrieve product from the databse
    private fun fetchProductsFromDatabase() {
        val currentPage = _page.value ?: 1

        viewModelScope.launch {
            try {
                logger.info("start talking to the database")
                val databaseProducts = repository.getProducts(currentPage)
                logger.info("Database fetch size: ${databaseProducts.size}")
                _products.value = databaseProducts
                logger.info("Successfully fetched products from database")
            } catch (e: Exception) {
                logger.warning("Failed to fetch products from database: ${e.message}")
            }
        }

    }


}