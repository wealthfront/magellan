package com.wealthfront.magellan

import android.os.Bundle
import androidx.test.runner.AndroidJUnitRunner
import com.wealthfront.magellan.coroutines.ShownLifecycleScopeImpl
import com.wealthfront.magellan.init.Magellan

class SampleTestRunner : AndroidJUnitRunner() {

  override fun onStart() {
    Magellan.shownScopeProvider = { TestShownLifecycleScope() }
    super.onStart()
  }

  override fun finish(resultCode: Int, results: Bundle) {
    Magellan.shownScopeProvider = { ShownLifecycleScopeImpl() }
    super.finish(resultCode, results)
  }
}

