package com.example.myapplication.viewmodel.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.myapplication.manager.SelectedUserStore
import com.example.myapplication.manager.SelectedMeasurementStore
import com.example.myapplication.model.UserListItem
import com.example.myapplication.model.UserListResult
import com.example.myapplication.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.navigation.NavController
import com.example.myapplication.model.MeasurementType

data class UserResultUiState(
    val items: List<UserListItem>
)


class UserResultViewModel (
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState: MutableStateFlow<UserResultUiState>
    val uiState: StateFlow<UserResultUiState>

    // 사용자 정보 저장 및 페이지 이동을 위한 측정 유형 반환
    fun saveUserDataAndGetMeasurementType(user: UserListItem): MeasurementType? {
        SelectedUserStore.save(user)
        return SelectedMeasurementStore.get()
    }

    init {

        val result: UserListResult? = savedStateHandle.get(Screen.UserResult.KEY_RESULT)
        _uiState = MutableStateFlow(UserResultUiState(items = result?.items ?: emptyList()))
        uiState = _uiState.asStateFlow()
    }

}


