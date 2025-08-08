package com.example.myapplication.manager

import com.example.myapplication.controller.AuthController
import com.example.myapplication.model.User
import com.example.myapplication.utils.LogManager
import kotlinx.coroutines.flow.StateFlow

object UserManager {
  private const val TAG = "UserManager"
  
    private var authController: AuthController? = null
    
    fun initialize(authController: AuthController) {
        this.authController = authController
        LogManager.info(TAG, "UserManager initialized")
    }
    
    fun getAuthController(): AuthController? {
        return authController
    }
    
    suspend fun login(email: String, password: String): Result<User> {
        return authController?.login(email, password) 
            ?: Result.failure(IllegalStateException("AuthController not initialized"))
    }
    
    suspend fun logout(): Result<Unit> {
        return authController?.logout() 
            ?: Result.failure(IllegalStateException("AuthController not initialized"))
    }
    
    suspend fun getCurrentUser(): User? {
        return authController?.getCurrentUser()
    }
    
    suspend fun updateUser(user: User) {
        authController?.updateUser(user)
    }
    
    fun observeUser(): StateFlow<User?>? {
        return authController?.currentUser
    }
    
    fun isLoggedIn(): Boolean {
        return authController?.isLoggedIn() ?: false
    }
}
