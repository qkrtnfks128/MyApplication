package com.example.myapplication

import android.app.Application
import com.example.myapplication.controller.AuthController
import com.example.myapplication.manager.UserManager
import com.example.myapplication.repository.AuthRepository
import com.example.myapplication.repository.AuthRepositoryFactory
import com.example.myapplication.utils.LogManager

private const val TAG = "MyApplication"
class MyApplication : Application() {
    
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
        
        // UserManager 초기화
        UserManager.initialize(authController)
    }
}
