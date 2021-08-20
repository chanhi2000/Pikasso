package com.poole.pikasso

import android.app.Activity
import android.view.View
import android.widget.Toast
import androidx.annotation.IdRes

inline fun <reified T : View> View.find(@IdRes id: Int) : T = findViewById(id)
inline fun <reified T : View> Activity.find(@IdRes id: Int): T = findViewById(id)
inline fun <reified T : View> T.click(crossinline block: (T) -> Unit) = setOnClickListener {block(it as T)}
inline fun <reified T : View> T.click(listener: View.OnClickListener) = setOnClickListener(listener)
fun Activity.toast(message: String, length: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, message, length).show()
fun Activity.shortToast(message: String) = this.toast(message)
fun Activity.longToast(message: String) = this.toast(message, Toast.LENGTH_LONG)
