package com.elfennani.ledgerly.domain.model

data class HomeOverview(
    val accounts: List<Account>,
    val groups: List<Group>,
    val budgetData: BudgetData = BudgetData()
)
