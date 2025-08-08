package com.example.myapplication

import android.app.Application
import com.example.myapplication.controller.AuthController
import com.example.myapplication.manager.UserManager
import com.example.myapplication.repository.AuthRepository
import com.example.myapplication.repository.AuthRepositoryFactory
import com.example.myapplication.utils.LogManager
import com.example.myapplication.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import android.util.Base64

private const val TAG = "MyApplication"

class MyApplication : Application() {
    private val appScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        initializeDependencies()
        attemptAutoLogin()
        LogManager.info(TAG, "MyApplication created")
    }

    private fun initializeDependencies() {
        // Repository 생성 (네트워크 기반)
        val authRepository: AuthRepository = AuthRepositoryFactory.create()
        
        // Controller 생성
        val authController = AuthController(authRepository)
        
        // UserManager 초기화
        UserManager.initialize(authController)
        
    }
    // 최초 앱 로그인 api 실행
    private fun attemptAutoLogin() {
        appScope.launch {
            val result: Result<User> = UserManager.login(Base64.encodeToString("03183921140".toByteArray(),  Base64.NO_WRAP),  Base64.encodeToString("000000".toByteArray(),  Base64.NO_WRAP))
            result.onSuccess { LogManager.auth(TAG, "auto_login", true) }
            result.onFailure { LogManager.auth(TAG, "auto_login", false) }
        }
    }
}
