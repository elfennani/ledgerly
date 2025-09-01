package com.elfennani.ledgerly.domain.model

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val failure: Failure) : Result<Nothing>()
}