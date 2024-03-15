package com.cs4520.assignment4.view.productlist

import ProductListAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cs4520.assignment4.R
import com.cs4520.assignment4.databinding.ActivityProductlistBinding
import com.cs4520.assignment4.modelview.ProductListViewModel
import java.util.logging.Logger

class ProductListFragment : Fragment() {
    private val logger = Logger.getLogger("MyLogger")
    private lateinit var binding: ActivityProductlistBinding
    private lateinit var productListRecyclerView: RecyclerView
    private lateinit var productListViewModel: ProductListViewModel
    private lateinit var productListAdapter: ProductListAdapter
    private lateinit var noProductsTextView: TextView
    private lateinit var pageNumberSpinner: Spinner
    val numbersList = (1..10).toList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityProductlistBinding.inflate(inflater, container, false)
        val rootView = binding.root
        noProductsTextView = rootView.findViewById(R.id.noProductsTextView)
        pageNumberSpinner = rootView.findViewById(R.id.pageNumberSpinner) // Initialize Spinner
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productListViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(ProductListViewModel::class.java)

        productListRecyclerView = binding.recyclerView
        productListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        productListAdapter = ProductListAdapter()
        productListRecyclerView.adapter = productListAdapter

        productListViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
                productListRecyclerView.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                productListRecyclerView.visibility = View.VISIBLE
            }
        }

        productListViewModel.products.observe(viewLifecycleOwner) { products ->
            logger.info("trying to check if the product list is empty")
            if (products.isEmpty()) {
                logger.info("no product message")
                showNoProductsMessage()
            } else {
                logger.info("hide no product message")
                hideNoProductsMessage()
                productListAdapter.submitList(products)
            }
        }

        productListViewModel.page.observe(viewLifecycleOwner) { page ->
            page?.let {
                productListViewModel.fetchProducts()
            }
        }

        productListViewModel.fetchProducts()

        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            numbersList.map { it.toString() }
        )


        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        pageNumberSpinner.adapter = spinnerAdapter

        pageNumberSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedPage = position + 1
                productListViewModel.setPage(selectedPage)
                logger.info("Selected page: $selectedPage")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun showNoProductsMessage() {
        noProductsTextView.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        productListRecyclerView.visibility = View.GONE
        binding.pageNumberSpinner.visibility = View.VISIBLE

    }

    private fun hideNoProductsMessage() {
        noProductsTextView.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        productListRecyclerView.visibility = View.VISIBLE
        binding.pageNumberSpinner.visibility = View.VISIBLE
    }


}

