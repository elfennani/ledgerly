package com.elfennani.ledgerly.presentation.component

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.elfennani.ledgerly.R
import com.elfennani.ledgerly.domain.model.Tab
import com.elfennani.ledgerly.presentation.scenes.home.HomeRoute
import com.elfennani.ledgerly.presentation.scenes.products.ProductListRoute
import com.elfennani.ledgerly.presentation.theme.AppTheme

@Composable
fun AppScaffold(
    selected: (Tab) -> Boolean = { false },
    tabs: List<Tab>,
    onTabSelected: (Tab) -> Unit,
    content: @Composable (innerPadding: PaddingValues) -> Unit,
) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEach { tab ->
                    val isSelected = selected(tab)

                    NavigationBarItem(
                        icon = {
                            Crossfade(
                                targetState = isSelected,
                                label = "Icon transition for ${tab.title}",
                                animationSpec = tween(durationMillis = 250)
                            ) { selected ->
                                Icon(
                                    painter = painterResource(id = if (selected) tab.selectedIcon else tab.icon),
                                    contentDescription = tab.title
                                )
                            }
                        },
                        label = { Text(tab.title) },
                        selected = isSelected,
                        onClick = {
                            if (!isSelected) {
                                onTabSelected(tab)
                            }
                        }
                    )
                }
            }
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.only(WindowInsetsSides.Bottom)
    ) {
        content(it)
    }
}

@Preview
@Composable
private fun AppScaffoldPreview() {
    AppTheme {
        AppScaffold(
            selected = {
                it.route == HomeRoute
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
                    route = "transactions"
                ),
                Tab(
                    title = "Products",
                    icon = R.drawable.rectangle_stack_outline,
                    selectedIcon = R.drawable.rectangle_stack_filled,
                    route = ProductListRoute
                ),

                ),
            onTabSelected = {}
        ) {
            Text(
                "Content goes here",
                modifier = Modifier.padding(it)
            )
        }
    }
}