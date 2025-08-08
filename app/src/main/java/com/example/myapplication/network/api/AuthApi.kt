package com.example.myapplication.network.api

import com.example.myapplication.network.dto.AdminLoginRequest
import com.example.myapplication.network.dto.AdminLoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("user/login_elf")
    suspend fun adminLogin(@Body body: AdminLoginRequest): AdminLoginResponse

    @POST("api/v1/auth/logout")
    suspend fun logout(): Unit
}

 