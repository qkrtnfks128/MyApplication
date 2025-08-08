package com.example.myapplication.network.dto

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val id: String,
    val email: String,
    val name: String,
    val token: String
)

