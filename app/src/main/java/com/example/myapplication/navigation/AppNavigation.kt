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
import com.example.myapplication.page.login.UserAuthScreen
import com.example.myapplication.page.login.UserResultScreen
import com.example.myapplication.model.UserListResult
// no nav arguments used; passing complex object via SavedStateHandle
import com.example.myapplication.page.login.PhoneAuthScreen
import com.example.myapplication.page.measurement.MeasurementScreen
import com.example.myapplication.page.measurement.MeasurementType

// AppNavigation에서 사용할 네비게이션 관련 함수와 화면을 정의합니다.


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
    
    androidx.compose.runtime.CompositionLocalProvider(LocalAppNavController provides navController) {
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
        composable(Screen.UserAuth.route) {
            UserAuthScreen(navController)
        }
        composable(Screen.PhoneAuth.route) { PhoneAuthScreen(navController) }
        composable(Screen.UserResult.route) { backStackEntry ->
            val current = backStackEntry.savedStateHandle
            val prev = navController.previousBackStackEntry?.savedStateHandle

            // first time: move data from previous entry to current entry
            prev?.get<UserListResult>(Screen.UserResult.KEY_RESULT)?.let { moved ->
                current.set(Screen.UserResult.KEY_RESULT, moved)
                prev.remove<UserListResult>(Screen.UserResult.KEY_RESULT)
            }

            val result = current.get<UserListResult>(Screen.UserResult.KEY_RESULT)
                ?: return@composable // <- 여기서 조용히 빠져나와 크래시 방지

            UserResultScreen(navController = navController, result = result)
        }
        composable(Screen.Measurement.route + "/{type}") { backStackEntry ->
            val typeStr = backStackEntry.arguments?.getString("type") ?: "BloodSugar"
            val type = when (typeStr) {
                "BloodPressure" -> MeasurementType.BloodPressure
                "Weight" -> MeasurementType.Weight
                else -> MeasurementType.BloodSugar
            }
            MeasurementScreen(navController = navController, type = type)
        }
    }
        
    }
}

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Main : Screen("main")
    object BloodSugarHistory : Screen("bloodSugarHistory")
    object Login : Screen("adminLogin")
    object AdminOrgSelect : Screen("adminOrgSelect")
    object UserAuth : Screen("userAuth")
    object PhoneAuth : Screen("phoneAuth")
    object UserResult : Screen("userResult") {
        const val KEY_RESULT: String = "user_result"
    }
    object Measurement : Screen("measurement")
} 