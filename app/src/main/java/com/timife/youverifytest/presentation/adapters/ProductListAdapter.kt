package com.timife.youverifytest.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.timife.youverifytest.R
import com.timife.youverifytest.databinding.ProductListItemBinding
import com.timife.youverifytest.domain.model.Product
import com.timife.youverifytest.presentation.utils.Utils

class ProductListAdapter(private val onItemClickListener:(id:Int) -> Unit) : ListAdapter<Product, ProductListAdapter.ProductViewHolder>(ProductDiffUtil()) {

    inner class ProductViewHolder(private val binding: ProductListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                productName.text = product.title
                productPrice.text = binding.root.context.getString(R.string.price_label, product.price)

                // Load the image using loadImage function from Utils
                Utils.loadImage(
                    binding.productImage.context,
                    binding.productImage,
                    product.image, // Image URL from the product
                    binding.imageProgressBar // Pass the ProgressBar for visibility
                )

                binding.root.setOnClickListener {
                    onItemClickListener(product.id)
                }
            }
        }
    }

    class ProductDiffUtil : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductListItemBinding.inflate(LayoutInflater.from(parent.context))
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}