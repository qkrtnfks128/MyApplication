package com.example.myapplication.model

import java.io.Serializable
import BloodPressureStatus

data class BloodPressureData(
    val date: String,
    val time: String,
    val systolic: Int, // 수축기
    val diastolic: Int, // 이완기
    val pulse: Int,
    val judgment: BloodPressureStatus? = null
): Serializable


