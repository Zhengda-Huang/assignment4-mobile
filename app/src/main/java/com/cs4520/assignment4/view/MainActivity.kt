package com.cs4520.assignment4.view

import LoginFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cs4520.assignment4.R
import com.cs4520.assignment4.modelview.ProductListViewModel
import com.cs4520.assignment4.view.productlist.ProductListFragment

class MainActivity : AppCompatActivity(), LoginFragment.OnLoginSuccessListener {

    private var isLoggedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_temp)

        if (savedInstanceState == null) {
            val loginFragment = LoginFragment()
            loginFragment.setOnLoginSuccessListener(this)
            switchFragment(loginFragment)
        }
    }

    override fun onLoginSuccess() {
        isLoggedIn = true
        showProductList()
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun showProductList() {

        val productListFragment = ProductListFragment()
        switchFragment(productListFragment)
    }
}
