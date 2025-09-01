package com.elfennani.ledgerly.domain.model

sealed class Failure(val message: String) {
    class NetworkError(message: String = "Please check your internet connection.") :
        Failure(message)

    class ServerError(message: String = "Server error occurred. Please try again later.") :
        Failure(message)

    class NotFoundError(message: String = "Requested resource not found.") : Failure(message)
    class UnknownError(message: String = "An unknown error occurred.") : Failure(message)
}