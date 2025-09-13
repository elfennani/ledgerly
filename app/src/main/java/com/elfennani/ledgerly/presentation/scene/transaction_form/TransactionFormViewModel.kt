package com.elfennani.ledgerly.presentation.scene.transaction_form

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfennani.ledgerly.domain.model.Transaction
import com.elfennani.ledgerly.domain.model.TransactionSplit
import com.elfennani.ledgerly.domain.usecase.CreateTransactionUseCase
import com.elfennani.ledgerly.domain.usecase.GetHomeOverviewUseCase
import com.elfennani.ledgerly.domain.usecase.GetProductsUseCase
import com.elfennani.ledgerly.presentation.scene.transaction_form.model.SplitItem
import com.elfennani.ledgerly.presentation.utils.pretty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class TransactionFormViewModel @Inject constructor(
    private val getProducts: GetProductsUseCase,
    private val getHomeOverviewUseCase: GetHomeOverviewUseCase,
    private val createTransactionUseCase: CreateTransactionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionFormUiState())
    val state = _state.asStateFlow()


    val now: Calendar = Calendar.getInstance()
    val month = now.get(Calendar.MONTH)
    val year = now.get(Calendar.YEAR)

    init {
        viewModelScope.launch {
            getProducts().first().let {
                _state.update { state -> state.copy(products = it) }
            }
            getHomeOverviewUseCase(monthIndex = month, year).first().let { overview ->
                _state.update { state ->
                    state.copy(
                        accounts = overview.accounts,
                        categories = overview.groups.flatMap { it.categories ?: emptyList() }
                    )
                }
            }

            _state.distinctUntilChanged { old, new ->
                val oldTotal = old.splits.fold(0.00) { acc, split ->
                    acc + split.total
                }
                val newTotal = new.splits.fold(0.00) { acc, split ->
                    acc + split.total
                }

                oldTotal == newTotal
            }.collect {
                _state.update {
                    it.copy(total = it.splits.fold(0.00) { acc, split ->
                        acc + split.total
                    })
                }
            }
        }
    }

    fun onEvent(event: TransactionFormEvent) {
        when (event) {
            is TransactionFormEvent.AddProduct -> _state.update { state ->
                if (state.splits.any { it.product.id == event.product.id }) {
                    return@update state.copy(
                        error = "Product \"${event.product.name}\" already added",
                    )
                }

                state.copy(
                    splits = state.splits + SplitItem(
                        id = if (state.splits.isEmpty()) 0 else state.splits.maxOf { it.id } + 1,
                        product = event.product,
                        isByUnit = event.product.pricePerUnit != null,
                        units = 1,
                        total = event.product.pricePerUnit ?: 0.00,
                        isNew = true,
                        totalText = TextFieldValue(
                            text = event.product.pricePerUnit?.pretty ?: "",
                            selection = TextRange(event.product.pricePerUnit?.pretty?.length ?: 0)
                        )
                    ),
                )
            }

            is TransactionFormEvent.ConfirmSplit -> _state.update { state ->
                val split = state.splits.find { it.id == event.splitId }!!

                if (split.totalText.text.toDoubleOrNull() == null) {
                    return@update state.copy(
                        error = "Invalid total value for \"${split.product.name}\"",
                    )
                }

                state.copy(
                    openSplitId = null,
                    splits = state.splits.map { split ->
                        if (split.id == event.splitId) {
                            split.copy(
                                total = split.totalText.text.toDouble()
                            )
                        } else
                            split
                    },
                )
            }

            is TransactionFormEvent.IncrementQuantity -> _state.update { state ->
                state.copy(
                    splits = state.splits.map { split ->
                        if (split.id == event.splitId) {
                            split.copy(
                                units = split.units + 1,
                                totalText = if (split.isByUnit && split.product.pricePerUnit != null) TextFieldValue(
                                    text = (split.product.pricePerUnit * (split.units + 1)).pretty,
                                    selection = TextRange(
                                        (split.product.pricePerUnit * (split.units + 1)).pretty.length
                                    )
                                ) else split.totalText
                            )
                        } else
                            split
                    },
                )
            }

            is TransactionFormEvent.DecrementQuantity -> _state.update { state ->
                state.copy(
                    splits = state.splits.map { split ->
                        if (split.id == event.splitId) {
                            split.copy(
                                units = (split.units - 1).coerceAtLeast(1)
                            )
                        } else
                            split
                    },
                )
            }

            is TransactionFormEvent.EditSplit -> _state.update { it.copy(openSplitId = event.splitId) }
            is TransactionFormEvent.RemoveSplit -> _state.update {
                it.copy(splits = it.splits.filterNot { split -> split.id == event.splitId })
            }

            is TransactionFormEvent.SetAccount -> _state.update { it.copy(account = event.account) }
            is TransactionFormEvent.SetCategory -> _state.update { it.copy(category = event.category) }
            is TransactionFormEvent.SetDate -> _state.update { it.copy(date = event.date) }
            is TransactionFormEvent.SetIsByUnit -> _state.update { state ->
                state.copy(
                    splits = state.splits.map { split ->
                        if (split.id == event.splitId) {
                            split.copy(isByUnit = event.isByUnit)
                        } else
                            split
                    },
                )
            }

            is TransactionFormEvent.SetQuantity -> _state.update { state ->
                state.copy(
                    splits = state.splits.map { split ->
                        if (split.id == event.splitId) {
                            split.copy(
                                units = event.quantity
                            )
                        } else
                            split
                    },
                )
            }

            is TransactionFormEvent.SetTitle -> _state.update { it.copy(title = event.title) }
            is TransactionFormEvent.SetTotal -> _state.update { state ->
                state.copy(
                    splits = state.splits.map { split ->
                        if (split.id == event.splitId) {
                            split.copy(
                                totalText = event.total
                            )
                        } else
                            split
                    },
                )
            }

            TransactionFormEvent.OpenAddProductModal -> _state.update {
                it.copy(
                    isAddProductModalOpen = true,
                )
            }

            TransactionFormEvent.OpenDateModal -> _state.update { it.copy(isDateModalOpen = true) }
            TransactionFormEvent.OpenSelectAccountModal -> _state.update {
                it.copy(
                    isSelectAccountModalOpen = true,
                )
            }

            TransactionFormEvent.OpenSelectCategoryModal -> _state.update {
                it.copy(
                    isSelectCategoryModalOpen = true,
                )
            }

            TransactionFormEvent.DismissAddProductModal -> _state.update {
                it.copy(
                    isAddProductModalOpen = false,
                )
            }

            TransactionFormEvent.DismissDateModal -> _state.update { it.copy(isDateModalOpen = false) }
            TransactionFormEvent.DismissSelectAccountModal -> _state.update {
                it.copy(
                    isSelectAccountModalOpen = false,
                )
            }

            TransactionFormEvent.DismissSelectCategoryModal -> _state.update {
                it.copy(
                    isSelectCategoryModalOpen = false,
                )
            }

            TransactionFormEvent.Save -> {
                val state = _state.value
                if (state.title.text.isBlank()) {
                    _state.update {
                        it.copy(
                            error = "Title cannot be empty",
                        )
                    }
                    return
                } else if (state.account == null) {
                    _state.update {
                        it.copy(
                            error = "Please select an account",
                        )
                    }
                    return
                } else if (state.category == null) {
                    _state.update {
                        it.copy(
                            error = "Please select a category",
                        )
                    }
                    return
                } else if (state.splits.isEmpty()) {
                    _state.update {
                        it.copy(
                            error = "Please add at least one product",
                        )
                    }
                    return
                } else if (state.openSplitId != null) {
                    _state.update {
                        it.copy(
                            error = "Please confirm all splits",
                        )
                    }
                    return
                } else if (state.date > Instant.now()) {
                    _state.update {
                        it.copy(
                            error = "Date cannot be in the future",
                        )
                    }
                    return
                } else if (state.total == 0.00) {
                    _state.update {
                        it.copy(
                            error = "Total cannot be zero",
                        )
                    }
                    return
                }

                viewModelScope.launch {
                    _state.update { it.copy(isSaving = true) }
                    createTransactionUseCase(
                        Transaction.Outflow(
                            amount = state.total,
                            date = state.date,
                            title = state.title.text,
                            description = null,
                            category = state.category,
                            splits = state.splits.map {
                                TransactionSplit(
                                    transactionId = 0,
                                    productId = it.product.id,
                                    units = it.units,
                                    totalPrice = it.total,
                                    product = it.product
                                )
                            },
                            account = state.account,
                        )
                    )
                }

                _state.update { it.copy(isSaving = false, isSuccess = true) }
            }

            TransactionFormEvent.ClearError -> _state.update { it.copy(error = null) }
        }
    }

}