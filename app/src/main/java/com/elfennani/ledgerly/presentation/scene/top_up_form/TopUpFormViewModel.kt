package com.elfennani.ledgerly.presentation.scene.top_up_form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfennani.ledgerly.domain.model.Transaction
import com.elfennani.ledgerly.domain.usecase.CreateTransactionUseCase
import com.elfennani.ledgerly.domain.usecase.GetHomeOverviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class TopUpFormViewModel @Inject constructor(
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val getHomeOverviewUseCase: GetHomeOverviewUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(TopUpFormUiState())
    val state = _state.asStateFlow()

    val now: Calendar = Calendar.getInstance()
    val month = now.get(Calendar.MONTH)
    val year = now.get(Calendar.YEAR)

    init {
        viewModelScope.launch {
            getHomeOverviewUseCase(monthIndex = month, year).collect { overview ->
                _state.update { it.copy(accounts = overview.accounts) }
            }
        }
    }

    fun onEvent(event: TopUpFormEvent) {
        when (event) {
            TopUpFormEvent.ClearError -> _state.update { it.copy(error = null) }
            TopUpFormEvent.OnCloseDateModal -> _state.update { it.copy(isDateModalOpen = false) }
            TopUpFormEvent.OnCloseSelectAccountModal -> _state.update {
                it.copy(
                    isSelectAccountModalOpen = false
                )
            }

            is TopUpFormEvent.OnDateChange -> _state.update { it.copy(date = event.date) }
            TopUpFormEvent.OnOpenDateModal -> _state.update { it.copy(isDateModalOpen = true) }
            TopUpFormEvent.OnOpenSelectAccountModal -> _state.update {
                it.copy(
                    isSelectAccountModalOpen = true
                )
            }

            is TopUpFormEvent.OnSelectAccount -> _state.update { it.copy(account = event.account) }
            is TopUpFormEvent.OnTitleChange -> _state.update { it.copy(title = event.title) }
            is TopUpFormEvent.OnTotalChange -> _state.update { it.copy(total = event.total) }
            TopUpFormEvent.OnSubmit -> {
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
                } else if (state.date > Instant.now()) {
                    _state.update {
                        it.copy(
                            error = "Date cannot be in the future",
                        )
                    }
                    return
                } else if (state.total.text.toDoubleOrNull() == null) {
                    _state.update {
                        it.copy(
                            error = "Total amount must be a valid number",
                        )
                    }
                    return
                } else if (state.total.text.toDouble() <= 0.00) {
                    _state.update {
                        it.copy(
                            error = "Total amount must be greater than 0",
                        )
                    }
                    return
                }

                viewModelScope.launch {
                    _state.update { it.copy(isSaving = true) }
                    createTransactionUseCase(
                        Transaction.Inflow(
                            amount = state.total.text.toDouble(),
                            date = state.date,
                            title = state.title.text,
                            description = null,
                            account = state.account
                        )
                    )
                }

                _state.update { it.copy(isSaving = false, isSuccess = true) }
            }
        }
    }

}