package com.wealthfront.magellan.core

import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.lifecycle.attachFieldToLifecycle
import com.wealthfront.magellan.test.databinding.MagellanDummyLayoutBinding
import org.junit.Test

public class NavigableExtensionsKtTest {

  private val childDummyStep1 = ChildDummyStep1()
  private val childDummyStep2 = ChildDummyStep2()
  private val childDummyStep3 = ChildDummyStep3()
  private val dummyStep1 = DummyStep1(childDummyStep1, childDummyStep2, childDummyStep3)

  @Test
  public fun childNavigables() {
    assertThat(dummyStep1.childNavigables().size).isEqualTo(3)
    assertThat(dummyStep1.childNavigables()).containsExactlyElementsIn(setOf(childDummyStep1, childDummyStep2, childDummyStep3))
  }

  private open class DummyStep1(
    step1: Step<*>,
    step2: Step<*>,
    step3: Step<*>
  ) : Step<MagellanDummyLayoutBinding>(MagellanDummyLayoutBinding::inflate) {

    private val childDummyStep1 by attachFieldToLifecycle(step1)
    private val childDummyStep2 by attachFieldToLifecycle(step2)
    private val childDummyStep3 by attachFieldToLifecycle(step3)
  }
}

private class ChildDummyStep1 :
  Step<MagellanDummyLayoutBinding>(MagellanDummyLayoutBinding::inflate)

private class ChildDummyStep2 :
  Step<MagellanDummyLayoutBinding>(MagellanDummyLayoutBinding::inflate)

private class ChildDummyStep3 :
  Step<MagellanDummyLayoutBinding>(MagellanDummyLayoutBinding::inflate)
