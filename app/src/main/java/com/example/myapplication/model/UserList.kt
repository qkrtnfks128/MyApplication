package com.example.myapplication.model

data class UserListItem(
    val name: String,
    val cvid: String,
    val imageUrl: String,
    val parentTel: String,
    val registrationDate: String
)

data class UserListResult(
    val statusCode: Int,
    val items: List<UserListItem>,
    val status: String
)
