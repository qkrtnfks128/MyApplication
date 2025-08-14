package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.myapplication.manager.AdminManager
import com.example.myapplication.navigation.AppNavigation
import com.example.myapplication.navigation.Screen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.utils.LogManager
import kotlinx.coroutines.launch
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.network.ResponseInterceptor

// MainActivity는 앱의 진입점으로, AppNavigation을 통해 전체 네비게이션 구조를 관리합니다.
// MyApplicationTheme를 적용하여 일관된 UI 스타일을 제공합니다.

class MainActivity : ComponentActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 앱 시작 시 로그인 상태 확인
        AdminManager.getCurrentSession()

        setContent {
            MyApplicationTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                      // 에러 네비게이션 콜백 설정
                LaunchedEffect(navController) {
                    ResponseInterceptor.setErrorNavigationCallback { errorMessage ->
                        navController.navigate(Screen.Error.route + "/" + errorMessage) {
                            // popUpTo(navController.graph.id) { inclusive = true }
                            // launchSingleTop = true
                        }
                    }
                }
                    AppNavigation()
                }
            }
        }
    }

}
