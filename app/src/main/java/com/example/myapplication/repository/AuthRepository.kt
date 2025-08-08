package com.example.myapplication.repository

import com.example.myapplication.model.Admin
import com.example.myapplication.model.AdminSession
import com.example.myapplication.model.AdminOrg
import com.example.myapplication.network.RetrofitProvider
import com.example.myapplication.network.api.AuthApi
import com.example.myapplication.network.dto.AdminLoginRequest
import com.example.myapplication.network.dto.AdminLoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import okhttp3.OkHttpClient

interface AuthRepository {
    suspend fun adminLogin(email: String, password: String): Result<Admin>
    suspend fun logout(): Result<Unit>
    suspend fun getCurrentAdmin(): Admin?
    suspend fun saveAdmin(admin: Admin)
    suspend fun clearUser()
    fun observeAdmin(): Flow<Admin?>
    suspend fun getAdminSession(): AdminSession?
    fun observeAdminSession(): Flow<AdminSession?>
}

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val tokenStore: TokenStore
) : AuthRepository {
    private val currentAdminState: MutableStateFlow<Admin?> = MutableStateFlow(null)
    private val adminSessionState: MutableStateFlow<AdminSession?> = MutableStateFlow(null)
    private companion object {
        private const val DEFAULT_ADMIN_NAME: String = "Administrator"
    }

    override suspend fun adminLogin(email: String, password: String): Result<Admin> {
        return try {
            // AUTO_LOGIN: 자동로그인 1, 수동로그인 2
            val response: AdminLoginResponse = authApi.adminLogin(
                AdminLoginRequest(USER_TEL = email, USER_PW = password, AUTO_LOGIN = "2")
            )

            // 세션 매핑 및 저장
            val orgs: List<AdminOrg> = response.adminList.map {
                AdminOrg(
                    orgUuid = it.KINDERGARTEN_UUID,
                    orgName = it.KINDERGARTEN_NAME,
                    classCount = it.CLASS_CNT,
                    password = it.PWD,
                    companyId = it.COMPANY_ID,
                    registrationDate = it.REGISTRATION_DATE
                )
            }
            val session = AdminSession(
                userUuid = response.user_uuid,
                statusCode = response.status_code,
                adminOrgs = orgs
            )
            adminSessionState.update { session }

            if (response.status_code == 0) {
                val admin = Admin(
                    id = response.user_uuid,
                    email = "",
                    name = DEFAULT_ADMIN_NAME,
                    isLoggedIn = true,
                    lastLoginTime = System.currentTimeMillis()
                )
                currentAdminState.update { admin }
                Result.success(admin)
            } else {
                Result.failure(IllegalStateException("Admin login failed: status_code=${response.status_code}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            authApi.logout()
            tokenStore.clearToken()
            currentAdminState.update { null }
            adminSessionState.update { null }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentAdmin(): Admin? {
        return currentAdminState.value
    }

    override suspend fun saveAdmin(admin: Admin) {
        currentAdminState.update { admin }
    }

    override suspend fun clearUser() {
        currentAdminState.update { null }
    }

    override fun observeAdmin(): Flow<Admin?> {
        return currentAdminState.asStateFlow()
    }

    override suspend fun getAdminSession(): AdminSession? {
        return adminSessionState.value
    }

    override fun observeAdminSession(): Flow<AdminSession?> {
        return adminSessionState.asStateFlow()
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
