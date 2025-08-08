package com.example.myapplication.repository

import com.example.myapplication.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun logout(): Result<Unit>
    suspend fun getCurrentUser(): User?
    suspend fun saveUser(user: User)
    suspend fun clearUser()
    fun observeUser(): Flow<User?>
}

class AuthRepositoryImpl : AuthRepository {
    private var currentUser: User? = null
    
    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            // TODO: 실제 API 호출로 대체
            val user = User(
                id = "user_${System.currentTimeMillis()}",
                email = email,
                name = "사용자",
                isLoggedIn = true,
                lastLoginTime = System.currentTimeMillis()
            )
            currentUser = user
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun logout(): Result<Unit> {
        return try {
            currentUser = null
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getCurrentUser(): User? {
        return currentUser
    }
    
    override suspend fun saveUser(user: User) {
        currentUser = user
    }
    
    override suspend fun clearUser() {
        currentUser = null
    }
    
    override fun observeUser(): Flow<User?> {
        // TODO: 실제 Flow 구현으로 대체
        return kotlinx.coroutines.flow.flowOf(currentUser)
    }
}
