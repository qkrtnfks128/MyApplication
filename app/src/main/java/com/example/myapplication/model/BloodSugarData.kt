package com.example.myapplication.model

data class BloodSugarData(
    val id: String,
    val measurementTime: String, // yyyyMMddHHmmss 형식
    val bloodSugarLevel: Int, // mg/dL
    val mealType: MealType,
    val status: BloodSugarStatus
)

enum class MealType {
    BEFORE_MEAL, // 식전
    AFTER_MEAL   // 식후
}

enum class BloodSugarStatus {
    HIGH,    // 높음
    NORMAL,  // 보통
    LOW      // 낮음
} 