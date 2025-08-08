package com.example.myapplication.model

data class Admin(
    val id: String,
    val email: String,
    val name: String,
    val isLoggedIn: Boolean = false,
    val lastLoginTime: Long = 0L
)

data class AdminSession(
    val userUuid: String,
    val statusCode: Int,
    val adminOrgs: List<AdminOrg>
)

data class AdminOrg(
    val orgUuid: String,
    val orgName: String,
    val classCount: Int,
    val password: String,
    val companyId: String,
    val registrationDate: String
)


