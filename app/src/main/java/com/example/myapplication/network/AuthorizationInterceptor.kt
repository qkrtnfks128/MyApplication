package com.example.myapplication.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(
    private val tokenProvider: () -> String?
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = tokenProvider()
        val builder = original.newBuilder()
        if (!token.isNullOrBlank()) {
            builder.addHeader(NetworkConfig.HEADER_AUTHORIZATION, "Bearer ${'$'}token")
        }
        builder.addHeader(NetworkConfig.HEADER_CONTENT_TYPE, NetworkConfig.MEDIA_TYPE_JSON)
        return chain.proceed(builder.build())
    }
}

