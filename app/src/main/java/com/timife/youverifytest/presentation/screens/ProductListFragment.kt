package com.timife.youverifytest.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.timife.youverifytest.databinding.FragmentProductListBinding
import com.timife.youverifytest.navigation.ProductDetails
import com.timife.youverifytest.presentation.adapters.ProductListAdapter
import com.timife.youverifytest.presentation.states.ProductUiState
import com.timife.youverifytest.presentation.utils.Utils
import com.timife.youverifytest.presentation.viewmodels.products.ProductsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProductListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductListAdapter
    private val viewModel: ProductsViewModel by viewModels()
    private lateinit var binding: FragmentProductListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentProductListBinding.inflate(inflater, container, false)
        recyclerView = binding.productsRecyclerview
        adapter = ProductListAdapter(
            onItemClickListener = { id ->
                findNavController().navigate(
                    ProductDetails(id),
                    Utils.navOptions
                )
            }
        )
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.uiState.collect {state ->
                when (state) {
                    is ProductUiState.Loading -> {
                        // Show loading state
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is ProductUiState.Success -> {
                        adapter.submitList(state.products)
                        binding.progressBar.visibility = View.GONE
                    }

                    is ProductUiState.Error -> {
                        // Show error state
                        Snackbar.make(binding.root, state.error, Snackbar.LENGTH_LONG).show()
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
        return binding.root
    }
}