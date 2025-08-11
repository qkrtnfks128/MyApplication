package com.example.myapplication.page.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.manager.AdminManager
import com.example.myapplication.manager.SelectedOrgStore
import com.example.myapplication.navigation.Screen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.utils.LogManager
import kotlinx.coroutines.delay
import android.util.Base64
import androidx.compose.runtime.CompositionLocalProvider
import com.example.myapplication.navigation.LocalAppNavController

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        // 자동 로그인 시도 후 성공 시 메인, 실패 시 로그인으로 이동
        runCatching {
            AdminManager.adminLogin(
                "03183921140",
               "000000"
            )
        }.onSuccess { result ->
            result.onSuccess {
                LogManager.auth("Splash", "auto_login", true)
                val cached = SelectedOrgStore.getSelected()
                val next = if (cached != null) Screen.Main.route else Screen.AdminOrgSelect.route
                navController.navigate(next) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                    launchSingleTop = true
                }
            }.onFailure {
                LogManager.auth("Splash", "auto_login", false)
                SelectedOrgStore.clear()
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }.onFailure {
            LogManager.error("Splash", "auto_login exception", it)
            SelectedOrgStore.clear()
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
                launchSingleTop = true
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    val nav = rememberNavController()
    CompositionLocalProvider(LocalAppNavController provides nav) {
        SplashScreen(nav)
    }
}


