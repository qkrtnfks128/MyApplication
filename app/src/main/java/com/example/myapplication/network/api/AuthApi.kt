package com.example.myapplication.network.api

import com.example.myapplication.network.dto.LoginRequest
import com.example.myapplication.network.dto.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("api/v1/auth/logout")
    suspend fun logout(): Unit
}

