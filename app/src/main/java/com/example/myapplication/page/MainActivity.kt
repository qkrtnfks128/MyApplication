package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.manager.AdminManager
import com.example.myapplication.navigation.AppNavigation
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.utils.LogManager
import kotlinx.coroutines.launch

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
        checkLoginStatus()
        
        setContent {
            MyApplicationTheme {
                AppNavigation()
            }
        }
    }
    
    private fun checkLoginStatus() {
        lifecycleScope.launch {
            val currentAdmin = AdminManager.getCurrentAdmin()
            if (currentAdmin == null) {
                // 로그인되지 않은 경우 처리
                // TODO: 로그인 화면으로 이동하거나 자동 로그인 시도
                LogManager.info(TAG, "사용자가 로그인되지 않았습니다.")
            } else {
                LogManager.info(TAG, "현재 로그인된 관리자: ${currentAdmin.name}")
            }
        }
    }
}