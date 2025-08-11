package com.example.myapplication.manager

import android.util.Base64
import com.example.myapplication.model.Admin
import com.example.myapplication.model.AdminSession
import com.example.myapplication.repository.AuthRepository
import com.example.myapplication.utils.LogManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// AdminManager는 앱 전체에서 관리자 인증 및 세션 상태를 관리하는 싱글턴 객체입니다.
// AuthRepository를 직접 보유하며, 로그인/로그아웃/상태 관찰 기능을 제공합니다.
// 이메일과 비밀번호는 Base64로 인코딩되어 전달됩니다.

object AdminManager {
    private const val TAG: String = "AdminManager"
    private var authRepository: AuthRepository? = null

    private val currentAdminState: MutableStateFlow<Admin?> = MutableStateFlow(null)

    fun initialize(authRepository: AuthRepository) {
        this.authRepository = authRepository
        LogManager.info(TAG, "AdminManager initialized")
    }

    suspend fun adminLogin(email: String, password: String): Result<Admin> {
        val encodedEmail: String = Base64.encodeToString(email.toByteArray(), Base64.NO_WRAP)
        val encodedPassword: String = Base64.encodeToString(password.toByteArray(), Base64.NO_WRAP)
        val repo = authRepository ?: return Result.failure(IllegalStateException("AuthRepository not initialized"))
        val result = repo.adminLogin(encodedEmail, encodedPassword)
        result.onSuccess { admin -> currentAdminState.value = admin }
        return result
    }

    suspend fun logout(): Result<Unit> {
        val repo = authRepository ?: return Result.failure(IllegalStateException("AuthRepository not initialized"))
        val result = repo.logout()
        if (result.isSuccess) currentAdminState.value = null
        return result
    }

    suspend fun getCurrentAdmin(): Admin? {
        val repo = authRepository ?: return null
        return repo.getCurrentAdmin().also { admin -> currentAdminState.value = admin }
    }

    suspend fun updateAdmin(admin: Admin) {
        authRepository?.saveAdmin(admin)
        currentAdminState.value = admin
    }

    fun observeAdmin(): StateFlow<Admin?> {
        return currentAdminState.asStateFlow()
    }

    fun isLoggedIn(): Boolean {
        return currentAdminState.value?.isLoggedIn == true
    }

    suspend fun getAdminSession(): AdminSession? {
        return authRepository?.getAdminSession()
    }

    fun observeAdminSession() = authRepository?.observeAdminSession()
}
