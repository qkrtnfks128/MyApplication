package com.example.myapplication.model

import java.io.Serializable

data class UserListItem(
    val name: String,
    val cvid: String,
    val imageUrl: String? = null,
    val parentTel: String? = null,
    val registrationDate: String? = null
) : Serializable

data class UserListResult(
    val statusCode: Int,
    val items: List<UserListItem>,
    val status: String = ""
) : Serializable
