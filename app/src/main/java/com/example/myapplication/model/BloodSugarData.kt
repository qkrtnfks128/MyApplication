

data class BloodSugarData(
    val Date: String,
    val Time: String,
    val GlucoseResult: Int,
    val Temperature: Float,
    // 식전 식후 플래그 추가
    val mealFlag: MealType? = null,
    // 판정값 추가
    val judgment: BloodSugarStatus? = null

)

enum class MealType {
    BEFORE_MEAL, // 식전
    AFTER_MEAL   // 식후
}

