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
import androidx.compose.runtime.CompositionLocalProvider
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
import com.example.myapplication.navigation.LocalAppNavController
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.ui.res.painterResource
import com.example.myapplication.ui.theme.YC_Color
import com.example.myapplication.ui.theme.CustomColor
import com.example.myapplication.ui.theme.Stroke
import com.example.myapplication.ui.theme.b4
import com.example.myapplication.ui.theme.h1
import com.example.myapplication.R


@Composable
fun UserAuthStartScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
        AppBar(
            leftButtonType = LeftButtonType.BACK,
            centerWidget = {
                Text(text = "사용자 인증", style = MaterialTheme.typography.h1)
            }
        )
        Text(text = "인증방법을 선택해주세요", style = MaterialTheme.typography.b4)
        Spacer(modifier = Modifier.height(35.dp))
        Row(modifier = Modifier.padding(start = 40.dp, top = 10.dp, end = 40.dp, bottom = 40.dp), horizontalArrangement = Arrangement.Center,verticalAlignment = Alignment.CenterVertically) {
                AuthOptionCard(
                    background = YC_Color.blue,
                    imageResId = R.drawable.face_recognition,
                    label = "얼굴인식",
                    onClick = {
                        navController.navigate(Screen.Detecting.route)
                    }
                )
            Spacer(modifier = Modifier.width(30.dp))
                AuthOptionCard(
                    background = YC_Color.green,
                    imageResId = R.drawable.phone_number,
                    label = "전화번호",
                    onClick = { navController.navigate(Screen.PhoneAuth.route) }
                )

        }
    }
}

@Composable
private fun AuthOptionCard(
    modifier: Modifier = Modifier,
    background: Color,
    imageResId: Int,
    label: String,
    onClick: () -> Unit
) {
    val cardShape = RoundedCornerShape(30.dp)
    Card(
        modifier = modifier.clickable { onClick() },
        shape = cardShape,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {


            Box(
                modifier = Modifier
                    .width(310.dp)
                    .border(10.dp, Stroke.black20, shape = cardShape)
                    .height(310.dp)
                    .background(background, shape = cardShape)
                    .padding(24.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(imageResId),
                        contentDescription = label,
                        modifier = Modifier.size(96.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = label,
                        color = CustomColor.white,
                        style = MaterialTheme.typography.b4
                    )
                }
            }

    }
}

@Preview(showBackground = true)
@Composable
fun UserAuthStartScreenPreview() {
    val nav = rememberNavController()
    CompositionLocalProvider(LocalAppNavController provides nav) {
        UserAuthStartScreen(nav)
    }
}


