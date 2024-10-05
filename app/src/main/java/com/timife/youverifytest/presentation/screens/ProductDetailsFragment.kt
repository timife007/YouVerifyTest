package com.timife.youverifytest.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.timife.youverifytest.R
import com.timife.youverifytest.databinding.FragmentProductDetailsBinding
import com.timife.youverifytest.presentation.states.DetailUiState
import com.timife.youverifytest.presentation.utils.Utils
import com.timife.youverifytest.presentation.viewmodels.ProductDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        lifecycleScope.launch {
            viewModel.detailUiState.collectLatest {state ->
                when (state) {
                    is DetailUiState.Loading -> {
                        // Show loading state
                        binding.detailsProgress.visibility = View.VISIBLE
                    }

                    is DetailUiState.Success -> {
                        binding.detailsProgress.visibility = View.GONE
                        binding.apply {
                            Utils.loadImage(requireContext(), productDetailImage, state.data.image, imageProgress)
                            title.text = state.data.title
                            description.text = state.data.description
                            price.text = getString(R.string.price_label, state.data.price)
                            rating.text = state.data.rating.toString()
                            reviews.text = getString(R.string._no_of_reviews, state.data.review)
                        }
                    }

                    is DetailUiState.Error -> {
                        // Show error state
                        Snackbar.make(binding.root, state.error, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.quantity.collectLatest { qty ->
                val updateCart = {
                    viewModel.updateItemInCart(qty + 1)
                }
                binding.apply {
                    addToCart.setOnClickListener { updateCart() }
                    plus.setOnClickListener { updateCart() }
                    minus.setOnClickListener { viewModel.updateItemInCart(qty - 1) }

                    //visibility logic based on
                    addToCart.visibility = if (qty != 0) View.GONE else View.VISIBLE
                    quantity.visibility = if (qty != 0) View.VISIBLE else View.GONE
                    if (qty != 0) {
                        quantityText.text = qty.toString()
                    }
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}