package com.example.myapplication.network.dto.ycsmart

import com.example.myapplication.model.BloodSugarData

/**
 * 혈당 기록 응답 모델
 */
data class BloodSugarHistoryResponse(
    val list: List<BloodSugarData>,
    val hasNext: Boolean
)
