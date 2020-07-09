package com.wealthfront.magellan.compose.view

import android.view.View
import androidx.annotation.IdRes
import kotlin.reflect.KProperty

fun <T : View> View.bindView(@IdRes res: Int): MutableBindViewDelegate<T> {
  return MutableBindViewDelegate(lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(res)!! })
}

class MutableBindViewDelegate<T>(private val lazy: Lazy<T>) {

  private var value: T? = null

  operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
    return value ?: lazy.getValue(thisRef, property)
  }

  operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
    this.value = value
  }
}