package com.example.myapplication

import android.app.Application
import com.example.myapplication.manager.AdminManager
import com.example.myapplication.manager.SelectedOrgStore
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
        val authRepository: AuthRepository = AuthRepositoryFactory.create()
        AdminManager.initialize(authRepository)
        SelectedOrgStore.initialize(this)
    }
    // 자동 로그인은 SplashScreen에서 처리합니다.
}
