package com.example.myapplication.model

import java.io.Serializable

data class BloodSugarHistoryData(
    var hasNext: Boolean,
    var list : List<BloodSugarData>
)
