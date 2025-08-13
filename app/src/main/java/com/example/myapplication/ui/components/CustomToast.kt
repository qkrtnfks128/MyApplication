package com.example.myapplication.ui.components

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

object CustomToast {
    fun show(context: Context, message: String) {
        val appCtx: Context = context.applicationContext
        val toast: Toast = Toast.makeText(appCtx, message, Toast.LENGTH_LONG)
        toast.duration = 5000 // 5초로 강제 설정 (단, 일부 기기에서는 duration이 3.5초로 제한될 수 있음)
        val root: View? = toast.view
        val textView: TextView? = when (root) {
            is TextView -> root
            is ViewGroup -> findTextView(root)
            else -> null
        }
        textView?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 45f)
        toast.show()
    }

    private fun findTextView(parent: ViewGroup): TextView? {
        for (i in 0 until parent.childCount) {
            val child: View = parent.getChildAt(i)
            if (child is TextView) return child
            if (child is ViewGroup) {
                val nested: TextView? = findTextView(child)
                if (nested != null) return nested
            }
        }
        return null
    }
}
