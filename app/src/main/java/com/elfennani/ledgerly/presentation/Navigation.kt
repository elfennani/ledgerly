package com.elfennani.ledgerly.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.elfennani.ledgerly.presentation.scenes.home.HomeRoute
import com.elfennani.ledgerly.presentation.scenes.home.HomeScreen
import com.elfennani.ledgerly.presentation.scenes.products.ProductListRoute
import com.elfennani.ledgerly.presentation.scenes.products.ProductListScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeRoute,
    ) {
        composable<HomeRoute> {
            HomeScreen(navController = navController)
        }
        composable<ProductListRoute> {
            ProductListScreen(navController = navController)
        }
    }
}