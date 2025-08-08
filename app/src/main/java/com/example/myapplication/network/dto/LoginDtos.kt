package com.example.myapplication.network.dto

data class LoginRequest(
    val USER_TEL: String,
    val USER_PW: String,
    val AUTO_LOGIN: String
)

data class LoginResponse(
    val id: String,
    val email: String,
    val name: String,
    val token: String
)

