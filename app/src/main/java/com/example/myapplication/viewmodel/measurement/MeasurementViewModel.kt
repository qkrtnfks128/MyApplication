package com.example.myapplication.viewmodel.measurement

import BloodSugarData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.BloodPressureData
import com.example.myapplication.model.WeightData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class MeasurementStage {  Waiting, Measuring, Completed, Error }


class MeasurementViewModel : ViewModel() {
    private val _stage = MutableStateFlow(MeasurementStage.Waiting)
    val stage: StateFlow<MeasurementStage> = _stage.asStateFlow()
    val bloodSugarData: BloodSugarData? = null
    val bloodPressureData: BloodPressureData? = null
    val weightData: WeightData? = null

    // 측정시작
    fun triggerStartMeasuring() {
    viewModelScope.launch {
        delay(3000)
        _stage.value = MeasurementStage.Measuring
        delay(3000)
        _stage.value = MeasurementStage.Completed
    }

    }

    //재시도
    fun triggerRetry() { _stage.value = MeasurementStage.Waiting }

    init {
        // ViewModel 생성 시 자동으로 측정 시작 트리거 실행
        triggerStartMeasuring()
    }
}


