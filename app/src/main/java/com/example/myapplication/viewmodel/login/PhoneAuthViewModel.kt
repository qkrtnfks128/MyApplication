package com.example.myapplication.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.manager.SelectedOrgStore
import com.example.myapplication.model.UserListItem
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
    var isLoading: Boolean = false
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

    fun submit() {
        val number = _uiState.value.digits.joinToString("")
        _uiState.value.isLoading = true
          val centerUuid: String = SelectedOrgStore.getSelected()?.orgUuid ?: ""
        viewModelScope.launch {
            if (number.length != 4) {
                _events.emit(PhoneAuthEvent.Error("4자리를 입력해주세요."))
                return@launch
            }

            val result: UserListResult = repository.getUserListUsingPhoneNumber(
                centerUuid = centerUuid,
                number = number
            )

            // 유저 2명인 경우의 목데이터 생성
            val mockResult = UserListResult(
                statusCode = 1,
                items = listOf(
                    UserListItem(
                        name = "홍길동",
                        cvid = "CVID-001",
                        imageUrl = "",
                        parentTel = "01012345678",
                        registrationDate = "1954년 01월 18일"
                    ),
                    UserListItem(
                        name = "김철수",
                        cvid = "CVID-002",
                        imageUrl = "",
                        parentTel = "01087654321",
                        registrationDate = "1960년 03월 01일"
                    )
                )
            )
            _events.emit(PhoneAuthEvent.Success(result))

        }
        _uiState.value.isLoading = false
    }
}
