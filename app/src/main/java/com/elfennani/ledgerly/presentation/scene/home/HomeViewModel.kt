package com.elfennani.ledgerly.presentation.scene.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfennani.ledgerly.domain.usecase.CreateAccountUseCase
import com.elfennani.ledgerly.domain.usecase.CreateCategoryUseCase
import com.elfennani.ledgerly.domain.usecase.DeleteAccountUseCase
import com.elfennani.ledgerly.domain.usecase.GetHomeOverviewUseCase
import com.elfennani.ledgerly.domain.usecase.ToggleGroupCollapsedUseCase
import com.elfennani.ledgerly.domain.usecase.UpdateAccountUseCase
import com.elfennani.ledgerly.presentation.scene.home.model.AccountFormState
import com.elfennani.ledgerly.presentation.scene.home.model.CategoryFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getHomeOverview: GetHomeOverviewUseCase,
    private val createAccount: CreateAccountUseCase,
    private val deleteAccount: DeleteAccountUseCase,
    private val updateAccount: UpdateAccountUseCase,
    private val toggleGroupCollapsed: ToggleGroupCollapsedUseCase,
    private val createCategory: CreateCategoryUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(HomeUiState(isLoading = true))
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getHomeOverview().collect {
                Log.d("HomeViewModel", "Received home overview: $it")
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        accounts = it.accounts,
                        groups = it.groups
                    )
                }
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.DismissCreateAccountModal -> {
                _state.update {
                    it.copy(
                        isCreateAccountModalVisible = false,
                        accountFormState = AccountFormState()
                    )
                }
            }

            HomeEvent.ShowCreateAccountModal -> {
                _state.update { it.copy(isCreateAccountModalVisible = true) }
            }

            HomeEvent.SubmitCreateAccount -> {
                val isError = with(state.value.accountFormState) {
                    val nameError = isNameError(name)
                    val balanceError = isInitialBalanceError(initialBalance)

                    _state.update {
                        it.copy(
                            accountFormState = it.accountFormState.copy(
                                nameError = nameError,
                                initialBalanceError = balanceError
                            )
                        )
                    }

                    nameError != null || balanceError != null
                }

                if (!isError) {
                    viewModelScope.launch {
                        val current = state.value.copy().accountFormState
                        _state.update {
                            it.copy(
                                accountFormState = it.accountFormState.copy(
                                    isSubmitting = true
                                )
                            )
                        }
                        createAccount(
                            name = current.name,
                            initialBalance = current.initialBalance.toDouble(),
                            description = current.description.ifEmpty { null }
                        )
                        _state.update {
                            it.copy(
                                accountFormState = it.accountFormState.copy(
                                    isSubmitting = false,
                                    isSuccess = true
                                ),
                            )
                        }
                    }
                }
            }

            is HomeEvent.OnAccountDescriptionChange -> {
                _state.update { it.copy(accountFormState = it.accountFormState.copy(description = event.description)) }
            }

            is HomeEvent.OnAccountNameChange -> {
                _state.update {
                    val error = isNameError(event.name)

                    it.copy(
                        accountFormState = it.accountFormState.copy(
                            name = event.name,
                            nameError = error
                        )
                    )
                }
            }

            is HomeEvent.OnInitialBalanceChange -> {
                _state.update {
                    val error = isInitialBalanceError(event.balance)

                    it.copy(
                        accountFormState = it.accountFormState.copy(
                            initialBalance = event.balance,
                            initialBalanceError = error
                        )
                    )
                }
            }

            HomeEvent.DismissAccountDetailsModal -> {
                _state.update {
                    it.copy(
                        selectedAccount = null,
                    )
                }
            }

            is HomeEvent.ShowAccountDetailsModal -> {
                _state.update {
                    it.copy(
                        selectedAccount = event.accountId,
                    )
                }
            }

            is HomeEvent.ConfirmDeleteAccount -> {
                viewModelScope.launch {
                    deleteAccount(event.accountId)
                    _state.update {
                        it.copy(
                            toDeleteAccount = null,
                            selectedAccount = null
                        )
                    }
                }
            }

            HomeEvent.CancelDeleteAccount -> {
                _state.update {
                    it.copy(
                        toDeleteAccount = null
                    )
                }
            }

            is HomeEvent.DeleteAccount -> {
                _state.update {
                    it.copy(
                        toDeleteAccount = event.accountId
                    )
                }
            }

            HomeEvent.DismissEditAccountModal -> {
                _state.update {
                    it.copy(
                        editingAccount = null,
                        accountFormState = AccountFormState()
                    )
                }
            }

            is HomeEvent.ShowEditAccountModal -> {
                val account = state.value.accounts.find { it.id == event.accountId }
                if (account != null) {
                    _state.update {
                        it.copy(
                            editingAccount = event.accountId,
                            accountFormState = AccountFormState(
                                name = account.name,
                                initialBalance = account.balance.toString(),
                                description = account.description ?: ""
                            )
                        )
                    }
                }
            }

            HomeEvent.SubmitEditAccount -> {
                val isError = with(state.value.accountFormState) {
                    val nameError = isNameError(name)
                    val balanceError = isInitialBalanceError(initialBalance)

                    _state.update {
                        it.copy(
                            accountFormState = it.accountFormState.copy(
                                nameError = nameError,
                                initialBalanceError = balanceError
                            )
                        )
                    }

                    nameError != null || balanceError != null
                }

                if (!isError) {
                    val accountId = state.value.editingAccount
                    if (accountId != null) {
                        viewModelScope.launch {
                            val current = state.value.copy().accountFormState
                            _state.update {
                                it.copy(
                                    accountFormState = it.accountFormState.copy(
                                        isSubmitting = true
                                    )
                                )
                            }
                            updateAccount(
                                accountId = accountId,
                                name = current.name,
                                initialBalance = current.initialBalance.toDouble(),
                                description = current.description.ifEmpty { null }
                            )
                            _state.update {
                                it.copy(
                                    editingAccount = null,
                                    accountFormState = AccountFormState()
                                )
                            }
                        }
                    }
                }
            }

            is HomeEvent.ToggleGroupCollapsed -> {
                viewModelScope.launch {
                    toggleGroupCollapsed(event.groupId)
                }
            }

            HomeEvent.DismissCreateCategoryModal -> {
                _state.update {
                    it.copy(
                        createCategoryGroupId = null,
                        categoryFormState = CategoryFormState()
                    )
                }
            }

            is HomeEvent.OnCategoryNameChange -> {
                _state.update {
                    it.copy(
                        categoryFormState = it.categoryFormState.copy(name = event.name)
                    )
                }
            }

            is HomeEvent.OnCategoryTargetChange -> {
                _state.update {
                    it.copy(
                        categoryFormState = it.categoryFormState.copy(target = event.target)
                    )
                }
            }

            is HomeEvent.ShowCreateCategoryModal -> {
                _state.update {
                    it.copy(
                        createCategoryGroupId = event.groupId,
                        categoryFormState = CategoryFormState(groupId = event.groupId)
                    )
                }
            }

            HomeEvent.SubmitCreateCategory -> {
                val groupId = state.value.createCategoryGroupId
                if (groupId != null) {
                    viewModelScope.launch {
                        val current = state.value.categoryFormState

                        if (current.name.text.isBlank()) {
                            _state.update {
                                it.copy(
                                    categoryFormState = it.categoryFormState.copy(
                                        nameError = "Name cannot be empty"
                                    )
                                )
                            }
                            return@launch
                        }

                        if (current.target.text.toDoubleOrNull() == null) {
                            _state.update {
                                it.copy(
                                    categoryFormState = it.categoryFormState.copy(
                                        targetError = "Invalid target amount"
                                    )
                                )
                            }
                            return@launch
                        } else if (current.target.text.toDouble() < 0) {
                            _state.update {
                                it.copy(
                                    categoryFormState = it.categoryFormState.copy(
                                        targetError = "Target cannot be negative"
                                    )
                                )
                            }
                            return@launch
                        }

                        _state.update {
                            it.copy(
                                categoryFormState = it.categoryFormState.copy(
                                    isSubmitting = true
                                )
                            )
                        }
                        createCategory(
                            name = current.name.text,
                            target = current.target.text.toDouble(),
                            groupId = groupId
                        )
                        _state.update {
                            it.copy(
                                createCategoryGroupId = null,
                                categoryFormState = it.categoryFormState.copy(
                                    isSubmitting = false,
                                    isSuccess = true
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun isNameError(name: String): String? {
        return if (name.isEmpty()) {
            "Name cannot be empty"
        } else {
            null
        }
    }

    private fun isInitialBalanceError(balance: String): String? {
        return if (balance.isEmpty()) {
            "Balance cannot be empty"
        } else if (balance.toDoubleOrNull() == null) {
            "Invalid Balance"
        } else if (balance.toDouble() < 0) {
            "Balance cannot be negative"
        } else {
            null
        }
    }
}