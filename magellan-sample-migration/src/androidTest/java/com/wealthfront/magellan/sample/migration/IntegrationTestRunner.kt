package com.wealthfront.magellan.sample.migration

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class IntegrationTestRunner : AndroidJUnitRunner() {

  @Throws(
    InstantiationException::class,
    IllegalAccessException::class,
    ClassNotFoundException::class
  )
  override fun newApplication(
    cl: ClassLoader,
    className: String,
    context: Context,
  ): Application {
    return super.newApplication(cl, TestSampleApplication::class.java.name, context)
  }
}
