package com.timife.youverifytest.presentation

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils.attachBadgeDrawable
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.timife.youverifytest.R
import com.timife.youverifytest.databinding.ActivityMainBinding
import com.timife.youverifytest.navigation.Cart
import com.timife.youverifytest.navigation.navigationGraph
import com.timife.youverifytest.presentation.screens.auth.AuthActivity
import com.timife.youverifytest.presentation.states.CartUiState
import com.timife.youverifytest.presentation.utils.Utils
import com.timife.youverifytest.presentation.viewmodels.cart.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences


    private val viewModel: CartViewModel by viewModels()
    private lateinit var badgeDrawable: BadgeDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)

        // Check if the user is already logged in
        val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)

        if (isLoggedIn) {
            // If the user is already logged in, go directly to MainActivity
            setContentView(binding.root)
        } else {
            // If not logged in, navigate to LoginActivity
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish() // Close MainActivity
        }
        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        navController.graph = navigationGraph(navController)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    @OptIn(ExperimentalBadgeUtils::class)
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)

        // Get the menu item for the cart
        val cartItem = menu.findItem(R.id.action_cart)

        lifecycleScope.launch {
            viewModel.cartUiState.collectLatest{
                when(it){
                    is CartUiState.Loading -> {
                        // Show loading state
                    }
                    is CartUiState.Success -> {
                        // Create a BadgeDrawable
                        val cartItemCount = it.data.count()
                        badgeDrawable = BadgeDrawable.create(this@MainActivity).apply {
                            number = cartItemCount
                            isVisible = cartItemCount > 0
                        }
                        // Attach the badge to the cart icon view
                        val toolbar = binding.toolbar
                        val cartIconView = toolbar.findViewById<View>(R.id.action_cart)
                        cartItem?.let {
                            attachBadgeDrawable(badgeDrawable, cartIconView)
                        }
                    }
                    is CartUiState.Error -> {
                        // Show error state
                    }
                }
            }
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_cart -> {
                findNavController(R.id.nav_host_fragment_content_main).navigate(Cart, Utils.navOptions)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}