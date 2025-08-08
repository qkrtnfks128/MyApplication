package com.example.myapplication.network

import okhttp3.Interceptor
import okhttp3.Response
import com.example.myapplication.utils.LogManager

class LoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (NetworkConfig.isDebug()) {
            LogManager.debug("HTTP", "→ ${'$'}{request.method} ${'$'}{request.url}")
            request.headers.forEach { header ->
                LogManager.debug("HTTP", "→ Header: ${'$'}{header.first}: ${'$'}{header.second}")
            }
            request.body?.let { body ->
                LogManager.debug("HTTP", "→ Body Content-Type: ${'$'}{body.contentType()}")
            }
        }

        val response = chain.proceed(request)

        if (NetworkConfig.isDebug()) {
            LogManager.debug("HTTP", "← ${'$'}{response.code} ${'$'}{response.message} for ${'$'}{response.request.url}")
        }
        return response
    }
}

