package com.timife.youverifytest.presentation.screens.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.timife.youverifytest.R
import com.timife.youverifytest.databinding.ActivityAuthBinding
import com.timife.youverifytest.navigation.authNavigationGraph
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