package com.example.tokenizetest.ui.main

import android.content.Context
import android.graphics.drawable.Icon
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun EditText.hideSoftKeyboardOnFocusLostEnabled(enabled: Boolean) {
    val listener = if (enabled)
        OnFocusLostListener()
    else
        null
    onFocusChangeListener = listener
}
class OnFocusLostListener: View.OnFocusChangeListener {
    override fun onFocusChange(v: View, hasFocus: Boolean) {
        if (!hasFocus) {
            val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }
}

fun getIconFromResName(name: String, context: Context): Icon {
    var resID = 0
    try {
        resID = context.resources.getIdentifier(name, "drawable", context.packageName)
    } catch (e: Exception) {
        throw RuntimeException("No resource ID found for: "+ name)
    }
    return Icon.createWithResource(context, resID)

}