package com.example.myapplication

import android.app.Application
import com.example.myapplication.manager.AdminManager
import com.example.myapplication.manager.SelectedOrgStore
import com.example.myapplication.manager.SelectedUserStore
import com.example.myapplication.manager.SelectedMeasurementStore
import com.example.myapplication.repository.AuthRepository
import com.example.myapplication.repository.AuthRepositoryFactory
import com.example.myapplication.utils.LogManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner

private const val TAG = "MyApplication"

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initializeDependencies()
        LogManager.info(TAG, "MyApplication created")
        ProcessLifecycleOwner.get().lifecycle.addObserver(
            AppLifecycleObserver(
                onForeground = { LogManager.info("APP", "foreground") },
                onBackground = {
                    SelectedUserStore.clear()
                    SelectedMeasurementStore.clear()
                    LogManager.info("APP", "background") }
            )
        )
    }

    // 선택: 메모리 신호(홈키 등)도 함께 잡고 싶다면
//override fun onTrimMemory(level: Int) {
//  super.onTrimMemory(level)
//  if (level == TRIM_MEMORY_UI_HIDDEN) {
//      LogManager.info("APP", "UI hidden (background)")
//  }
//}

    private fun initializeDependencies() {
        val authRepository: AuthRepository = AuthRepositoryFactory.create()
        AdminManager.initialize(authRepository)
        SelectedOrgStore.initialize(this)
        SelectedUserStore.initialize()
        SelectedMeasurementStore.initialize()
    }
    // 자동 로그인은 SplashScreen에서 처리합니다.
}

class AppLifecycleObserver(
    private val onForeground: () -> Unit,
    private val onBackground: () -> Unit
) : DefaultLifecycleObserver {
    override fun onStart(owner: LifecycleOwner) { onForeground() }   // 앱이 포그라운드로
    override fun onStop(owner: LifecycleOwner) { onBackground() }   // 앱이 백그라운드로
}


