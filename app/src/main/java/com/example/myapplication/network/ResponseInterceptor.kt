package com.example.myapplication.network

import com.example.myapplication.utils.LogManager
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * ResponseErrorInterceptor는 모든 HTTP 응답에 대해 전역적으로 에러 처리를 담당합니다.
 */
class ResponseInterceptor : Interceptor {

    companion object {
        private const val TAG = "ResponseInterceptor"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val response = chain.proceed(chain.request())

            // HTTP 상태 코드 체크
            when {
                response.isSuccessful -> return response
                response.code in 400..499 -> {
                    val errorMessage = getClientErrorMessage(response.code)
                    LogManager.error(TAG, "Client Error: ${response.code} - $errorMessage")
                    throw ClientErrorException(response.code, errorMessage)
                }
                response.code in 500..599 -> {
                    val errorMessage = getServerErrorMessage(response.code)
                    LogManager.error(TAG, "Server Error: ${response.code} - $errorMessage")
                    throw ServerErrorException(response.code, errorMessage)
                }
                else -> {
                    val errorMessage = "Unexpected HTTP status: ${response.code}"
                    LogManager.error(TAG, errorMessage)
                    throw NetworkException(errorMessage)
                }
            }
        } catch (e: Exception) {
            when (e) {
                is ClientErrorException,
                is ServerErrorException,
                is NetworkException -> throw e

                is SocketTimeoutException -> {
                    val errorMessage = "요청 시간이 초과되었습니다. 네트워크 상태를 확인해주세요."
                    LogManager.error(TAG, "Timeout Error: $errorMessage")
                    throw NetworkException(errorMessage)
                }

                is UnknownHostException -> {
                    val errorMessage = "서버에 연결할 수 없습니다. 인터넷 연결을 확인해주세요."
                    LogManager.error(TAG, "Connection Error: $errorMessage")
                    throw NetworkException(errorMessage)
                }

                is IOException -> {
                    val errorMessage = "네트워크 통신 중 오류가 발생했습니다."
                    LogManager.error(TAG, "IO Error: $errorMessage", e)
                    throw NetworkException(errorMessage)
                }

                else -> {
                    val errorMessage = "알 수 없는 네트워크 오류가 발생했습니다."
                    LogManager.error(TAG, "Unknown Error: $errorMessage", e)
                    throw NetworkException(errorMessage)
                }
            }
        }
    }

    private fun getClientErrorMessage(code: Int): String {
        return when (code) {
            400 -> "잘못된 요청입니다. 입력 정보를 확인해주세요."
            401 -> "인증이 필요합니다. 로그인을 다시 시도해주세요."
            403 -> "접근 권한이 없습니다."
            404 -> "요청한 리소스를 찾을 수 없습니다."
            409 -> "요청이 충돌했습니다. 다시 시도해주세요."
            422 -> "요청 데이터가 유효하지 않습니다."
            else -> "클라이언트 오류가 발생했습니다. (코드: $code)"
        }
    }

    private fun getServerErrorMessage(code: Int): String {
        return when (code) {
            500 -> "서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요."
            502 -> "서버가 일시적으로 사용할 수 없습니다. 잠시 후 다시 시도해주세요."
            503 -> "서비스가 일시적으로 사용할 수 없습니다. 잠시 후 다시 시도해주세요."
            504 -> "서버 응답 시간이 초과되었습니다. 잠시 후 다시 시도해주세요."
            else -> "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요. (코드: $code)"
        }
    }
}

/**
 * 네트워크 에러를 나타내는 예외 클래스들
 */
open class NetworkException(message: String) : Exception(message)

class ClientErrorException(
    val statusCode: Int,
    message: String
) : NetworkException(message)

class ServerErrorException(
    val statusCode: Int,
    message: String
) : NetworkException(message)
