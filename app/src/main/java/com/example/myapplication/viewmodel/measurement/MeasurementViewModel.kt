package com.example.myapplication.viewmodel.measurement

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class MeasurementStage { Idle, Waiting, Measuring, Completed }

data class MeasurementUiState(
    val stage: MeasurementStage = MeasurementStage.Idle,
    val message: String = ""
)

class MeasurementViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<MeasurementUiState> = MutableStateFlow(MeasurementUiState())
    val uiState: StateFlow<MeasurementUiState> = _uiState.asStateFlow()

    fun setWaiting() { _uiState.value = MeasurementUiState(stage = MeasurementStage.Waiting, message = "측정대기중") }
    fun setMeasuring() { _uiState.value = MeasurementUiState(stage = MeasurementStage.Measuring, message = "측정중...") }
    fun setCompleted() { _uiState.value = MeasurementUiState(stage = MeasurementStage.Completed, message = "완료") }
}


