package com.wealthfront.magellan

import android.os.Bundle
import androidx.test.runner.AndroidJUnitRunner
import com.wealthfront.magellan.coroutines.ShownLifecycleScope
import com.wealthfront.magellan.init.Magellan

class SampleTestRunner : AndroidJUnitRunner() {

  override fun onStart() {
    Magellan.shownScopeProvider = { TestShownLifecycleScope() }
    super.onStart()
  }

  override fun finish(resultCode: Int, results: Bundle) {
    Magellan.shownScopeProvider = { ShownLifecycleScope() }
    super.finish(resultCode, results)
  }
}

