package com.example.myapplication.network.dto.admin

data class AdminLoginRequest(
    val USER_TEL: String,
    val USER_PW: String,
    val AUTO_LOGIN: String
)

data class AdminLoginResponse(
    val user_uuid: String,
    val status_code: Int,
    val adminList: List<AdminOrgNetwork>
)

data class AdminOrgNetwork(
    val KINDERGARTEN_UUID: String,
    val KINDERGARTEN_NAME: String,
    val CLASS_CNT: Int,
    val PWD: String,
    val COMPANY_ID: String,
    val REGISTRATION_DATE: String
)


