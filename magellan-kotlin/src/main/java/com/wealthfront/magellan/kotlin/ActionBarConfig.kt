package com.wealthfront.magellan.kotlin

import android.app.ActionBar
import android.support.annotation.ColorRes
import android.widget.Toolbar

/**
 * Used to configure the [ActionBar] or [Toolbar] in [NavigationListener.onNavigate]
 * with what the current Screen asked for.
 */
class ActionBarConfig private constructor(val visible: Boolean, val animated: Boolean, val colorRes: Int) {

  private constructor(builder: Builder) : this(builder.visible, builder.animated, builder.colorRes)

  companion object {
    fun with(init: Builder.() -> Unit) = Builder(init).build()
  }

  class Builder private constructor() {

    constructor(init: Builder.() -> Unit) : this() {
      init()
    }

    var visible: Boolean = false
    var animated: Boolean = false
    @ColorRes var colorRes: Int = 0

    fun visible(init: Builder.() -> Boolean) = apply { visible = init() }

    fun animated(init: Builder.() -> Boolean) = apply { animated = init() }

    fun colorRes(init: Builder.() -> Int) = apply { colorRes = init() }

    fun build() = ActionBarConfig(this)
  }

}