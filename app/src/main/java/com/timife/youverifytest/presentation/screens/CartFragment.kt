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
import com.paystack.android.core.Paystack
import com.paystack.android.ui.paymentsheet.PaymentSheet
import com.paystack.android.ui.paymentsheet.PaymentSheetResult
import com.timife.youverifytest.BuildConfig
import com.timife.youverifytest.R
import com.timife.youverifytest.databinding.FragmentCartBinding
import com.timife.youverifytest.navigation.ProductList
import com.timife.youverifytest.presentation.adapters.CartListAdapter
import com.timife.youverifytest.presentation.states.CartUiState
import com.timife.youverifytest.presentation.states.InitPaymentState
import com.timife.youverifytest.presentation.utils.Utils
import com.timife.youverifytest.presentation.viewmodels.cart.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CartListAdapter
    private val viewModel: CartViewModel by viewModels()
    private lateinit var paymentSheet: PaymentSheet

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
                        val totalPrice =
                            state.totalPrice.toString().ifBlank { getString(R.string._0_00) }
                        adapter.submitList(state.data)
                        binding.apply {
                            checkoutText.text = getString(R.string.checkout_with_price, totalPrice)
                            cartProgress.visibility = View.GONE
                            errorLayout.visibility = View.GONE
                        }
                    }

                    is CartUiState.Error -> {
                        // Show error state
                        binding.apply {
                            cartProgress.visibility = View.GONE
                            errorImage.visibility = View.VISIBLE
                            errorText.visibility = View.VISIBLE
                            errorText.text = state.error
                            Snackbar.make(binding.root, state.error, Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

        //listener for payment
        makePayment()

        Paystack.builder()
            .setPublicKey(BuildConfig.PUBLIC_KEY)
            .setLoggingEnabled(true)
            .build()
        paymentSheet = PaymentSheet(this, ::paymentComplete)
        binding.checkoutLayout.setOnClickListener {
            triggerValidation()
        }
        return binding.root
    }

    private fun triggerValidation() {
        lifecycleScope.launch {
            viewModel.totalPrice.collectLatest {
                if (it != 0.0) {
                    viewModel.paymentInit(it)
                }
            }
        }
    }

    private fun makePayment() {
        lifecycleScope.launch {
            viewModel.initPaymentState.collectLatest {
                when (it) {
                    is InitPaymentState.Loading -> {
                        binding.checkoutProgress.visibility = View.VISIBLE

                    }
                    is InitPaymentState.Success -> {
                        paymentSheet.launch(accessCode = it.accessCode)
                        binding.checkoutProgress.visibility = View.GONE
                    }
                    is InitPaymentState.Error -> {
                        Utils.showSnackbar(binding.root, "Payment Failed, please try again")
                        binding.checkoutProgress.visibility = View.GONE
                    }
                    else -> {}
                }
            }
        }
    }

    private fun paymentComplete(paymentSheetResult: PaymentSheetResult) {
        val message = when (paymentSheetResult) {
            PaymentSheetResult.Cancelled -> getString(R.string.cancelled)
            is PaymentSheetResult.Failed -> {
                paymentSheetResult.error.message ?: getString(R.string.failed)
            }

            is PaymentSheetResult.Completed -> {
                // Returns the transaction reference  PaymentCompletionDetails(reference={TransactionRef})
                viewModel.clearCartDB()
                findNavController().popBackStack(ProductList, false)
                getString(R.string.successful)
            }
        }

        Utils.showSnackbar(binding.root, message)
    }
}