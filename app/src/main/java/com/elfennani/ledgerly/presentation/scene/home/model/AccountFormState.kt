package com.elfennani.ledgerly.presentation.scene.home.model

data class AccountFormState(
    val name: String = "",
    val initialBalance: String = "",
    val description: String = "",

    // Validation
    val nameError: String? = null,
    val initialBalanceError: String? = null,
    val descriptionError: String? = null,

    val isSubmitting: Boolean = false,
    val isSuccess: Boolean = false,
)
