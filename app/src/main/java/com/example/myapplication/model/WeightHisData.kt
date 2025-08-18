package com.example.myapplication.model

import java.io.Serializable

data class WeightHistoryData(
    var hasNext: Boolean,
    var list: List<WeightData>
)
