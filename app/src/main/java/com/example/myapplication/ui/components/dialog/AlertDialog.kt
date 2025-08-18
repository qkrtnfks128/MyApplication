package com.example.myapplication.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.myapplication.ui.theme.CustomColor
import com.example.myapplication.ui.theme.Stroke
import com.example.myapplication.ui.theme.b3
import com.example.myapplication.ui.theme.b4
import com.example.myapplication.ui.theme.b5

/**
 * 확인/취소 버튼이 있는 다이얼로그 컴포넌트
 * @param title 다이얼로그 제목
 * @param message 다이얼로그 내용 메시지
 * @param confirmText 확인 버튼 텍스트 (기본값: "네, 종료할게요")
 * @param cancelText 취소 버튼 텍스트 (기본값: "아니오, 계속할게요")
 * @param onConfirm 확인 버튼 클릭 시 실행할 콜백
 * @param onDismiss 취소 버튼 클릭 또는 다이얼로그 외부 클릭 시 실행할 콜백
 */
@Composable
fun AlertDialog(
    title: String,
    message: String? = null,
    confirmText: String = "네, 종료할게요",
    cancelText: String = "아니오, 계속할게요",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    // Dialog를 사용하여 크기 제한 문제 해결
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        val configuration = LocalConfiguration.current
        val maxWidth = configuration.screenWidthDp.dp * 0.9f

        Surface(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.wrapContentWidth() // 내용에 맞게 너비 조정
                .widthIn(max = maxWidth) // 최대 너비만 제한
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 제목
                Text(
                    text = title,
                    style = MaterialTheme.typography.b3,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                // 메시지 (있는 경우에만)
                if (message != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = message,
                        style = MaterialTheme.typography.b5,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // 버튼 영역
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(30.dp)
                ) {
                    // 취소 버튼
                    Button(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CustomColor.white,
                            contentColor = CustomColor.black
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .border(6.dp, CustomColor.black, RoundedCornerShape(30.dp))
                    ) {
                        Text(
                            text = cancelText,
                            style = MaterialTheme.typography.b4,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }

                    // 확인 버튼
                    Button(
                        onClick = onConfirm,
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CustomColor.red,
                            contentColor = CustomColor.white
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .border(6.dp, Stroke.black20, RoundedCornerShape(30.dp))
                    ) {
                        Text(
                            text = confirmText,
                            style = MaterialTheme.typography.b4,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AlertDialogPreview() {
    Surface {
        AlertDialog(
            title = "앱을 종료하시겠어요?",
            onConfirm = {},
            onDismiss = {}
        )
    }
}
