package com.wealthfront.magellan

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

public object ContextUtil {
  public fun findActivity(context: Context): Activity {
    return when (context) {
      is Activity -> {
        context
      }
      is ContextWrapper -> {
        findActivity(context.baseContext)
      }
      else -> {
        throw IllegalStateException("Context must be Activity or wrap Activity")
      }
    }
  }
}
