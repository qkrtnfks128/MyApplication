import androidx.compose.ui.graphics.Color
import com.example.myapplication.ui.theme.CustomColor

enum class MealType {
    BEFORE_MEAL, // 식전
    AFTER_MEAL   // 식후
}

fun MealType.displayName(): String = when (this) {
    MealType.BEFORE_MEAL -> "식전"
    MealType.AFTER_MEAL -> "식후"
}

enum class BloodSugarStatus {
    HIGH,    // 높음
    NORMAL,  // 보통
    LOW;     // 낮음

}

fun BloodSugarStatus.getStatusColor(): Color {
    return when (this) {
        BloodSugarStatus.HIGH ->CustomColor.red
        BloodSugarStatus.NORMAL -> CustomColor.green
        BloodSugarStatus.LOW -> CustomColor.blue
    }
}

fun BloodSugarStatus.getStatusText(): String {
    return when (this) {
        BloodSugarStatus.HIGH -> "높음"
        BloodSugarStatus.NORMAL -> "정상"
        BloodSugarStatus.LOW -> "낮음"
    }
}

fun BloodSugarStatus.getDescriptionText(mealType: MealType): String {
    val mealTypeText = when (mealType) {
        MealType.BEFORE_MEAL -> "식사 전"
        MealType.AFTER_MEAL -> "식사 후"
        else -> ""
    }

    return when (this) {
        BloodSugarStatus.HIGH -> "$mealTypeText 혈당 기준 높은 편이에요."
        BloodSugarStatus.LOW -> "$mealTypeText 혈당 기준 낮은 편이에요."
        BloodSugarStatus.NORMAL -> "$mealTypeText 혈당 기준 정상 범위에요."
    }
}


enum class BloodPressureStatus {
    HIGH,    // 높음
    NORMAL,  // 보통
    LOW;     // 낮음

}

fun BloodPressureStatus.getStatusColor(): Color {
    return when (this) {
        BloodPressureStatus.HIGH ->CustomColor.red
        BloodPressureStatus.NORMAL -> CustomColor.green
        BloodPressureStatus.LOW -> CustomColor.blue
    }
}

fun BloodPressureStatus.getStatusText(): String {
    return when (this) {
        BloodPressureStatus.HIGH -> "높음"
        BloodPressureStatus.NORMAL -> "정상"
        BloodPressureStatus.LOW -> "낮음"
    }
}

fun BloodPressureStatus.getDescriptionText(): String {
    return when (this) {
        BloodPressureStatus.HIGH -> "높은 혈압으로 분류되는 수치에요."
        BloodPressureStatus.LOW -> "낮은 혈압으로 분류되는 수치에요."
        BloodPressureStatus.NORMAL -> "일반적으로 안정적인 혈압 수치에요."
    }
}

enum class WeightStatus {
    HIGH1,    // 과체중
    HIGH2,    // 비만 3
    HIGH3,    // 비만 2
    HIGH4,    // 비만 3
    NORMAL,  // 정상
    LOW;     // 저체중

}


fun WeightStatus.getStatusColor(): Color {
    return when (this) {
        WeightStatus.LOW -> CustomColor.indigo
        WeightStatus.NORMAL -> CustomColor.green
        WeightStatus.HIGH1 -> CustomColor.orange
        WeightStatus.HIGH2 -> CustomColor.red
        WeightStatus.HIGH3 -> CustomColor.red
        WeightStatus.HIGH4 -> CustomColor.black
    }
}

fun WeightStatus.getStatusText(): String {
    return when (this) {
        WeightStatus.HIGH1 -> "과체중"
        WeightStatus.HIGH2 -> "비만1"
        WeightStatus.HIGH3 -> "비만2"
        WeightStatus.HIGH4 -> "비만3"
        WeightStatus.NORMAL -> "정상"
        WeightStatus.LOW -> "저체중"
    }
}

fun WeightStatus.getDescriptionText(): String {
    return when (this) {
        WeightStatus.HIGH1 -> "체중이 살짝 높은 편이에요."
        WeightStatus.HIGH2 -> "체중이 기준보다 높은 편이에요."
        WeightStatus.HIGH3 -> "체중이 많이 높은 상태에요."
        WeightStatus.HIGH4 -> "병원이나 전문가와 상담을 권장하는 체중입니다."
        WeightStatus.NORMAL -> "지금 체중은 건강한 범위에 있어요."
        WeightStatus.LOW -> "현재 체중이 부족한 편이에요."
    }
}
