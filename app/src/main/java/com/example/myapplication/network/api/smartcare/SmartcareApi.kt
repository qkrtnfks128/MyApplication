package com.example.myapplication.network.api.smartcare

import com.example.myapplication.network.dto.smartcare.UserListUsingPhoneNumberResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SmartcareApi {
    // 사용자 인증 - 전화번호로 목록 조회
    @GET("smartcare/{customerCode}/check/{centerUuid}/{number}")
    suspend fun requestUserListUsingPhoneNumber(
        @Path("customerCode") customerCode: String,
        @Path("centerUuid") centerUuid: String,
        @Path("number") number: String
    ): Response<UserListUsingPhoneNumberResponse>
}


