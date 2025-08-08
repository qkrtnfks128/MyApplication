package com.example.myapplication.manager

import com.example.myapplication.controller.AuthController
import com.example.myapplication.model.Admin
import com.example.myapplication.model.AdminSession
import com.example.myapplication.utils.LogManager
import kotlinx.coroutines.flow.StateFlow
import android.util.Base64

object AdminManager {
    private const val TAG: String = "AdminManager"
    private var authController: AuthController? = null
    
    fun initialize(authController: AuthController) {
        this.authController = authController
        LogManager.info(TAG, "AdminManager initialized")
    }
    
    fun getAuthController(): AuthController? {
        return authController
    }
    
    suspend fun adminLogin(email: String, password: String): Result<Admin> {
        return authController?.adminLogin(
            Base64.encodeToString(email.toByteArray(), Base64.NO_WRAP),
            Base64.encodeToString(password.toByteArray(), Base64.NO_WRAP)
        ) ?: Result.failure(IllegalStateException("AuthController not initialized"))
    }
    
    suspend fun logout(): Result<Unit> {
        return authController?.logout()
            ?: Result.failure(IllegalStateException("AuthController not initialized"))
    }
    
    suspend fun getCurrentAdmin(): Admin? {
        return authController?.getCurrentAdmin()
    }
    
    suspend fun updateAdmin(admin: Admin) {
        authController?.updateAdmin(admin)
    }
    
    fun observeAdmin(): StateFlow<Admin?>? {
        return authController?.currentAdmin
    }
    
    fun isLoggedIn(): Boolean {
        return authController?.isLoggedIn() ?: false
    }
    
    suspend fun getAdminSession(): AdminSession? {
        return authController?.getAdminSession()
    }
    
    fun observeAdminSession() = authController?.observeAdminSession()
}


