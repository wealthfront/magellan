package com.wealthfront.magellan

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.idling.CountingIdlingResource
import com.wealthfront.magellan.sample.migration.api.CallEventListener
import com.wealthfront.magellan.sample.migration.api.RequestCountingInterceptor
import org.junit.rules.ExternalResource

class CoroutineIdlingRule : ExternalResource() {
  private val coroutineCountingIdlingResource = CountingIdlingResource("coroutine")

  override fun after() {
    IdlingRegistry.getInstance().unregister(coroutineCountingIdlingResource)
    RequestCountingInterceptor.listener = null
  }

  override fun before() {
    RequestCountingInterceptor.listener = object : CallEventListener {
      override fun onRequestEnd() {
        coroutineCountingIdlingResource.decrement()
      }

      override fun onRequestStart() {
        coroutineCountingIdlingResource.increment()
      }
    }
    IdlingRegistry.getInstance().register(coroutineCountingIdlingResource)
  }
}
