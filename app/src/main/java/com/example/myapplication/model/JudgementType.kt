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
