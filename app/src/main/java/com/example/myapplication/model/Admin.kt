package com.example.myapplication.model

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


