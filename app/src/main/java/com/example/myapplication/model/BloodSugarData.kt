package com.example.myapplication.model

import java.io.Serializable
import BloodSugarStatus
import MealType


data class BloodSugarData(
    val date: String,
    val time: String,
    val glucoseResult: Int,
    val temperature: Float,
    // 식전 식후 플래그 추가
    val mealFlag: MealType? = null,
    // 판정값 추가
    val judgment: BloodSugarStatus? = null
): Serializable




