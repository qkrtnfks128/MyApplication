package com.example.myapplication.controller

import com.example.myapplication.model.Admin
import com.example.myapplication.model.AdminSession
import com.example.myapplication.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthController(
    private val authRepository: AuthRepository
) {
    private val _currentAdmin = MutableStateFlow<Admin?>(null)
    val currentAdmin: StateFlow<Admin?> = _currentAdmin.asStateFlow()
    
    suspend fun adminLogin(email: String, password: String): Result<Admin> {
        return authRepository.adminLogin(email, password).also { result ->
            result.onSuccess { admin ->
                _currentAdmin.value = admin
            }
        }
    }
    
    suspend fun logout(): Result<Unit> {
        return authRepository.logout().also { result ->
            if (result.isSuccess) {
                _currentAdmin.value = null
            }
        }
    }
    
    suspend fun getCurrentAdmin(): Admin? {
        return authRepository.getCurrentAdmin().also { admin ->
            _currentAdmin.value = admin
        }
    }
    
    suspend fun updateAdmin(admin: Admin) {
        authRepository.saveAdmin(admin)
        _currentAdmin.value = admin
    }
    
    fun observeAdmin(): Flow<Admin?> {
        return currentAdmin
    }
    
    fun isLoggedIn(): Boolean {
        return currentAdmin.value?.isLoggedIn == true
    }

    suspend fun getAdminSession(): AdminSession? {
        return authRepository.getAdminSession()
    }

    fun observeAdminSession(): Flow<AdminSession?> {
        return authRepository.observeAdminSession()
    }
}
