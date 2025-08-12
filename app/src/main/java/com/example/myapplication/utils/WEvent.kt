package com.example.myapplication.utils

import androidx.lifecycle.Observer

open class WEvent<out T>(private val content: T) {
    var hasBeenHandled: Boolean = false
        private set

    fun getContentIfNotHandled(): T? {
        if (hasBeenHandled) return null
        hasBeenHandled = true
        return content
    }

    fun peekContent(): T = content
}

class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<WEvent<T>> {
    override fun onChanged(event: WEvent<T>) {
        event.getContentIfNotHandled()?.let(onEventUnhandledContent)
    }
}