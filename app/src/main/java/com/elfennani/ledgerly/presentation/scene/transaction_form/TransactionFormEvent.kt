package com.elfennani.ledgerly.presentation.scene.transaction_form

import androidx.compose.ui.text.input.TextFieldValue
import com.elfennani.ledgerly.domain.model.Account
import com.elfennani.ledgerly.domain.model.Category
import com.elfennani.ledgerly.domain.model.Product
import java.time.Instant

sealed class TransactionFormEvent {
    // split events
    data class AddProduct(val product: Product) : TransactionFormEvent()
    data class SetQuantity(val splitId: Int, val quantity: Int) : TransactionFormEvent()
    data class IncrementQuantity(val splitId: Int) : TransactionFormEvent()
    data class DecrementQuantity(val splitId: Int) : TransactionFormEvent()
    data class SetTotal(val splitId: Int, val total: TextFieldValue) : TransactionFormEvent()
    data class SetIsByUnit(val splitId: Int, val isByUnit: Boolean) : TransactionFormEvent()
    data class ConfirmSplit(val splitId: Int) : TransactionFormEvent()
    data class RemoveSplit(val splitId: Int) : TransactionFormEvent()
    data class EditSplit(val splitId: Int) : TransactionFormEvent()

    // form events
    data class SetTitle(val title: TextFieldValue) : TransactionFormEvent()
    data class SetDate(val date: Instant) : TransactionFormEvent()
    data class SetAccount(val account: Account) : TransactionFormEvent()
    data class SetCategory(val category: Category) : TransactionFormEvent()
    data object Save : TransactionFormEvent()

    // modal events
    data object OpenAddProductModal : TransactionFormEvent()
    data object DismissAddProductModal : TransactionFormEvent()
    data object OpenDateModal : TransactionFormEvent()
    data object DismissDateModal : TransactionFormEvent()
    data object OpenSelectAccountModal : TransactionFormEvent()
    data object DismissSelectAccountModal : TransactionFormEvent()
    data object OpenSelectCategoryModal : TransactionFormEvent()
    data object DismissSelectCategoryModal : TransactionFormEvent()
    data object ClearError : TransactionFormEvent()
}