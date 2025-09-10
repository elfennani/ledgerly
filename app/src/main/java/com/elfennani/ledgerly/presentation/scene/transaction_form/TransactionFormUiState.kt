package com.elfennani.ledgerly.presentation.scene.transaction_form


import androidx.compose.ui.text.input.TextFieldValue
import com.elfennani.ledgerly.domain.model.Account
import com.elfennani.ledgerly.domain.model.Category
import com.elfennani.ledgerly.domain.model.Product
import com.elfennani.ledgerly.presentation.scene.transaction_form.model.SplitItem
import com.elfennani.ledgerly.presentation.scene.transaction_form.model.TransactionFormTab
import java.time.Instant

data class TransactionFormUiState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val categories: List<Category> = emptyList(),
    val accounts: List<Account> = emptyList(),
    val splits: List<SplitItem> = emptyList(),
    val total: Double = 0.00,
    val title: TextFieldValue = TextFieldValue(""),
    val category: Category? = null,
    val date: Instant = Instant.now(),
    val account: Account? = null,
    val tab: TransactionFormTab = TransactionFormTab.SPLITS,
    val openSplitId: Int? = null,
    val error: String? = null,
    val isSaving: Boolean = false,
    val isSuccess: Boolean = false,

    // modals
    val isAddProductModalOpen: Boolean = false,
    val isSelectAccountModalOpen: Boolean = false,
    val isSelectCategoryModalOpen: Boolean = false,
    val isDateModalOpen: Boolean = false,
)
