package com.wealthfront.magellan.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.wealthfront.magellan.core.Navigable
import com.wealthfront.magellan.lifecycle.setContentScreen

public class AttachingActivity : ComponentActivity() {
  public companion object {
    public var step: Navigable? = null
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentScreen(step!!)
  }
}