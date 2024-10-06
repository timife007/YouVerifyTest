package com.timife.youverifytest.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.timife.youverifytest.R
import com.timife.youverifytest.databinding.FragmentCartBinding
import com.timife.youverifytest.presentation.adapters.CartListAdapter
import com.timife.youverifytest.presentation.states.CartUiState
import com.timife.youverifytest.presentation.viewmodels.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CartListAdapter

    private val viewModel: CartViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)

        recyclerView = binding.cartRecyclerview
        adapter = CartListAdapter(
            onDeleteItemClicked = {
                viewModel.deleteItemFromCart(it)
            }
        )
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.cartUiState.collectLatest { state ->
                when (state) {
                    is CartUiState.Loading -> {
                        // Show loading state
                        binding.cartProgress.visibility = View.VISIBLE
                        binding.errorImage.visibility = View.GONE
                    }

                    is CartUiState.Success -> {
                        val totalPrice = state.totalPrice.toString().ifBlank { "0.00" }
                        adapter.submitList(state.data)
                        binding.apply {
                            checkout.text = getString(R.string.checkout_with_price, totalPrice)
                            cartProgress.visibility = View.GONE
                            errorLayout.visibility = View.GONE
                        }
                    }

                    is CartUiState.Error -> {
                        // Show error state
                        binding.apply {
                            cartProgress.visibility = View.GONE
                            errorLayout.visibility = View.VISIBLE
                            errorImage.visibility = View.VISIBLE
                            errorText.visibility = View.VISIBLE
                            errorText.text = state.error
                            Snackbar.make(binding.root, state.error, Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
        return binding.root
    }
}