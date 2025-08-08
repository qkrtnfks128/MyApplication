package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.page.MainScreen
import com.example.myapplication.page.history.BloodSugarHistoryScreen
import com.example.myapplication.page.login.AdminLoginScreen
import com.example.myapplication.page.splash.SplashScreen
import com.example.myapplication.utils.LogManager
import com.example.myapplication.page.admin.AdminOrgSelectScreen

// AppNavigation에서 사용할 네비게이션 관련 함수와 화면을 정의합니다.
// MainScreen: 메인 화면
// BloodSugarHistoryScreen: 혈당 기록 내역 화면

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    LaunchedEffect(navController) {
        var previousRoute: String? = null
        navController.currentBackStackEntryFlow.collect { entry ->
            val currentRoute: String = entry.destination.route ?: "unknown"
            val from: String = previousRoute ?: "start"
            LogManager.navigation("AppNavigation", from, currentRoute)
            previousRoute = currentRoute
        }
    }
    
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }
        composable(Screen.Main.route) {
            MainScreen(navController)
        }
        composable(Screen.BloodSugarHistory.route) {
            BloodSugarHistoryScreen(navController)
        }
        composable(Screen.Login.route) {
            AdminLoginScreen(navController)
        }
        composable(Screen.AdminOrgSelect.route) {
            AdminOrgSelectScreen(navController)
        }
    }
}

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Main : Screen("main")
    object BloodSugarHistory : Screen("bloodSugarHistory")
    object Login : Screen("adminLogin")
    object AdminOrgSelect : Screen("adminOrgSelect")
} 