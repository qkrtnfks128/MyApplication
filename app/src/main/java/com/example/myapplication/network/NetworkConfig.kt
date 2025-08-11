package com.example.myapplication.network

import com.example.myapplication.BuildConfig

object NetworkConfig {
    const val NETWORK_TIMEOUT_SECONDS: Long = 30
    const val HEADER_AUTHORIZATION: String = "Authorization"
    const val HEADER_CONTENT_TYPE: String = "Content-Type"
    const val MEDIA_TYPE_JSON: String = "application/json"
    const val CUSTOMER_CODE: String = "namhae"

    // Environment base URLs
    private const val PROD_BASE_URL: String = "http://35.241.46.53/"
    private const val DEV_BASE_URL: String = "http://34.93.223.59:9001/"

    // External services
    const val SOUND_URL: String = "https://channel.dasomi.ai/"
    const val INBI_APP_STORE_URL: String = "https://web.inbiappstore.com/"
    const val ISENS_BASE_URL: String = "https://www.ycsmartcity.net/api/link/ai/"

    fun baseUrl(): String =PROD_BASE_URL
    // fun baseUrl(): String = if (!BuildConfig.DEBUG) PROD_BASE_URL else DEV_BASE_URL
    fun isDebug(): Boolean = BuildConfig.DEBUG
}

