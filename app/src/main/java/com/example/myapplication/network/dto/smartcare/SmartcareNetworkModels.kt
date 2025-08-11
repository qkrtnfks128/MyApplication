package com.example.myapplication.network.dto.smartcare

data class UserListUsingPhoneNumberResponse(
    val status_code: Int,
    val data: List<UserListUsingPhoneNumber>,
    val status: String
)

data class UserListUsingPhoneNumber(
    val CHILDREN_NAME: String,
    val CHILDREN_CVID: String,
    val CHILDREN_IMG_URL: String,
    val PARENT_TEL: String,
    val REGISTRATION_DATE: String
)


