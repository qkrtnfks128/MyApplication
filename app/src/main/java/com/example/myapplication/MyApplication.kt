package com.example.myapplication

import android.app.Application
import com.example.myapplication.controller.AuthController
import com.example.myapplication.manager.AdminManager
import com.example.myapplication.repository.AuthRepository
import com.example.myapplication.repository.AuthRepositoryFactory
import com.example.myapplication.utils.LogManager
import com.example.myapplication.model.Admin
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
        LogManager.info(TAG, "MyApplication created")
    }

    private fun initializeDependencies() {
        // Repository 생성 (네트워크 기반)
        val authRepository: AuthRepository = AuthRepositoryFactory.create()
        
        // Controller 생성
        val authController = AuthController(authRepository)
        
        // AdminManager 초기화
        AdminManager.initialize(authController)
        
    }
    // 자동 로그인은 SplashScreen에서 처리합니다.
}
