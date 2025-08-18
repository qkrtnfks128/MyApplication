package com.example.myapplication.model

import WeightStatus
import java.io.Serializable

data class WeightData(
    val date: String,
    val time: String,
    val scale: Double,
    val bmi: Double,
    val fatRate: Double,
    val muscleRate: Double,
    val judgment: WeightStatus? = null
): Serializable


