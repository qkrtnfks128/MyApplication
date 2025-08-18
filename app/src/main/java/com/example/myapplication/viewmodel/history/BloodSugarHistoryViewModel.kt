package com.example.myapplication.viewmodel.history

import BloodSugarStatus
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.BloodSugarData
import com.example.myapplication.model.BloodSugarHistoryData
// ApiResult 사용 제거 - 인터셉터로 오류 처리
import com.example.myapplication.repository.YcSmartRepository
import com.example.myapplication.repository.YcSmartRepositoryFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
// Flow 사용 제거
import kotlinx.coroutines.launch

/**
 * 혈당 기록 화면을 위한 ViewModel
 */
class BloodSugarHistoryViewModel(
) : ViewModel() {



    // 혈당 기록 데이터
    private val _historyItems = MutableStateFlow<BloodSugarHistoryData>(BloodSugarHistoryData(false, emptyList()))
    val historyItems: StateFlow<BloodSugarHistoryData> = _historyItems.asStateFlow()

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
        viewModelScope.launch {

                _isLoading.value = true

                // Repository에서 직접 데이터 목록을 반환
                val newItems : BloodSugarHistoryData =  createTestData()
                // val newItems : BloodSugarHistoryData = repository.getBloodSugarHistory(userId, currentState.offset, limit)

                    val currentItems = _historyItems.value.list
                    val updatedItems = currentItems + newItems.list

                _historyItems.value.list = updatedItems

                // 더 이상 데이터가 없는지 확인 (가져온 데이터가 limit보다 적으면 마지막 페이지)
                val isLastPage = !newItems.hasNext
                offset = offset + limit
                _isLoading.value = false
        }
    }



    /**
     * 테스트 데이터 생성
     */
    private fun createTestData(): BloodSugarHistoryData {
        // BloodSugarHistoryScreen.kt의 테스트 데이터를 재사용
        return BloodSugarHistoryData(
            hasNext = true,
            list = listOf(
            BloodSugarData(
                date = "20240805",
                time = "102000",
                glucoseResult = 180,
                temperature = 36.5f,
                mealFlag =MealType.AFTER_MEAL,
                judgment = BloodSugarStatus.NORMAL as BloodSugarStatus
            ),
            BloodSugarData(
                date = "20240806",
                time = "091100",
                glucoseResult = 235,
                temperature = 36.8f,
                mealFlag = MealType.BEFORE_MEAL,
                judgment = BloodSugarStatus.HIGH as BloodSugarStatus
            ),
            BloodSugarData(
                date = "20240804",
                time = "174000",
                glucoseResult = 75,
                temperature = 36.2f,
                mealFlag = MealType.AFTER_MEAL,
                judgment = BloodSugarStatus.LOW as BloodSugarStatus
            ),
            BloodSugarData(
                date = "20240803",
                time = "120000",
                glucoseResult = 120,
                temperature = 36.6f,
                mealFlag = MealType.BEFORE_MEAL,
                judgment = BloodSugarStatus.NORMAL as BloodSugarStatus
            ),
            BloodSugarData(
                date = "20240802",
                time = "183000",
                glucoseResult = 200,
                temperature = 37.1f,
                mealFlag = MealType.AFTER_MEAL,
                judgment = BloodSugarStatus.HIGH as BloodSugarStatus
            )
            )
        )
    }

     /**
     * 초기 데이터 로드
     */
   init{
    loadNextPage()
}
}

