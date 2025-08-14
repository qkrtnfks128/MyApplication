package com.example.myapplication.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.manager.SelectedOrgStore
import com.example.myapplication.model.UserListResult
import com.example.myapplication.repository.WonderfulRepository
import com.example.myapplication.repository.WonderfulRepositoryFactory
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PhoneAuthUiState(
    val digits: List<String> = listOf("", "", "", ""),
    val activeIndex: Int = 0,
    val isLoading: Boolean = false
)

sealed class PhoneAuthEvent {
    data class Success(val result: UserListResult) : PhoneAuthEvent()
    data class Error(val message: String) : PhoneAuthEvent()
}

class PhoneAuthViewModel : ViewModel() {
    private val repository: WonderfulRepository = WonderfulRepositoryFactory.create()

    private val _uiState: MutableStateFlow<PhoneAuthUiState> = MutableStateFlow(PhoneAuthUiState())
    val uiState: StateFlow<PhoneAuthUiState> = _uiState.asStateFlow()

    private val _events: MutableSharedFlow<PhoneAuthEvent> = MutableSharedFlow()
    val events: SharedFlow<PhoneAuthEvent> = _events.asSharedFlow()

    fun appendDigit(digit: String) {
        val s = _uiState.value
        val idx = s.activeIndex
        if (idx in 0..3 && s.digits[idx].isEmpty()) {
            val list = s.digits.toMutableList()
            list[idx] = digit
            val next = list.indexOfFirst { it.isEmpty() }.let { if (it == -1) 3 else it }
            _uiState.value = s.copy(digits = list, activeIndex = next)
        }
    }

    fun deleteDigit() {
        val s = _uiState.value
        val idx = if (s.digits[s.activeIndex].isNotEmpty()) s.activeIndex else s.digits.indexOfLast { it.isEmpty() }
        if (idx >= 0) {
            val list = s.digits.toMutableList()
            list[idx] = ""
            _uiState.value = s.copy(digits = list, activeIndex = idx)
        }
    }

    fun submit(customerCode: String,) {
        val s = _uiState.value
        val number = s.digits.joinToString("")

          val centerUuid: String = SelectedOrgStore.getSelected()?.orgUuid ?: ""
        viewModelScope.launch {
            if (number.length != 4) {
                _events.emit(PhoneAuthEvent.Error("4자리를 입력해주세요."))
                return@launch
            }
            _uiState.value = s.copy(isLoading = true)
            val result: UserListResult = repository.getUserListUsingPhoneNumber(
                customerCode = customerCode,
                centerUuid = centerUuid,
                number = number
            )
            _events.emit(PhoneAuthEvent.Success(result))
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }
}
