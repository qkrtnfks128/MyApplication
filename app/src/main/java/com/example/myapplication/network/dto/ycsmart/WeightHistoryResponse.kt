package com.example.myapplication.network.dto.ycsmart

import com.example.myapplication.model.WeightData

/**
 * 체중 기록 응답 모델
 */
data class WeightHistoryResponse(
    val list: List<WeightData>,
    val hasNext: Boolean
)
