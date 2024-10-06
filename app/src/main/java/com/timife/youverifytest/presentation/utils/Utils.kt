package com.timife.youverifytest.presentation.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.navigation.NavOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.timife.youverifytest.R

object Utils {
    fun loadImage(
        context: Context,
        imageView: ImageView,
        imageUrl: String,
        progressBar: View
    ) {
        // request listener to manage loading states
        val listener = object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>,
                isFirstResource: Boolean
            ): Boolean {
                progressBar.visibility = View.GONE
                imageView.visibility = View.VISIBLE
                return false
            }

            override fun onResourceReady(
                resource: Drawable,
                model: Any,
                target: Target<Drawable>?,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                // visibility management when loading is complete
                progressBar.visibility = View.GONE
                imageView.visibility = View.VISIBLE
                return false
            }
        }

        Glide.with(context)
            .load(imageUrl)
            .error(R.drawable.image_error)
            .listener(listener)
            .into(imageView)
    }

    val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left)
        .setPopExitAnim(R.anim.slide_out_right)
        .build()

    fun showSnackbar(
        view: View,
        message: String,
        duration: Int = Snackbar.LENGTH_LONG,
        actionText: String? = null,
        action: (() -> Unit)? = null
    ) {
        val snackbar = Snackbar.make(view, message, duration)

        if (actionText != null && action != null) {
            snackbar.setAction(actionText) {
                action()
            }
        }
        snackbar.show()
    }


}