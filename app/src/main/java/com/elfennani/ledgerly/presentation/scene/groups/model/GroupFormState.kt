package com.elfennani.ledgerly.presentation.scene.groups.model

data class GroupFormState(
    val name: String = "",
    val nameError: String? = null,
    val isSubmitting: Boolean = false,
    val isSuccess: Boolean = false
)
