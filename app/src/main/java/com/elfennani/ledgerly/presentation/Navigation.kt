package com.elfennani.ledgerly.presentation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.elfennani.ledgerly.R
import com.elfennani.ledgerly.domain.model.Tab
import com.elfennani.ledgerly.presentation.component.AppScaffold
import com.elfennani.ledgerly.presentation.scene.category.CategoryRoute
import com.elfennani.ledgerly.presentation.scene.category.CategoryScreen
import com.elfennani.ledgerly.presentation.scene.groups.GroupsRoute
import com.elfennani.ledgerly.presentation.scene.groups.GroupsScreen
import com.elfennani.ledgerly.presentation.scene.home.HomeRoute
import com.elfennani.ledgerly.presentation.scene.home.HomeScreen
import com.elfennani.ledgerly.presentation.scene.product_form.ProductFormRoute
import com.elfennani.ledgerly.presentation.scene.product_form.ProductFormScreen
import com.elfennani.ledgerly.presentation.scene.products.ProductListRoute
import com.elfennani.ledgerly.presentation.scene.products.ProductListScreen
import com.elfennani.ledgerly.presentation.scene.transactions.TransactionListRoute
import com.elfennani.ledgerly.presentation.scene.transactions.TransactionListScreen
import kotlin.math.roundToInt

private const val TAG = "Navigation"
const val ANIM_DURATION_MILLIS = 150

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentDestination = navBackStackEntry?.destination
    val spec = remember { tween<Float>(ANIM_DURATION_MILLIS, easing = FastOutSlowInEasing) }
    val specInt = remember { tween<IntOffset>(ANIM_DURATION_MILLIS, easing = FastOutSlowInEasing) }

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
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            modifier = Modifier
                .consumeWindowInsets(innerPadding)
                .padding(innerPadding),
            enterTransition = {
                fadeIn() + slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(ANIM_DURATION_MILLIS),
                ) + scaleIn(initialScale = 0.9f)
            },
            exitTransition = {
                fadeOut(animationSpec = tween(ANIM_DURATION_MILLIS)) + scaleOut(
                    targetScale = 0.75f
                )
            },
            popEnterTransition = {
                fadeIn(animationSpec = spec) + scaleIn(
                    initialScale = 0.9f, animationSpec = spec
                ) + slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    initialOffset = { (it * 0.15f).roundToInt() },
                    animationSpec = specInt
                )
            },
            popExitTransition = {
                fadeOut(animationSpec = spec) + slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    targetOffset = { (it * 0.15f).roundToInt() },
                    animationSpec = specInt
                ) + scaleOut(
                    targetScale = 0.85f, animationSpec = spec
                )
            },
        ) {
            composable<HomeRoute>(
                enterTransition = { EnterTransition.None },
                popEnterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                popExitTransition = { ExitTransition.None }
            ) {
                HomeScreen(navController = navController)
            }
            composable<ProductListRoute>(
                enterTransition = { EnterTransition.None },
                popEnterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                popExitTransition = { ExitTransition.None }
            ) {
                ProductListScreen(navController = navController)
            }
            composable<TransactionListRoute>(
                enterTransition = { EnterTransition.None },
                popEnterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                popExitTransition = { ExitTransition.None }
            ) {
                TransactionListScreen(navController = navController)
            }
            composable<GroupsRoute> {
                GroupsScreen(
                    navController = navController
                )
            }
            composable<CategoryRoute> {
                CategoryScreen(
                    navController = navController
                )
            }

            composable<ProductFormRoute> {
                ProductFormScreen(
                    navController = navController
                )
            }
        }
    }
}