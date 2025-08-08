package com.example.myapplication.utils

import android.util.Log

/**
 * 앱 전체에서 사용할 공통 로그 매니저
 * 디버그/릴리즈 빌드에 따라 로그 레벨을 조정하고,
 * 태그를 통한 로그 분류를 제공합니다.
 */
object LogManager {
    
    private const val TAG_PREFIX = "MyApp"
    
    /**
     * 디버그 로그 출력
     * @param tag 로그 태그 (클래스명 권장)
     * @param message 로그 메시지
     */
    fun debug(tag: String, message: String) {
       
            Log.d("$TAG_PREFIX-$tag", message)
        
    }
    
    /**
     * 정보 로그 출력
     * @param tag 로그 태그 (클래스명 권장)
     * @param message 로그 메시지
     */
    fun info(tag: String, message: String) {
        Log.i("$TAG_PREFIX-$tag", message)
    }
    
    /**
     * 경고 로그 출력
     * @param tag 로그 태그 (클래스명 권장)
     * @param message 로그 메시지
     */
    fun warning(tag: String, message: String) {
        Log.w("$TAG_PREFIX-$tag", message)
    }
    
    /**
     * 에러 로그 출력
     * @param tag 로그 태그 (클래스명 권장)
     * @param message 로그 메시지
     * @param throwable 예외 객체 (선택사항)
     */
    fun error(tag: String, message: String, throwable: Throwable? = null) {
        Log.e("$TAG_PREFIX-$tag", message, throwable)
    }
    
    /**
     * 사용자 액션 로그 출력 (버튼 클릭, 화면 이동 등)
     * @param tag 로그 태그 (클래스명 권장)
     * @param action 액션 설명
     */
    fun userAction(tag: String, action: String) {
      
            Log.d("$TAG_PREFIX-$tag", "사용자 액션: $action")
      
    }
    
    /**
     * 네비게이션 로그 출력
     * @param tag 로그 태그 (클래스명 권장)
     * @param from 현재 화면
     * @param to 이동할 화면
     */
    fun navigation(tag: String, from: String, to: String) {
      
            Log.d("$TAG_PREFIX-$tag", "네비게이션: $from -> $to")
       
    }
    
    /**
     * 인증 관련 로그 출력
     * @param tag 로그 태그 (클래스명 권장)
     * @param action 인증 액션 (로그인, 로그아웃, 회원가입 등)
     * @param success 성공 여부
     */
    fun auth(tag: String, action: String, success: Boolean) {
        val status = if (success) "성공" else "실패"
        Log.i("$TAG_PREFIX-$tag", "인증 $action: $status")
    }
}
