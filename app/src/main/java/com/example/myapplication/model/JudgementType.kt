import androidx.compose.ui.graphics.Color
import com.example.myapplication.ui.theme.CustomColor


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

enum class WeightStatus {
    HIGH,    // 높음
    NORMAL,  // 보통
    LOW;     // 낮음

}


fun WeightStatus.getStatusColor(): Color {
    return when (this) {
        WeightStatus.HIGH ->CustomColor.red
        WeightStatus.NORMAL -> CustomColor.green
        WeightStatus.LOW -> CustomColor.blue
    }
}

fun WeightStatus.getStatusText(): String {
    return when (this) {
        WeightStatus.HIGH -> "높음"
        WeightStatus.NORMAL -> "정상"
        WeightStatus.LOW -> "낮음"
    }
}
