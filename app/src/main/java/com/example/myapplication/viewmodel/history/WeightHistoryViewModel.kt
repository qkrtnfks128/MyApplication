package com.example.myapplication.viewmodel.history

import WeightStatus
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.WeightData
import com.example.myapplication.model.WeightHistoryData
import com.example.myapplication.repository.YcSmartRepository
import com.example.myapplication.repository.YcSmartRepositoryFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 체중 기록 화면을 위한 ViewModel
 */
class WeightHistoryViewModel(
    private val repository: YcSmartRepository = YcSmartRepositoryFactory.create()
) : ViewModel() {

    // 체중 기록 데이터
    private val _historyItems = MutableStateFlow<WeightHistoryData>(WeightHistoryData(false, emptyList()))
    val historyItems: StateFlow<WeightHistoryData> = _historyItems.asStateFlow()

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
                val newData = createTestData()
                // val newData = repository.getWeightHistory(userId, offset, limit)

                val currentItems = _historyItems.value.list
                val updatedItems = currentItems + newData.list

                _historyItems.value = WeightHistoryData(
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
    private fun createTestData(): WeightHistoryData {
        return WeightHistoryData(
            hasNext = false,
            list = listOf(
                WeightData(
                    date = "20240805",
                    time = "102000",
                    scale = 65.5,
                    bmi = 22.3,
                    fatRate = 25.0,
                    muscleRate = 32.5,
                    judgment = WeightStatus.NORMAL as WeightStatus
                ),
                WeightData(
                    date = "20240806",
                    time = "091100",
                    scale = 66.2,
                    bmi = 22.5,
                    fatRate = 25.2,
                    muscleRate = 32.3,
                    judgment = WeightStatus.NORMAL as WeightStatus
                ),
                WeightData(
                    date = "20240804",
                    time = "174000",
                    scale = 65.0,
                    bmi = 22.1,
                    fatRate = 24.8,
                    muscleRate = 32.7,
                    judgment = WeightStatus.NORMAL as WeightStatus
                ),
                WeightData(
                    date = "20240803",
                    time = "120000",
                    scale = 64.8,
                    bmi = 22.0,
                    fatRate = 24.5,
                    muscleRate = 33.0,
                    judgment = WeightStatus.NORMAL as WeightStatus
                ),
                WeightData(
                    date = "20240802",
                    time = "183000",
                    scale = 65.3,
                    bmi = 22.2,
                    fatRate = 24.9,
                    muscleRate = 32.6,
                    judgment = WeightStatus.NORMAL as WeightStatus
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

