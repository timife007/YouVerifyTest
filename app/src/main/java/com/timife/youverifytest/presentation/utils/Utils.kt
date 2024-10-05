package com.timife.youverifytest.presentation.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.navigation.NavOptions
import androidx.navigation.Navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.load.engine.GlideException
import com.google.android.material.chip.Chip
import com.google.android.material.shape.ShapeAppearanceModel
import com.timife.youverifytest.R

object Utils {
    fun loadImage(
        context: Context,
        imageView: ImageView,
        imageUrl: String,
        progressBar: View // ProgressBar to show/hide during loading
    ) {
        // Define a request listener to manage loading states
        val listener = object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>,
                isFirstResource: Boolean
            ): Boolean {
                // Hide ProgressBar and show the ImageView on load failure
                progressBar.visibility = View.GONE
                imageView.visibility = View.VISIBLE
                return false // Allow error placeholder to be set
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: com.bumptech.glide.load.DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                // Hide ProgressBar and show the ImageView when loading is complete
                progressBar.visibility = View.GONE
                imageView.visibility = View.VISIBLE
                return false // Allow Glide to handle the resource
            }
        }

        // Load the image using Glide
        Glide.with(context)
            .load(imageUrl) // Load image from URL
            .error(R.drawable.image_error) // Optional error image
            .listener(listener) // Set the listener
            .into(imageView) // Set the image in the ImageView
    }

    fun createCategoryChip(context: Context, category: String, onClick:() -> Unit): Chip {
        return Chip(context).apply {
            text = category
            isCloseIconVisible = false
            setChipBackgroundColorResource(R.color.off_white)
            shapeAppearanceModel = ShapeAppearanceModel().withCornerSize(
                context.resources.getDimension(R.dimen.chip_corner_radius)
            )
            setChipStrokeWidthResource(R.dimen.stroke_width)
            setTextColor(context.resources.getColor(R.color.black, null))
            setOnClickListener {
                onClick()
            }
        }
    }

    val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left)
        .setPopExitAnim(R.anim.slide_out_right)
        .build()

}