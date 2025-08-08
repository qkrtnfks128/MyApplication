package com.example.myapplication.manager

import android.content.Context
import android.content.SharedPreferences
import com.example.myapplication.model.AdminOrg
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

// SelectedOrgStore는 선택된 AdminOrg(기관) 정보를 SharedPreferences에 저장하고 불러오는 역할을 합니다.
// 앱 전체에서 선택된 기관 정보를 관리하며, Moshi를 사용해 AdminOrg 객체를 JSON 문자열로 변환하여 저장합니다.
// initialize(context): SharedPreferences를 초기화합니다. 반드시 Application 시작 시 호출해야 합니다.
// saveSelected(org): 선택한 AdminOrg를 JSON으로 변환해 저장합니다.
// getSelected(): 저장된 AdminOrg 정보를 불러옵니다. 없으면 null을 반환합니다.
// clear(): 저장된 기관 정보를 삭제합니다.

object SelectedOrgStore {
    private const val PREF_NAME: String = "selected_admin_org_prefs"
    private const val KEY_SELECTED: String = "selected_org_json"

    private lateinit var preferences: SharedPreferences
    private val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val adapter = moshi.adapter(AdminOrg::class.java)

    fun initialize(context: Context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveSelected(org: AdminOrg) {
        val json: String = adapter.toJson(org)
        preferences.edit().putString(KEY_SELECTED, json).apply()
    }

    fun getSelected(): AdminOrg? {
        val json: String? = preferences.getString(KEY_SELECTED, null)
        return json?.let { adapter.fromJson(it) }
    }

    fun clear() {
        preferences.edit().remove(KEY_SELECTED).apply()
    }
}


