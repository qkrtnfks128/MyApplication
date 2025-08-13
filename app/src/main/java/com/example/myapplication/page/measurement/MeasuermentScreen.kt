package com.example.myapplication.page.measurement

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.components.AppBar
import com.example.myapplication.components.LeftButtonType
import com.example.myapplication.manager.SelectedUserStore
import com.example.myapplication.navigation.LocalAppNavController
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.model.MeasurementType
import com.example.myapplication.model.displayName
import com.example.myapplication.ui.theme.h1

@Composable
fun MeasurementScreen(
    navController: NavController,
    type: MeasurementType
) {
    val userName: String = SelectedUserStore.get()?.name ?: "사용자"

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppBar(
            leftButtonType = LeftButtonType.HOME,
            centerWidget = {
                Text(text = "$userName 어르신", style = MaterialTheme.typography.h1)
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = type.displayName(), style = MaterialTheme.typography.headlineLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    modifier = Modifier
                        .width(200.dp)
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.Transparent
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFE3F2FD), shape = RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "측정대기중", color = Color(0xFF1976D2), style = MaterialTheme.typography.titleMedium)
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Device/Image placeholder
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(260.dp),
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFFF4F6F8)
            ) {}
        }


    }
}

@Preview(showBackground = true)
@Composable
private fun MeasurementScreenPreview_BloodSugar() {
    val nav = rememberNavController()
    CompositionLocalProvider(LocalAppNavController provides nav) {
        MeasurementScreen(nav, MeasurementType.BloodSugar)
    }
}

@Preview(showBackground = true)
@Composable
private fun MeasurementScreenPreview_BloodPressure() {
    val nav = rememberNavController()
    CompositionLocalProvider(LocalAppNavController provides nav) {
        MeasurementScreen(nav, MeasurementType.BloodPressure)
    }
}

@Preview(showBackground = true)
@Composable
private fun MeasurementScreenPreview_Weight() {
    val nav = rememberNavController()
    CompositionLocalProvider(LocalAppNavController provides nav) {
        MeasurementScreen(nav, MeasurementType.Weight)
    }
}


