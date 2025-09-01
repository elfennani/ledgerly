package com.elfennani.ledgerly.domain.model

data class Group(
    val id: Int,
    val name: String,
    val collapsed: Boolean = false,
    val index: Int = 0
)