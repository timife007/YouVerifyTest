package com.timife.youverifytest.presentation.screens.auth

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.badge.BadgeDrawable
import com.timife.youverifytest.R
import com.timife.youverifytest.databinding.ActivityAuthBinding
import com.timife.youverifytest.databinding.ActivityMainBinding
import com.timife.youverifytest.navigation.authNavigationGraph
import com.timife.youverifytest.navigation.navigationGraph
import com.timife.youverifytest.presentation.viewmodels.CartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity :  AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_auth)
        navController.graph = authNavigationGraph(navController)
    }
}