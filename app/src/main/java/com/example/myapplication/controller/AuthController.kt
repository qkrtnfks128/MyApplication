package com.example.myapplication.controller

import com.example.myapplication.model.User
import com.example.myapplication.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthController(
    private val authRepository: AuthRepository
) {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    suspend fun adminLogin(email: String, password: String): Result<User> {
        return authRepository.adminLogin(email, password).also { result ->
            result.onSuccess { user ->
                _currentUser.value = user
            }
        }
    }
    
    suspend fun logout(): Result<Unit> {
        return authRepository.logout().also { result ->
            if (result.isSuccess) {
                _currentUser.value = null
            }
        }
    }
    
    suspend fun getCurrentUser(): User? {
        return authRepository.getCurrentUser().also { user ->
            _currentUser.value = user
        }
    }
    
    suspend fun updateUser(user: User) {
        authRepository.saveUser(user)
        _currentUser.value = user
    }
    
    fun observeUser(): Flow<User?> {
        return currentUser
    }
    
    fun isLoggedIn(): Boolean {
        return currentUser.value?.isLoggedIn == true
    }
}
