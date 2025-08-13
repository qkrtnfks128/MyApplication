package com.example.myapplication.model

enum class MeasurementType { BloodSugar, BloodPressure, Weight }

fun MeasurementType.displayName(): String = when (this) {
    MeasurementType.BloodSugar -> "혈당"
    MeasurementType.BloodPressure -> "혈압"
    MeasurementType.Weight -> "체중"
}




