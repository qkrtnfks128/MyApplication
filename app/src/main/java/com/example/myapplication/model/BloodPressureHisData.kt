package com.example.myapplication.model

import java.io.Serializable

data class BloodPressureHistoryData(
    var hasNext: Boolean,
    var list: List<BloodPressureData>
)
