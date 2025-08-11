package com.example.myapplication.manager

import com.example.myapplication.model.UserListItem

// SelectedUserStore는 선택된 UserListItem(사용자) 정보를 메모리에 저장하고 불러오는 역할을 합니다.
// 앱 전체에서 선택된 사용자 정보를 관리하며, 일시적으로만 보관합니다.
// initialize(): 선택된 사용자 정보를 초기화합니다. (null로 설정)
// save(user): 선택한 UserListItem을 저장합니다.
// get(): 저장된 UserListItem 정보를 반환합니다. 없으면 null을 반환합니다.
// clear(): 저장된 사용자 정보를 삭제합니다.

object SelectedUserStore {
    @Volatile private var selected: UserListItem? = null

    fun initialize() {
        selected = null
    }

    fun save(user: UserListItem) {
        selected = user
    }

    fun get(): UserListItem? = selected

    fun clear() { selected = null }
}


