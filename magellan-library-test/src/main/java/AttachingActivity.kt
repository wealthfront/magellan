package com.wealthfront.magellan

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.lifecycle.setContentScreen

public class AttachingActivity : ComponentActivity() {
  public companion object {
    public var step: Step<*>? = null
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentScreen(step!!)
  }
}