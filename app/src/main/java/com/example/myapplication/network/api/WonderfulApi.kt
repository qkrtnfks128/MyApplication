package com.example.myapplication.network.api.admin

import com.example.myapplication.network.dto.admin.AdminLoginRequest
import com.example.myapplication.network.dto.admin.AdminLoginResponse
import com.example.myapplication.network.dto.smartcare.UserListUsingPhoneNumberResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface WonderfulApi {
    // 관리자 로그인
    @POST("user/login_elf")
    suspend fun adminLogin(@Body body: AdminLoginRequest): Response<AdminLoginResponse>

      // 사용자 인증 - 전화번호로 목록 조회 - api 변경예정
      @GET("smartcare/{customerCode}/check/{centerUuid}/{number}")
      suspend fun requestUserListUsingPhoneNumber(
          @Path("customerCode") customerCode: String,
          @Path("centerUuid") centerUuid: String,
          @Path("number") number: String
      ): Response<UserListUsingPhoneNumberResponse>
}


