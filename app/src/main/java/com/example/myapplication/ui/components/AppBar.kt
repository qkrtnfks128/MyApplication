package com.example.myapplication.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import androidx.compose.ui.tooling.preview.Preview
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.myapplication.navigation.LocalAppNavController
import com.example.myapplication.navigation.Screen
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.manager.AdminManager
import com.example.myapplication.manager.SelectedOrgStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

enum class LeftButtonType {
    NONE, HOME, BACK, LOGOUT
}

data class ButtonConfig(
    val icon: ImageVector,
    val text: String,
    val iconTint: Color = Color.Black,
    val textColor: Color = Color.Black
)

@Composable
fun AppBar(
    leftButtonType: LeftButtonType = LeftButtonType.NONE,
    centerWidget: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current  // Context 가져오기
    val nav = LocalAppNavController.current 

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 40.dp, top = 24.dp, end = 40.dp, bottom = 24.dp),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            // 가운데 위젯 - 항상 중앙에 위치
            centerWidget?.invoke()
            
            // 왼쪽 버튼 섹션
            if (leftButtonType != LeftButtonType.NONE) {
                val leftButtonConfig = getLeftButtonConfig(leftButtonType)
                val leftButtonClickHandler = getLeftButtonClickHandler(
                    leftButtonType,
                    context,
                    nav
                )
                AppBarButton(
                    buttonConfig = leftButtonConfig,
                    onClick = leftButtonClickHandler,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
            }

            // 우측 종료 버튼 섹션
            val rightButtonConfig = ButtonConfig(
                icon = Icons.Default.PowerSettingsNew,
                text = "종료",
                iconTint = Color.Red
            )
            AppBarButton(
                buttonConfig = rightButtonConfig,
                onClick = {
                    // 종료 버튼 클릭 시 동작 구현
                    (context as? android.app.Activity)?.finish()
                // 유저정보 삭제 기능 추가
                    SelectedOrgStore.clear()

                },
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}

@Composable
private fun AppBarButton(
    buttonConfig: ButtonConfig,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 12.dp,
                spotColor = Color(0x26000000),
                ambientColor = Color(0x26000000)
            )
            .clip(RoundedCornerShape(size = 100.dp))
            .background(color = Color.White)
            .clickable { onClick?.invoke() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                start = 20.dp,
                top = 20.dp,
                end = 30.dp,
                bottom = 20.dp
            )
        ) {
            Icon(
                imageVector = buttonConfig.icon,
                contentDescription = buttonConfig.text,
                tint = buttonConfig.iconTint,
                modifier = Modifier.size(42.dp)
            )
            Text(
                text = buttonConfig.text,
                style = getButtonTextStyle(buttonConfig.textColor)
            )
        }
    }
}

private fun getLeftButtonConfig(buttonType: LeftButtonType): ButtonConfig {
    return when (buttonType) {
        LeftButtonType.HOME -> ButtonConfig(
            icon = Icons.Default.Home,
            text = "처음으로"
        )
        LeftButtonType.BACK -> ButtonConfig(
            icon = Icons.Default.ArrowBack,
            text = "뒤로가기"
        )
        LeftButtonType.LOGOUT -> ButtonConfig(
            icon = Icons.Default.Logout,
            text = "로그아웃"
        )
        LeftButtonType.NONE -> ButtonConfig(
            icon = Icons.Default.Home,
            text = ""
        )
    }
}

private fun getLeftButtonClickHandler(
    buttonType: LeftButtonType,
    context: Context,
    nav: NavController
): (() -> Unit)? {
    return when (buttonType) {
        LeftButtonType.HOME -> {
           
            {
                nav.navigate(Screen.Main.route) {
                    popUpTo(nav.graph.id) { inclusive = true } // 그래프 루트까지 완전 삭제
                    launchSingleTop = true
                    restoreState = false
                }
            }
        }
        LeftButtonType.BACK -> {
            {
            // 뒤로가기 버튼 클릭 시 현재 액티비티를 종료하여 이전 화면으로 이동합니다.
            (context as? android.app.Activity)?.onBackPressed()
       
            }
        }
        LeftButtonType.LOGOUT -> { {
            CoroutineScope(Dispatchers.Main).launch {
                AdminManager.logout()
            }
        nav.navigate(Screen.UserAuth.route) {
            popUpTo(nav.graph.id) { inclusive = true }
            launchSingleTop = true
            restoreState = false
        }
        } }
        LeftButtonType.NONE -> null
    }
}


private fun getButtonTextStyle(textColor: Color): TextStyle {
    return TextStyle(
        fontSize = 40.sp,
        lineHeight = 52.sp,
        fontFamily = FontFamily(Font(R.font.pretendard)),
        fontWeight = FontWeight(600),
        color = textColor,
        textAlign = TextAlign.Center,
    )
}

@Preview(showBackground = true)
@Composable
fun AppBarPreview() {
    val nav = rememberNavController()
    CompositionLocalProvider(LocalAppNavController provides nav) {
        AppBar(leftButtonType = LeftButtonType.BACK)
    }
}