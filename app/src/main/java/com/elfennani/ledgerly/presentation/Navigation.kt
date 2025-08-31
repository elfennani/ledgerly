package com.elfennani.ledgerly.presentation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.elfennani.ledgerly.R
import com.elfennani.ledgerly.domain.model.Tab
import com.elfennani.ledgerly.presentation.component.AppScaffold
import com.elfennani.ledgerly.presentation.scenes.home.HomeRoute
import com.elfennani.ledgerly.presentation.scenes.home.HomeScreen
import com.elfennani.ledgerly.presentation.scenes.products.ProductListRoute
import com.elfennani.ledgerly.presentation.scenes.products.ProductListScreen
import com.elfennani.ledgerly.presentation.scenes.transactions.TransactionListRoute
import com.elfennani.ledgerly.presentation.scenes.transactions.TransactionListScreen

private const val TAG = "Navigation"

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentDestination = navBackStackEntry?.destination

    AppScaffold(
        selected = { tab ->
            currentDestination?.hierarchy?.any {
                it.hasRoute(tab.route::class)
            } == true
        },
        tabs = listOf(
            Tab(
                title = "Home",
                icon = R.drawable.home_outline,
                selectedIcon = R.drawable.home_filled,
                route = HomeRoute
            ),
            Tab(
                title = "Transactions",
                icon = R.drawable.banknotes_outline,
                selectedIcon = R.drawable.banknotes_filled,
                route = TransactionListRoute
            ),
            Tab(
                title = "Products",
                icon = R.drawable.rectangle_stack_outline,
                selectedIcon = R.drawable.rectangle_stack_filled,
                route = ProductListRoute
            )
        ),
        onTabSelected = { tab ->
            navController.navigate(tab.route) {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    ) {
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
            composable<TransactionListRoute> {
                TransactionListScreen(navController = navController)
            }
        }
    }
}