package com.elfennani.ledgerly.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes

data class Tab(
    val title: String,
    @DrawableRes val icon: Int,
    @DrawableRes val selectedIcon: Int,
    val route: Any,
)
