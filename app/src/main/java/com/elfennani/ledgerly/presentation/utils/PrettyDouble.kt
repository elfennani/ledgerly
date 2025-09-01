package com.elfennani.ledgerly.presentation.utils

val Double.pretty: String
    get() = toBigDecimal()
        .stripTrailingZeros()
        .toPlainString()