package com.example.myapplication.repository

import com.example.myapplication.model.UserListItem
import com.example.myapplication.model.UserListResult
import com.example.myapplication.network.RetrofitProvider
import com.example.myapplication.network.api.smartcare.SmartcareApi
import com.example.myapplication.network.dto.smartcare.UserListUsingPhoneNumber
import com.example.myapplication.network.dto.smartcare.UserListUsingPhoneNumberResponse
import okhttp3.OkHttpClient

interface SmartCareRepository {
    suspend fun getUserListUsingPhoneNumber(
        customerCode: String,
        centerUuid: String,
        number: String
    ): Result<UserListResult>
}

class SmartCareRepositoryImpl(
    private val api: SmartcareApi
) : SmartCareRepository {

    override suspend fun getUserListUsingPhoneNumber(
        customerCode: String,
        centerUuid: String,
        number: String
    ): Result<UserListResult> {
        return try {
            val response = api.requestUserListUsingPhoneNumber(customerCode, centerUuid, number)
            if (response.isSuccessful) {
                val body: UserListUsingPhoneNumberResponse? = response.body()
                if (body != null) {
                    val items: List<UserListItem> = body.data.map { it.toDomain() }
                    Result.success(
                        UserListResult(
                            statusCode = body.status_code,
                            items = items,
                        )
                    )
                } else {
                    Result.failure(IllegalStateException("Empty response body"))
                }
            } else {
                Result.failure(IllegalStateException("HTTP ${'$'}{response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

private fun UserListUsingPhoneNumber.toDomain(): UserListItem =
    UserListItem(
        name = CHILDREN_NAME,
        cvid = CHILDREN_CVID,
        imageUrl = CHILDREN_IMG_URL,
        parentTel = PARENT_TEL,
        registrationDate = REGISTRATION_DATE
    )

object SmartCareRepositoryFactory {
    fun create(): SmartCareRepositoryImpl {
        val client: OkHttpClient = RetrofitProvider.createOkHttpClient { null }
        val retrofit = RetrofitProvider.createRetrofit(client = client)
        val api = retrofit.create(SmartcareApi::class.java)
        return SmartCareRepositoryImpl(api)
    }
}

