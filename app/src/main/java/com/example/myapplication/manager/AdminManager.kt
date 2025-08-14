package com.example.myapplication.manager

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import com.example.myapplication.model.AdminSession
import com.example.myapplication.repository.AuthRepository
import com.example.myapplication.repository.AuthRepositoryFactory
import com.example.myapplication.utils.LogManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// AdminManager는 앱 전체에서 관리자 인증 및 세션 상태를 관리하는 싱글턴 객체입니다.
// AuthRepository를 직접 보유하며, 로그인/로그아웃/상태 관찰 기능을 제공합니다.
// 이메일과 비밀번호는 Base64로 인코딩되어 전달됩니다.
// 세션 정보는 SharedPreferences에 저장되어 앱 재시작 시에도 유지됩니다.

object AdminManager {
    private const val TAG: String = "AdminManager"
    private const val PREF_NAME: String = "admin_session_prefs"
    private const val KEY_ADMIN_SESSION: String = "admin_session_json"

    private var authRepository: AuthRepository = AuthRepositoryFactory.create()
    private var adminSession: AdminSession? = null

    // StateFlow for observing session changes
    private val _sessionState = MutableStateFlow<AdminSession?>(null)

    private lateinit var preferences: SharedPreferences
    private val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val sessionAdapter = moshi.adapter(AdminSession::class.java)

    fun initialize(context: Context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        // 저장된 세션 정보가 있다면 복원
        restoreSession()

        LogManager.info(TAG, "AdminManager initialized")
    }

    private fun restoreSession() {
        try {
            val savedSession = getSavedSession()
            if (savedSession != null) {
                adminSession = savedSession
                _sessionState.value = savedSession
                LogManager.info(TAG, "Admin session restored from storage")
            }
        } catch (e: Exception) {
            LogManager.error(TAG, "Failed to restore admin session: ${e.message}")
            clearSavedSession()
        }
    }

    private fun getSavedSession(): AdminSession? {
        if (!::preferences.isInitialized) return null

        val json: String? = preferences.getString(KEY_ADMIN_SESSION, null)
        return json?.let { sessionAdapter.fromJson(it) }
    }

    private fun saveSessionToStorage(session: AdminSession) {
        if (!::preferences.isInitialized) return

        try {
            val json: String = sessionAdapter.toJson(session)
            preferences.edit().putString(KEY_ADMIN_SESSION, json).apply()
            LogManager.info(TAG, "Admin session saved to storage")
        } catch (e: Exception) {
            LogManager.error(TAG, "Failed to save admin session: ${e.message}")
        }
    }

    private fun clearSavedSession() {
        if (!::preferences.isInitialized) return

        preferences.edit().remove(KEY_ADMIN_SESSION).apply()
        LogManager.info(TAG, "Admin session cleared from storage")
    }

    suspend fun adminLogin(email: String, password: String): AdminSession {
        val encodedEmail: String = Base64.encodeToString(email.toByteArray(), Base64.NO_WRAP)
        val encodedPassword: String = Base64.encodeToString(password.toByteArray(), Base64.NO_WRAP)

        val result: AdminSession = authRepository.adminLogin(encodedEmail, encodedPassword)

        // 메모리에 세션 저장
        adminSession = result
        _sessionState.value = result

        // 세션 스토리지에 저장
        saveSessionToStorage(result)

        LogManager.info(TAG, "Admin login successful, session saved")
        return result
    }

    fun logout() {
        // 메모리에서 세션 제거
        adminSession = null
        _sessionState.value = null

        // 세션 스토리지에서 제거
        clearSavedSession()

        LogManager.info(TAG, "Admin logged out, session cleared")
    }

    fun getCurrentSession(): AdminSession? = adminSession

    fun isLoggedIn(): Boolean = adminSession != null

    // StateFlow observer
    fun observeAdminSession(): StateFlow<AdminSession?> = _sessionState.asStateFlow()
}
