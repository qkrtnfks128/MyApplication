package com.example.myapplication.network.api.admin

import com.example.myapplication.network.dto.admin.AdminLoginRequest
import com.example.myapplication.network.dto.admin.AdminLoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AdminAuthApi {
    // 관리자 로그인
    @POST("user/login_elf")
    suspend fun adminLogin(@Body body: AdminLoginRequest): Response<AdminLoginResponse>
}


