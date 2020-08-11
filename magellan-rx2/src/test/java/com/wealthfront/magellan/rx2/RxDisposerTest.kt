package com.wealthfront.magellan.rx2

import android.app.Activity
import com.google.common.truth.Truth.assertThat
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test

class RxDisposerTest {

  private lateinit var rxDisposer: RxDisposer

  private val testSubscriber: TestObserver<String> = TestObserver.create()
  private val context = Activity()

  @Before
  fun setUp() {
    rxDisposer = RxDisposer()
    Observable.just("a").subscribe(testSubscriber)
  }

  @Test
  fun autoDisposeOnCreate() {
    assertThat(testSubscriber.isDisposed).isFalse()

    rxDisposer.create(context)

    rxDisposer.autoDispose(testSubscriber)

    assertThat(testSubscriber.isDisposed).isFalse()

    rxDisposer.show(context)
    rxDisposer.resume(context)
    rxDisposer.pause(context)
    rxDisposer.hide(context)

    assertThat(testSubscriber.isDisposed).isTrue()
  }

  @Test
  fun autoDisposeOnShow() {
    assertThat(testSubscriber.isDisposed).isFalse()

    rxDisposer.create(context)
    rxDisposer.show(context)

    rxDisposer.autoDispose(testSubscriber)

    assertThat(testSubscriber.isDisposed).isFalse()

    rxDisposer.resume(context)
    rxDisposer.pause(context)
    rxDisposer.hide(context)

    assertThat(testSubscriber.isDisposed).isTrue()
  }

  @Test
  fun autoDisposeOnResume() {
    assertThat(testSubscriber.isDisposed).isFalse()

    rxDisposer.create(context)
    rxDisposer.show(context)
    rxDisposer.resume(context)

    rxDisposer.autoDispose(testSubscriber)

    assertThat(testSubscriber.isDisposed).isFalse()

    rxDisposer.pause(context)
    rxDisposer.hide(context)

    assertThat(testSubscriber.isDisposed).isTrue()
  }
}
