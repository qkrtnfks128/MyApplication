package com.example.myapplication.viewmodel.history

import BloodPressureStatus
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.BloodPressureData
import com.example.myapplication.model.BloodPressureHistoryData
import com.example.myapplication.repository.YcSmartRepository
import com.example.myapplication.repository.YcSmartRepositoryFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 혈압 기록 화면을 위한 ViewModel
 */
class BloodPressureHistoryViewModel(
) : ViewModel() {

    // 혈압 기록 데이터
    private val _historyItems = MutableStateFlow<BloodPressureHistoryData>(BloodPressureHistoryData(false, emptyList()))
    val historyItems: StateFlow<BloodPressureHistoryData> = _historyItems.asStateFlow()

    // UI 상태
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // 한 번에 가져올 데이터 개수
    private var offset = 0
    private val limit = 10




    /**
     * 다음 페이지 로드
     */
    fun loadNextPage() {
        if (_isLoading.value || (_historyItems.value.list.isNotEmpty() && !_historyItems.value.hasNext)) {
            return
        }

        viewModelScope.launch {
            _isLoading.value = true

            try {
                // Repository에서 직접 데이터 목록을 반환
                 // val newItems : BloodPressureHistoryData = repository.getBloodPressureHistory(userId, currentState.offset, limit)
                val newData = createTestData()

                val currentItems = _historyItems.value.list
                val updatedItems = currentItems + newData.list

                _historyItems.value = BloodPressureHistoryData(
                    hasNext = newData.hasNext,
                    list = updatedItems
                )

                offset += limit
            } catch (e: Exception) {
                // 에러 처리는 인터셉터에서 하므로 여기서는 별도 처리 없음
            } finally {
                _isLoading.value = false
            }
        }
    }



    /**
     * 테스트 데이터 생성
     */
    private fun createTestData(): BloodPressureHistoryData {
        return BloodPressureHistoryData(
            hasNext = false,
            list = listOf(
                BloodPressureData(
                    date = "20240805",
                    time = "102000",
                    systolic = 120,
                    diastolic = 80,
                    pulse = 70,
                    judgment = BloodPressureStatus.NORMAL as BloodPressureStatus
                ),
                BloodPressureData(
                    date = "20240806",
                    time = "091100",
                    systolic = 135,
                    diastolic = 85,
                    pulse = 75,
                    judgment = BloodPressureStatus.HIGH as BloodPressureStatus
                ),
                BloodPressureData(
                    date = "20240804",
                    time = "174000",
                    systolic = 110,
                    diastolic = 70,
                    pulse = 65,
                    judgment = BloodPressureStatus.NORMAL as BloodPressureStatus
                ),
                BloodPressureData(
                    date = "20240803",
                    time = "120000",
                    systolic = 125,
                    diastolic = 82,
                    pulse = 72,
                    judgment = BloodPressureStatus.NORMAL as BloodPressureStatus
                ),
                BloodPressureData(
                    date = "20240802",
                    time = "183000",
                    systolic = 145,
                    diastolic = 95,
                    pulse = 80,
                    judgment = BloodPressureStatus.HIGH as BloodPressureStatus
                )
            )
        )
    }

    /**
     * 초기 데이터 로드
     */
    init {
        loadNextPage()
    }
}

