package com.example.myapplication.viewmodel.measurement

import BloodSugarStatus
import DeviceData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.model.BloodPressureData
import com.example.myapplication.model.WeightData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.BloodSugarData
import com.example.myapplication.model.MealType
import com.example.myapplication.model.MeasurementType
import com.example.myapplication.repository.YcSmartRepositoryFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class MeasurementStage {  Waiting, Measuring, Completed, Error }

class MeasurementViewModelFactory(
    private val measurementType: MeasurementType
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MeasurementViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MeasurementViewModel(
                measurementType
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MeasurementViewModel(
    private val measurementType: MeasurementType
) : ViewModel() {
    private val _stage = MutableStateFlow(MeasurementStage.Waiting)
    val stage: StateFlow<MeasurementStage> = _stage.asStateFlow()
    var bloodSugarData: MutableStateFlow<BloodSugarData>? = null
    val bloodPressure: BloodPressureData? = null
    val weightData: WeightData? = null
    val deviceData: DeviceData? = null

    private val repository = YcSmartRepositoryFactory.create()

    // 측정시작
    fun triggerStartMeasuring() {
    viewModelScope.launch {
        delay(3000)
        _stage.value = MeasurementStage.Measuring
        delay(3000)
        _stage.value = MeasurementStage.Completed

        when (measurementType) {
            MeasurementType.BloodSugar -> {
                bloodSugarData = MutableStateFlow(BloodSugarData(
                    date = "20240806",
                    time = "091100",
                    glucoseResult = 235,
                    temperature = 36.8f,
                    mealFlag = MealType.AFTER_MEAL,
                    judgment = BloodSugarStatus.HIGH as BloodSugarStatus?
                ))

                // repository.postBloodSugarData(bloodSugarData!!, deviceData!!)
            }

            MeasurementType.BloodPressure -> TODO()
            MeasurementType.Weight -> TODO()
        }
    }

    }

    //재시도
    fun triggerRetry() { _stage.value = MeasurementStage.Waiting }

    init {
        // ViewModel 생성 시 자동으로 측정 시작 트리거 실행
        triggerStartMeasuring()
    }
}


