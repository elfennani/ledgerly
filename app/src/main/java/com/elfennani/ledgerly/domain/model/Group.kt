package com.elfennani.ledgerly.domain.model

data class Group(
    val id: Int,
    val name: String,
    val collapsed: Boolean = false,
    val categories: List<Category>? = null,
    val index: Int = 0,
)