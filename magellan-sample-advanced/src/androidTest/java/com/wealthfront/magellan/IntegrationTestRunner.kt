package com.wealthfront.magellan

import androidx.test.runner.AndroidJUnitRunner
import com.squareup.rx2.idler.Rx2Idler
import io.reactivex.plugins.RxJavaPlugins

class IntegrationTestRunner : AndroidJUnitRunner() {

  override fun onStart() {
    RxJavaPlugins.setInitIoSchedulerHandler(Rx2Idler.create("RxJava 2.x IO Scheduler"))
    RxJavaPlugins.setInitComputationSchedulerHandler(Rx2Idler.create("RxJava 2.x Computation Scheduler"))

    super.onStart()
  }
}
