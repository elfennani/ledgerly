package com.elfennani.ledgerly.presentation.utils

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.clickableTextField(key: Any?, onClick: () -> Unit) = this.pointerInput(key) {
    awaitEachGesture {
        awaitFirstDown(pass = PointerEventPass.Initial)
        val upEvent =
            waitForUpOrCancellation(pass = PointerEventPass.Initial)
        if (upEvent != null) {
            onClick()
        }
    }
}