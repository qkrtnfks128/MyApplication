package com.example.myapplication.network.dto.ycsmart

import com.example.myapplication.model.BloodPressureData

/**
 * 혈압 기록 응답 모델
 */
data class BloodPressureHistoryResponse(
    val list: List<BloodPressureData>,
    val hasNext: Boolean
)
