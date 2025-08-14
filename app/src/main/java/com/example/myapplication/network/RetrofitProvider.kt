package com.example.myapplication.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import com.example.myapplication.utils.LogManager

/**
 * RetrofitProvider는 Retrofit 및 OkHttpClient 인스턴스 생성을 담당합니다.
 * - createOkHttpClient: 인증 토큰을 주입하는 인터셉터와 로깅 인터셉터를 포함한 OkHttpClient를 생성합니다.
 * - createRetrofit: Moshi 컨버터와 지정된 baseUrl, OkHttpClient를 사용하여 Retrofit 인스턴스를 생성합니다.
 *
 * 인증 헤더, 타임아웃, 로깅 등 네트워크 공통 설정을 일관되게 관리합니다.
 *
 * 사용 예시:
 * val client = RetrofitProvider.createOkHttpClient { tokenStore.getToken() }
 * val retrofit = RetrofitProvider.createRetrofit(client = client)
 */


object RetrofitProvider {
    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    fun createOkHttpClient(
        tokenProvider: () -> String?
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor { message ->
            LogManager.debug("HTTP", message) // MyApp-HTTP 태그로 출력
        }.apply {
            level = if (NetworkConfig.isDebug())
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        }

        return OkHttpClient.Builder()
            .connectTimeout(NetworkConfig.NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(NetworkConfig.NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(NetworkConfig.NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(AuthorizationInterceptor(tokenProvider))
            .addInterceptor(ResponseInterceptor())
            .addInterceptor(logging) // 여기서 LogManager로 라우팅됨
            .build()
    }

    fun createRetrofit(
        baseUrl: String = NetworkConfig.baseUrl(),
        client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}

