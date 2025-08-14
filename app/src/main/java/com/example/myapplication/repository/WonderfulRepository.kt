package com.example.myapplication.repository

import com.example.myapplication.model.AdminSession
import com.example.myapplication.model.AdminOrg
import com.example.myapplication.model.UserListItem
import com.example.myapplication.model.UserListResult
import com.example.myapplication.network.RetrofitProvider
import com.example.myapplication.network.api.admin.WonderfulApi
import com.example.myapplication.network.dto.admin.AdminLoginRequest
import com.example.myapplication.network.dto.admin.AdminLoginResponse
import com.example.myapplication.network.dto.smartcare.UserListUsingPhoneNumberResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import okhttp3.OkHttpClient
import retrofit2.Response

interface WonderfulRepository {
    suspend fun adminLogin(email: String, password: String): AdminSession
    suspend fun getUserListUsingPhoneNumber(
        customerCode: String,
        centerUuid: String,
        number: String
    ): UserListResult
}

class WonderfulRepositoryImpl(
    private val wonderfulApi: WonderfulApi,
) : WonderfulRepository {

    override suspend fun adminLogin(email: String, password: String): AdminSession {
            // AUTO_LOGIN: 자동로그인 1, 수동로그인 2
            val response: Response<AdminLoginResponse> = wonderfulApi.adminLogin(
                AdminLoginRequest(USER_TEL = email, USER_PW = password, AUTO_LOGIN = "2")
            )

            // ResponseErrorInterceptor에서 이미 에러 처리됨
            val body = response.body() ?: throw IllegalStateException("Empty response body")

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

    override suspend fun getUserListUsingPhoneNumber(
        customerCode: String,
        centerUuid: String,
        number: String
    ): UserListResult {
        val response: Response<UserListUsingPhoneNumberResponse> = wonderfulApi.requestUserListUsingPhoneNumber(customerCode, centerUuid, number)

                val body: UserListUsingPhoneNumberResponse = response.body() ?: throw IllegalStateException("Empty response body")

                    val items: List<UserListItem> = body.data.map {
                        UserListItem(
                            name = it.CHILDREN_NAME,
                            cvid = it.CHILDREN_CVID,
                            imageUrl = it.CHILDREN_IMG_URL,
                            parentTel = it.PARENT_TEL,
                            registrationDate = it.REGISTRATION_DATE
                        )
                     }
                     return UserListResult(
                        statusCode = body.status_code,
                        items = items,
                     )



    }

}



object WonderfulRepositoryFactory {
    fun create(): WonderfulRepositoryImpl {
        val client: OkHttpClient = RetrofitProvider.createOkHttpClient{null}
        val retrofit = RetrofitProvider.createRetrofit(client = client)
        val api = retrofit.create(WonderfulApi::class.java)
        return WonderfulRepositoryImpl(api)
    }
}
