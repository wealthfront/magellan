package com.wealthfront.magellan.core

import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.databinding.MagellanDummyLayoutBinding
import com.wealthfront.magellan.lifecycle.lifecycle
import org.junit.Test

class NavigableExtensionsKtTest {

  private val dummyStep1 = DummyStep1()

  @Test
  fun childNavigables() {
    assertThat(dummyStep1.childNavigables().size).isEqualTo(3)
    assertThat(dummyStep1.childNavigables()[0]).isInstanceOf(ChildDummyStep1::class.java)
    assertThat(dummyStep1.childNavigables()[1]).isInstanceOf(ChildDummyStep2::class.java)
    assertThat(dummyStep1.childNavigables()[2]).isInstanceOf(ChildDummyStep3::class.java)
  }
}

open class DummyJourney : Journey<MagellanDummyLayoutBinding>(MagellanDummyLayoutBinding::inflate, MagellanDummyLayoutBinding::container)

open class DummyStep1 : Step<MagellanDummyLayoutBinding>(MagellanDummyLayoutBinding::inflate) {
  private val childDummyStep1 by lifecycle(ChildDummyStep1())
  private val childDummyStep2 by lifecycle(ChildDummyStep2())
  private val childDummyStep3 by lifecycle(ChildDummyStep3())
}

open class DummyStep2 : Step<MagellanDummyLayoutBinding>(MagellanDummyLayoutBinding::inflate)

private class ChildDummyStep1 : Step<MagellanDummyLayoutBinding>(MagellanDummyLayoutBinding::inflate)
private class ChildDummyStep2 : Step<MagellanDummyLayoutBinding>(MagellanDummyLayoutBinding::inflate)
private class ChildDummyStep3 : Step<MagellanDummyLayoutBinding>(MagellanDummyLayoutBinding::inflate)
