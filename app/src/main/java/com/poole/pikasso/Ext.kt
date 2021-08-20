package com.poole.pikasso

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.annotation.IdRes

inline fun <reified T : View> View.find(@IdRes id: Int) : T = findViewById(id)
inline fun <reified T : View> Activity.find(@IdRes id: Int): T = findViewById(id)
inline fun <reified T : View> T.click(crossinline block: (T) -> Unit) = setOnClickListener {block(it as T)}
inline fun <reified T : View> T.click(listener: View.OnClickListener) = setOnClickListener(listener)
fun Activity.customToast(message: String, length: Int = Toast.LENGTH_SHORT, block: Toast.() -> Unit) = Toast.makeText(this, message, length).apply(block).show()
fun Activity.toast(message: String, length: Int = Toast.LENGTH_SHORT) = this.customToast(message, length) {}
fun Activity.shortToast(message: String) = this.toast(message)
fun Activity.longToast(message: String) = this.toast(message, Toast.LENGTH_LONG)
fun Context.customToast(message: String, length: Int = Toast.LENGTH_SHORT, block: Toast.() -> Unit) = Toast.makeText(this, message, length).apply(block).show()
fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) = this.customToast(message, length) {}
fun Context.shortToast(message: String) = this.toast(message)
fun Context.longToast(message: String) = this.toast(message, Toast.LENGTH_LONG)
