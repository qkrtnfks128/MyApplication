package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.page.MainScreen
import com.example.myapplication.page.history.BloodSugarHistoryScreen
import com.example.myapplication.page.login.LoginScreen

// AppNavigation에서 사용할 네비게이션 관련 함수와 화면을 정의합니다.
// MainScreen: 메인 화면
// BloodSugarHistoryScreen: 혈당 기록 내역 화면

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            MainScreen(navController)
        }
        composable(Screen.BloodSugarHistory.route) {
            BloodSugarHistoryScreen(navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
    }
}

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object BloodSugarHistory : Screen("bloodSugarHistory")
    object Login : Screen("login")
} 