package com.example.myapplication.network

import com.example.myapplication.BuildConfig

object NetworkConfig {
    const val NETWORK_TIMEOUT_SECONDS: Long = 30
    const val HEADER_AUTHORIZATION: String = "Authorization"
    const val HEADER_CONTENT_TYPE: String = "Content-Type"
    const val MEDIA_TYPE_JSON: String = "application/json"
    const val BASE_URL: String = "https://example.com/" // TODO: 환경에 맞게 수정

    fun isDebug(): Boolean = BuildConfig.DEBUG
}

