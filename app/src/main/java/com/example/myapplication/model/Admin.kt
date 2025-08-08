package com.example.myapplication.model


data class AdminSession(
    val userUuid: String,
    val statusCode: Int,
    val adminKindergartens: List<AdminKindergarten>
)

data class AdminKindergarten(
    val kindergartenUuid: String,
    val kindergartenName: String,
    val classCount: Int,
    val password: String,
    val companyId: String,
    val registrationDate: String
)


