package com.example.myapplication.page.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.material.icons.filled.TagFaces
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.components.AppBar
import com.example.myapplication.components.LeftButtonType
import com.example.myapplication.navigation.Screen
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

private val AUTH_BLUE: Color = Color(0xFF0B5DB8)
private val AUTH_BLUE_BORDER: Color = Color(0xFF0A4C96)
private val AUTH_GREEN: Color = Color(0xFF17A34A)
private val AUTH_GREEN_BORDER: Color = Color(0xFF12833C)
private val CARD_SHAPE: RoundedCornerShape = RoundedCornerShape(28.dp)

@Composable
fun UserAuthScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
        AppBar(
            leftButtonType = LeftButtonType.BACK,
            centerWidget = {
                Text(text = "사용자 인증", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "인증방법을 선택해주세요", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth().padding(start = 40.dp, top = 10.dp, end = 40.dp, bottom = 40.dp), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            Box(modifier = Modifier.weight(1f)) {
                AuthOptionCard(
                    modifier = Modifier.fillMaxWidth(),
                    background = AUTH_BLUE,
                    border = AUTH_BLUE_BORDER,
                    icon = { Icon(imageVector = Icons.Filled.TagFaces, contentDescription = "face", tint = Color.White, modifier = Modifier.size(64.dp)) },
                    label = "얼굴인식",
                    onClick = { }
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                AuthOptionCard(
                    modifier = Modifier.fillMaxWidth(),
                    background = AUTH_GREEN,
                    border = AUTH_GREEN_BORDER,
                    icon = { Icon(imageVector = Icons.Filled.Dialpad, contentDescription = "phone", tint = Color.White, modifier = Modifier.size(64.dp)) },
                    label = "전화번호",
                    onClick = { navController.navigate(Screen.PhoneAuth.route) }
                )
            }
        }
    }
}

@Composable
private fun AuthOptionCard(modifier: Modifier = Modifier, background: Color, border: Color, icon: @Composable () -> Unit, label: String, onClick: () -> Unit) {
    Card(modifier = modifier.height(360.dp).clickable { onClick() }, shape = CARD_SHAPE, colors = CardDefaults.cardColors(containerColor = Color.Transparent), elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
        Box(modifier = Modifier.fillMaxSize().background(border, shape = CARD_SHAPE).padding(10.dp)) {
            Box(modifier = Modifier.fillMaxSize().background(background, shape = CARD_SHAPE).padding(24.dp)) {
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    icon()
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(text = label, color = Color.White, style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserAuthScreenPreview() {
    MyApplicationTheme { UserAuthScreen(rememberNavController()) }
}


