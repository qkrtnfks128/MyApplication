package com.example.myapplication.repository

import com.example.myapplication.model.AdminSession
import com.example.myapplication.model.AdminOrg
import com.example.myapplication.network.RetrofitProvider
import com.example.myapplication.network.api.admin.AdminAuthApi
import com.example.myapplication.network.dto.admin.AdminLoginRequest
import com.example.myapplication.network.dto.admin.AdminLoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import okhttp3.OkHttpClient
import retrofit2.Response

interface AuthRepository {
    suspend fun adminLogin(email: String, password: String): AdminSession
}

class AuthRepositoryImpl(
    private val authApi: AdminAuthApi,
) : AuthRepository {

    override suspend fun adminLogin(email: String, password: String): AdminSession {
            // AUTO_LOGIN: 자동로그인 1, 수동로그인 2
            val response: Response<AdminLoginResponse> = authApi.adminLogin(
                AdminLoginRequest(USER_TEL = email, USER_PW = password, AUTO_LOGIN = "2")
            )

            // ResponseErrorInterceptor에서 이미 에러 처리됨
            val body = response.body() ?: throw IllegalStateException("Empty response body")

            // 성공 처리
            val orgs: List<AdminOrg> = body.adminList.map {
                AdminOrg(
                    orgUuid = it.KINDERGARTEN_UUID,
                    orgName = it.KINDERGARTEN_NAME,
                    classCount = it.CLASS_CNT,
                    password = it.PWD,
                    companyId = it.COMPANY_ID,
                    registrationDate = it.REGISTRATION_DATE
                )
            }
           return AdminSession(
                userUuid = body.user_uuid,
                statusCode = body.status_code,
                adminOrgs = orgs
            )


    }

}



object AuthRepositoryFactory {
    fun create(): AuthRepositoryImpl {
        val client: OkHttpClient = RetrofitProvider.createOkHttpClient{null}
        val retrofit = RetrofitProvider.createRetrofit(client = client)
        val api = retrofit.create(AdminAuthApi::class.java)
        return AuthRepositoryImpl(api)
    }
}
