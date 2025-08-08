package com.example.myapplication.repository

import com.example.myapplication.model.User
import com.example.myapplication.network.RetrofitProvider
import com.example.myapplication.network.api.AuthApi
import com.example.myapplication.network.dto.LoginRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import okhttp3.OkHttpClient

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun logout(): Result<Unit>
    suspend fun getCurrentUser(): User?
    suspend fun saveUser(user: User)
    suspend fun clearUser()
    fun observeUser(): Flow<User?>
}

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val tokenStore: TokenStore
) : AuthRepository {
    private val currentUserState: MutableStateFlow<User?> = MutableStateFlow(null)

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val response = authApi.login(LoginRequest(email = email, password = password))
            tokenStore.saveToken(response.token)
            val user = User(
                id = response.id,
                email = response.email,
                name = response.name,
                isLoggedIn = true,
                lastLoginTime = System.currentTimeMillis()
            )
            currentUserState.update { user }
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            authApi.logout()
            tokenStore.clearToken()
            currentUserState.update { null }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): User? {
        return currentUserState.value
    }

    override suspend fun saveUser(user: User) {
        currentUserState.update { user }
    }

    override suspend fun clearUser() {
        currentUserState.update { null }
    }

    override fun observeUser(): Flow<User?> {
        return currentUserState.asStateFlow()
    }
}

interface TokenStore {
    fun getToken(): String?
    fun saveToken(token: String)
    fun clearToken()
}

class InMemoryTokenStore : TokenStore {
    @Volatile private var token: String? = null
    override fun getToken(): String? = token
    override fun saveToken(token: String) { this.token = token }
    override fun clearToken() { token = null }
}

object AuthRepositoryFactory {
    fun create(): AuthRepositoryImpl {
        val tokenStore: TokenStore = InMemoryTokenStore()
        val client: OkHttpClient = RetrofitProvider.createOkHttpClient(tokenProvider = tokenStore::getToken)
        val retrofit = RetrofitProvider.createRetrofit(client = client)
        val api = retrofit.create(AuthApi::class.java)
        return AuthRepositoryImpl(api, tokenStore)
    }
}
