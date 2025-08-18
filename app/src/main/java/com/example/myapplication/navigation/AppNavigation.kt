package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
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
import com.example.myapplication.page.login.UserAuthStartScreen
import com.example.myapplication.page.login.UserResultScreen
import com.example.myapplication.model.UserListResult
// no nav arguments used; passing complex object via SavedStateHandle
import com.example.myapplication.page.login.PhoneAuthScreen
import com.example.myapplication.page.measurement.MeasurementScreen
import com.example.myapplication.model.MeasurementType
import com.example.myapplication.page.login.DetectingScreen
import android.net.Uri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.myapplication.model.BloodPressureData
import com.example.myapplication.model.BloodSugarData
import com.example.myapplication.model.WeightData
import com.example.myapplication.page.error.ErrorScreen
import com.example.myapplication.viewmodel.measurement.MeasurementViewModel
import com.example.myapplication.viewmodel.measurement.MeasurementViewModelFactory
import kotlinx.serialization.json.Json

// AppNavigation에서 사용할 네비게이션 관련 함수와 화면을 정의합니다.

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    var shouldResetToMain by remember { mutableStateOf(false) }
    val appLifecycle: Lifecycle = ProcessLifecycleOwner.get().lifecycle

    DisposableEffect(appLifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> shouldResetToMain = true
                Lifecycle.Event.ON_START -> {
                    if (shouldResetToMain) {
                        // 포그라운드 복귀 시 메인으로 이동
                        navController.navigate(Screen.Main.route) {
                            popUpTo(navController.graph.id) { inclusive = true }
                            launchSingleTop = true
                            restoreState = false
                        }
                        shouldResetToMain = false
                    }
                }
                else -> Unit
            }
        }
        appLifecycle.addObserver(observer)
        onDispose { appLifecycle.removeObserver(observer) }
    }


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
            UserAuthStartScreen(navController)
        }
        composable(Screen.PhoneAuth.route) { PhoneAuthScreen(navController) }
        composable(Screen.UserResult.route) { backStackEntry ->
            val current = backStackEntry.savedStateHandle
            val prev = navController.previousBackStackEntry?.savedStateHandle

            //  이전 화면에서 다음 화면으로 데이터를 전달하고, 이전 화면의 데이터를 정리
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

            // 공유 ViewModel 사용
            val measurementViewModel: MeasurementViewModel = viewModel(
                factory = MeasurementViewModelFactory(type)
            )

            MeasurementScreen(navController = navController, type = type, vm = measurementViewModel)
        }
    //     //  혈당 결과
    //     composable(Screen.BloodSugarResult.route) { backStackEntry ->
    //         val current = backStackEntry.savedStateHandle
    //         val prev = navController.previousBackStackEntry?.savedStateHandle

    //          // 이전 화면에서 다음 화면으로 데이터를 전달하고, 이전 화면의 데이터를 정리
    //          prev?.get<BloodSugarData>(Screen.BloodSugarResult.KEY_BLOOD_SUGAR_DATA)?.let { moved ->
    //             current.set(Screen.BloodSugarResult.KEY_BLOOD_SUGAR_DATA, moved)
    //             prev.remove<BloodSugarData>(Screen.BloodSugarResult.KEY_BLOOD_SUGAR_DATA)
    //         }

    // // JSON 문자열을 파싱
    // val jsonData = current.get<String>(Screen.BloodSugarResult.KEY_BLOOD_SUGAR_DATA)
    // if (jsonData == null) {
    //     return@composable
    // }

    // val result = try {
    //     Json.decodeFromString<BloodSugarData>(jsonData)
    // } catch (e: Exception) {
    //     LogManager.error("JSON parsing error", e.toString())
    //     return@composable
    // }

    //         BloodSugarResultScreen(navController = navController, bloodSugarData = result)
    //     }
    //     // 혈압 결과
    //     composable(Screen.BloodPressureResult.route) { backStackEntry ->
    //         val current = backStackEntry.savedStateHandle
    //         val prev = navController.previousBackStackEntry?.savedStateHandle

    //         // 이전 화면에서 다음 화면으로 데이터를 전달하고, 이전 화면의 데이터를 정리
    //         prev?.get<BloodPressureData>(Screen.BloodPressureResult.KEY_BLOOD_PRESSURE_DATA)?.let { moved ->
    //             current.set(Screen.BloodPressureResult.KEY_BLOOD_PRESSURE_DATA, moved)
    //             prev.remove<BloodPressureData>(Screen.BloodPressureResult.KEY_BLOOD_PRESSURE_DATA)
    //         }

    //         val result = current.get<BloodPressureData>(Screen.BloodPressureResult.KEY_BLOOD_PRESSURE_DATA)?:return@composable

    //         BloodPressureResultScreen(navController = navController, bloodPressureData = result)
    //     }
    //     // 체중 결과
    //     composable(Screen.WeightResult.route) { backStackEntry ->
    //         val current = backStackEntry.savedStateHandle
    //         val prev = navController.previousBackStackEntry?.savedStateHandle

    //         // 이전 화면에서 다음 화면으로 데이터를 전달하고, 이전 화면의 데이터를 정리
    //         prev?.get<WeightData>(Screen.WeightResult.KEY_WEIGHT_DATA)?.let { moved ->
    //             current.set(Screen.WeightResult.KEY_WEIGHT_DATA, moved)
    //             prev.remove<WeightData>(Screen.WeightResult.KEY_WEIGHT_DATA)
    //         }

    //         val result = current.get<WeightData>(Screen.WeightResult.KEY_WEIGHT_DATA)?:return@composable

    //         WeightResultScreen(navController = navController, weightData = result)
    //     }
        composable(Screen.Detecting.route) {
            DetectingScreen(navController = navController)
        }
        composable(
            route = Screen.Error.route + "/{errorMessage}",
        ) { backStackEntry ->
            val errorMessage = backStackEntry.arguments?.getString("errorMessage")
                ?: "네트워크 오류가 발생했습니다."
            ErrorScreen(
                navController = navController,
                errorMessage = errorMessage
            )
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
    // object BloodSugarResult : Screen("bloodSugarResult")
    // {
    //     const val KEY_BLOOD_SUGAR_DATA: String = "blood_sugar_data"
    // }
    // object BloodPressureResult : Screen("bloodPressureResult")
    // {
    //     const val KEY_BLOOD_PRESSURE_DATA: String = "blood_pressure_data"
    // }
    // object WeightResult : Screen("weightResult")
    // {
    //     const val KEY_WEIGHT_DATA: String = "weight_data"
    // }
    object Detecting : Screen("detecting")
    object Error : Screen("error")

}
