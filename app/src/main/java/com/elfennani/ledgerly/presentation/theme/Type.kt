package com.elfennani.ledgerly.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.elfennani.ledgerly.R

val interFont = FontFamily(
    listOf(
        Font(R.font.inter, FontWeight.W400),
        Font(R.font.inter_medium, FontWeight.W500),
        Font(R.font.inter_semibold, FontWeight.W600),
        Font(R.font.inter_bold, FontWeight.W700),
    )
)

val interTightFont = FontFamily(
    listOf(
        Font(R.font.inter_tight, FontWeight.W400),
        Font(R.font.inter_tight_medium, FontWeight.W500),
        Font(R.font.inter_tight_semibold, FontWeight.W600),
        Font(R.font.inter_tight_bold, FontWeight.W700),
    )
)

val bodyFontFamily = interFont

val displayFontFamily = interTightFont

// Default Material 3 typography values
val baseline = Typography()

val AppTypography = Typography(
    displayLarge = baseline.displayLarge.copy(
        fontFamily = displayFontFamily,
        fontWeight = FontWeight.W600
    ),
    displayMedium = baseline.displayMedium.copy(
        fontFamily = displayFontFamily,
        fontWeight = FontWeight.W600
    ),
    displaySmall = baseline.displaySmall.copy(
        fontFamily = displayFontFamily,
        fontWeight = FontWeight.W600
    ),
    headlineLarge = baseline.headlineLarge.copy(
        fontFamily = displayFontFamily,
        fontWeight = FontWeight.W600
    ),
    headlineMedium = baseline.headlineMedium.copy(
        fontFamily = displayFontFamily,
        fontWeight = FontWeight.W600
    ),
    headlineSmall = baseline.headlineSmall.copy(
        fontFamily = displayFontFamily,
        fontWeight = FontWeight.W600
    ),
    titleLarge = baseline.titleLarge.copy(
        fontFamily = displayFontFamily,
        fontWeight = FontWeight.W600
    ),
    titleMedium = baseline.titleMedium.copy(
        fontFamily = displayFontFamily,
        fontWeight = FontWeight.W500
    ),
    titleSmall = baseline.titleSmall.copy(
        fontFamily = displayFontFamily,
        fontWeight = FontWeight.W500
    ),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily),
    labelLarge = baseline.labelLarge.copy(
        fontFamily = bodyFontFamily,
        fontWeight = FontWeight.W500
    ),
    labelMedium = baseline.labelMedium.copy(
        fontFamily = bodyFontFamily,
        fontWeight = FontWeight.W500
    ),
    labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily, fontWeight = FontWeight.W500)
)

