package com.timife.youverifytest.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.timife.youverifytest.R
import com.timife.youverifytest.databinding.CartListItemBinding
import com.timife.youverifytest.domain.model.CartedProduct

class CartListAdapter : ListAdapter<CartedProduct, CartListAdapter.CartViewHolder>(CartDiffUtil()) {
    inner class CartViewHolder(private val binding: CartListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartedProduct) {
            binding.quantityText.text = item.title
        }
    }

    class CartDiffUtil : DiffUtil.ItemCallback<CartedProduct>() {
        override fun areItemsTheSame(oldItem: CartedProduct, newItem: CartedProduct): Boolean {
            // Compare unique identifiers
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: CartedProduct, newItem: CartedProduct): Boolean {
            // Compare item contents
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}