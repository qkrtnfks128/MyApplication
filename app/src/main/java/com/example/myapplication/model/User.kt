package com.example.myapplication.model

data class User(
    val id: String,
    val email: String,
    val name: String,
    val isLoggedIn: Boolean = false,
    val lastLoginTime: Long = 0L
)
