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
import com.example.myapplication.page.measurement.MeasurementType

data class UserResultUiState(
    val items: List<UserListItem>
)


class UserResultViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState: MutableStateFlow<UserResultUiState>
    val uiState: StateFlow<UserResultUiState>

    init {
        val result: UserListResult? = savedStateHandle.get(Screen.UserResult.KEY_RESULT)
        _uiState = MutableStateFlow(UserResultUiState(items = result?.items ?: emptyList()))
        uiState = _uiState.asStateFlow()
    }


    fun saveUserDataAndGetMeasurementType(user: UserListItem): MeasurementType? {
        SelectedUserStore.save(user)
        return SelectedMeasurementStore.get()
    }
}


