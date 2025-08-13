package com.example.myapplication.manager

import com.example.myapplication.model.MeasurementType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// SelectedMeasurementStore는 선택된 MeasurementType(측정 항목) 정보를 메모리와 StateFlow에 저장하고 불러오는 역할을 합니다.
// 앱 전체에서 선택된 측정 항목 정보를 관리하며, 일시적으로만 보관합니다.
// initialize(): 선택된 측정 항목 정보를 초기화합니다. (null로 설정)
// save(type): 선택한 MeasurementType을 저장합니다.
// get(): 저장된 MeasurementType 정보를 반환합니다. 없으면 null을 반환합니다.
// clear(): 저장된 측정 항목 정보를 삭제합니다.
// observe(): 선택된 측정 항목의 변화를 StateFlow로 관찰할 수 있습니다.


object SelectedMeasurementStore {
    @Volatile private var selected: MeasurementType? = null
    private val _selectedFlow: MutableStateFlow<MeasurementType?> = MutableStateFlow(null)

    fun initialize() {
        selected = null
        _selectedFlow.value = null
    }

    fun save(type: MeasurementType) {
        selected = type
        _selectedFlow.value = type
    }

    fun get(): MeasurementType? = selected

    fun clear() {
        selected = null
        _selectedFlow.value = null
    }

    fun observe(): StateFlow<MeasurementType?> = _selectedFlow.asStateFlow()
}


