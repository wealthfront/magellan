package com.wealthfront.magellan.sample.migration

import android.provider.Settings.Global.ANIMATOR_DURATION_SCALE
import android.provider.Settings.Global.TRANSITION_ANIMATION_SCALE
import android.provider.Settings.Global.WINDOW_ANIMATION_SCALE
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

private const val TRANSITION_ANIMATION_SETTINGS = "settings put global $TRANSITION_ANIMATION_SCALE"
private const val WINDOW_ANIMATION_SETTINGS = "settings put global $WINDOW_ANIMATION_SCALE"
private const val ANIMATOR_ANIMATION_SETTINGS = "settings put global $ANIMATOR_DURATION_SCALE"
private const val DISABLE_KEYBOARD_SETTINGS = "settings put secure show_ime_with_hard_keyboard"

class DisableAnimationsAndKeyboardRule : TestRule {

  override fun apply(base: Statement, description: Description): Statement {
    return object : Statement() {
      override fun evaluate() {
        disableAnimationsAndKeyboard()
        try {
          base.evaluate()
        } finally {
          enableAnimationsAndKeyboard()
        }
      }
    }
  }

  private fun enableAnimationsAndKeyboard() {
    Log.v(
      DisableAnimationsAndKeyboardRule::class.java.simpleName,
      "Enabling animations and keyboard"
    )
    executeUiDeviceCommand("$TRANSITION_ANIMATION_SETTINGS 1")
    executeUiDeviceCommand("$WINDOW_ANIMATION_SETTINGS 1")
    executeUiDeviceCommand("$ANIMATOR_ANIMATION_SETTINGS 1")
    executeUiDeviceCommand("$DISABLE_KEYBOARD_SETTINGS 1")
  }

  private fun disableAnimationsAndKeyboard() {
    Log.v(
      DisableAnimationsAndKeyboardRule::class.java.simpleName,
      "Disabling animations and keyboard"
    )
    executeUiDeviceCommand("$TRANSITION_ANIMATION_SETTINGS 0")
    executeUiDeviceCommand("$WINDOW_ANIMATION_SETTINGS 0")
    executeUiDeviceCommand("$ANIMATOR_ANIMATION_SETTINGS 0")
    executeUiDeviceCommand("$DISABLE_KEYBOARD_SETTINGS 0")
  }

  private fun executeUiDeviceCommand(command: String) {
    UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).executeShellCommand(command)
  }
}
