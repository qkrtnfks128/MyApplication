package com.example.myapplication.model

data class BloodPressureData(
    val id: String,
    val measurementTime: String, // yyyyMMddHHmmss
    val systolic: Int, // 수축기
    val diastolic: Int, // 이완기
    val pulse: Int
)


