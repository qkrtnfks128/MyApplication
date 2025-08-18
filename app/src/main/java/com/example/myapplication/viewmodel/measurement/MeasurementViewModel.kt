package com.example.myapplication.viewmodel.measurement

import BloodPressureStatus
import BloodSugarStatus
import DeviceData
import WeightStatus
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

enum class MeasurementStage {  Waiting, Measuring, Completed, Error, ShowingResult }

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

sealed class MeasurementEvent {
    data class BloodSugarSuccess(val jsonData: BloodSugarData) : MeasurementEvent()
    data class BloodPressureSuccess(val jsonData: BloodPressureData) : MeasurementEvent()
    data class WeightSuccess(val jsonData: WeightData) : MeasurementEvent()
    data class Error(val message: String) : MeasurementEvent()
}

class MeasurementViewModel(
    private val measurementType: MeasurementType
) : ViewModel() {

    // 미리보기용 상태 설정 함수
    fun setStageForPreview(stage: MeasurementStage) {
        _stage.value = stage

        // 결과 화면일 경우 테스트 데이터 생성
        if (stage == MeasurementStage.ShowingResult) {
            when (measurementType) {
                MeasurementType.BloodSugar -> {
                    _bloodSugarData.value = BloodSugarData(
                        date = "20240806",
                        time = "091100",
                        glucoseResult = 235,
                        temperature = 36.8f,
                        mealFlag = MealType.AFTER_MEAL,
                        judgment = BloodSugarStatus.HIGH as BloodSugarStatus?
                    )
                }
                MeasurementType.BloodPressure -> {
                    _bloodPressureData.value = BloodPressureData(
                        date = "20240806",
                        time = "091100",
                        systolic = 120,
                        diastolic = 80,
                        pulse = 70,
                        judgment = BloodPressureStatus.NORMAL as BloodPressureStatus?
                    )
                }
                MeasurementType.Weight -> {
                    _weightData.value = WeightData(
                        date = "20240806",
                        time = "091100",
                        scale = 60.0,
                        bmi = 20.0,
                        fatRate = 10.0,
                        muscleRate = 30.0,
                        judgment = WeightStatus.NORMAL as WeightStatus?
                    )
                }
            }
        }
    }
    private val _stage = MutableStateFlow(MeasurementStage.Waiting)
    val stage: StateFlow<MeasurementStage> = _stage.asStateFlow()

    private val _events: MutableSharedFlow<MeasurementEvent> = MutableSharedFlow()
    val events: SharedFlow<MeasurementEvent> = _events.asSharedFlow()

    private val _bloodSugarData = MutableStateFlow<BloodSugarData?>(null)
    val bloodSugarData: StateFlow<BloodSugarData?> = _bloodSugarData.asStateFlow()

    private val _bloodPressureData = MutableStateFlow<BloodPressureData?>(null)
    val bloodPressureData: StateFlow<BloodPressureData?> = _bloodPressureData.asStateFlow()

    private val _weightData = MutableStateFlow<WeightData?>(null)
    val weightData: StateFlow<WeightData?> = _weightData.asStateFlow()
    val deviceData: DeviceData? = null

    private val repository = YcSmartRepositoryFactory.create()

    // 측정시작
    fun triggerStartMeasuring() {
    viewModelScope.launch {
        delay(3000)
        _stage.value = MeasurementStage.Measuring
        delay(3000)
        _stage.value = MeasurementStage.Completed
        delay(3000) // (음성 알림 후) 결과 화면으로 전환
        _stage.value = MeasurementStage.ShowingResult

        when (measurementType) {
            // 서버에 보낸 뒤 화면 데이터 파싱
            MeasurementType.BloodSugar -> {
                _bloodSugarData.value = BloodSugarData(
                    date = "20240806",
                    time = "091100",
                    glucoseResult = 235,
                    temperature = 36.8f,
                    mealFlag = MealType.AFTER_MEAL,
                    judgment = BloodSugarStatus.HIGH as BloodSugarStatus?
                )

                // repository.postBloodSugarData(bloodSugarData!!, deviceData!!)
                // delay(3000)
                // sendBloodSugarData()
            }

            MeasurementType.BloodPressure -> {
                _bloodPressureData.value = BloodPressureData(
                    date = "20240806",
                    time = "091100",
                    systolic = 120,
                    diastolic = 80,
                    pulse = 70,
                    judgment = BloodPressureStatus.NORMAL as BloodPressureStatus?
                )
                // delay(3000)
                // sendBloodPressureData()
            }
            MeasurementType.Weight -> {
                _weightData.value = WeightData(
                    date = "20240806",
                    time = "091100",
                    scale = 60.0,
                    bmi = 20.0,
                    fatRate = 10.0,
                    muscleRate = 30.0,
                    judgment = WeightStatus.NORMAL as WeightStatus?
                )
                // delay(3000)
                // sendWeightData()
            }
        }
    }

    }

    // // BloodSugarData를 JSON으로 변환하여 이벤트로 전달
    // fun sendBloodSugarData() {
    //     viewModelScope.launch {
    //         try {
    //             // val jsonData = Json.encodeToString(BloodSugarData.serializer(), bloodSugarData!!.value)
    //             _events.emit(MeasurementEvent.BloodSugarSuccess(bloodSugarData!!.value))

    //         } catch (e: Exception) {
    //             _events.emit(MeasurementEvent.Error("데이터 변환 실패"))
    //         }
    //     }
    // }
    // // BloodPressureData를 JSON으로 변환하여 이벤트로 전달
    // fun sendBloodPressureData() {
    //     viewModelScope.launch {
    //         try {
    //             // val jsonData = Json.encodeToString(BloodPressureData.serializer(), bloodPressureData)
    //             _events.emit(MeasurementEvent.BloodPressureSuccess(bloodPressure!!.value))
    //         } catch (e: Exception) {
    //             _events.emit(MeasurementEvent.Error("데이터 변환 실패"))
    //         }
    //     }
    // }
    // // WeightData를 JSON으로 변환하여 이벤트로 전달
    // fun sendWeightData() {
    //     viewModelScope.launch {
    //         try {
    //             // val jsonData = Json.encodeToString(WeightData.serializer(), weightData)
    //             _events.emit(MeasurementEvent.WeightSuccess(weightData!!.value))
    //         } catch (e: Exception) {
    //             _events.emit(MeasurementEvent.Error("데이터 변환 실패"))
    //         }
    //     }
    // }

    //재시도
    fun triggerRetry() { _stage.value = MeasurementStage.Waiting }

    init {
        // ViewModel 생성 시 자동으로 측정 시작 트리거 실행
        triggerStartMeasuring()
    }
}


