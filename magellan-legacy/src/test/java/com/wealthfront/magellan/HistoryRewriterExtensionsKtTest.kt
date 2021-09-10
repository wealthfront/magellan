package com.wealthfront.magellan

import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.test.databinding.MagellanDummyLayoutBinding
import com.wealthfront.magellan.navigation.NavigationEvent
import com.wealthfront.magellan.test.DummyScreen
import com.wealthfront.magellan.transitions.DefaultTransition
import com.wealthfront.magellan.transitions.ShowTransition
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks
import rewriteHistoryWithNavigationEvents
import java.util.ArrayDeque

public class HistoryRewriterExtensionsKtTest {

  private lateinit var root: LegacyExpedition<*>
  private lateinit var journey1: Journey<*>
  private lateinit var screen: Screen<*>
  @Mock private lateinit var view: BaseScreenView<DummyScreen>

  @Before
  public fun setUp() {
    initMocks(this)
    root = RootJourney()
    journey1 = DummyJourney1()
    screen = DummyScreen(view)
  }

  @Test
  public fun historyRewriter() {
    val backStack = ArrayDeque<NavigationEvent>()
    backStack.push(NavigationEvent(root, DefaultTransition()))
    backStack.push(NavigationEvent(journey1, ShowTransition()))
    backStack.push(NavigationEvent(screen, DefaultTransition()))

    val historyRewriter = HistoryRewriter { stack ->
      stack.pop()
    }

    assertThat(backStack.size).isEqualTo(3)

    historyRewriter.rewriteHistoryWithNavigationEvents(backStack)

    assertThat(backStack.size).isEqualTo(2)

    val journeyInBackStack = backStack.poll()!!
    val rootInBackStack = backStack.poll()!!

    assertThat(journeyInBackStack.navigable).isEqualTo(journey1)
    assertThat(journeyInBackStack.magellanTransition).isInstanceOf(DefaultTransition::class.java)
    assertThat(rootInBackStack.navigable).isEqualTo(root)
    assertThat(rootInBackStack.magellanTransition).isInstanceOf(DefaultTransition::class.java)
  }

  @Test
  public fun historyRewriter_compatibility() {
    val backStack = ArrayDeque<NavigationEvent>()
    backStack.push(NavigationEvent(root, DefaultTransition()))
    backStack.push(NavigationEvent(journey1, ShowTransition()))

    val historyRewriter = HistoryRewriter { stack ->
      stack.push(screen)
    }

    assertThat(backStack.size).isEqualTo(2)

    historyRewriter.rewriteHistoryWithNavigationEvents(backStack)

    assertThat(backStack.size).isEqualTo(3)

    val screenInBackStack = backStack.poll()!!
    val journeyInBackStack = backStack.poll()!!
    val rootInBackStack = backStack.poll()!!

    assertThat(screenInBackStack.navigable).isEqualTo(screen)
    assertThat(screenInBackStack.magellanTransition).isInstanceOf(DefaultTransition::class.java)
    assertThat(journeyInBackStack.navigable).isEqualTo(journey1)
    assertThat(journeyInBackStack.magellanTransition).isInstanceOf(DefaultTransition::class.java)
    assertThat(rootInBackStack.navigable).isEqualTo(root)
    assertThat(rootInBackStack.magellanTransition).isInstanceOf(DefaultTransition::class.java)
  }

  @Test(expected = NoSuchElementException::class)
  public fun historyRewriter_exceptionWhenNoItemOnBackStack() {
    val backStack = ArrayDeque<NavigationEvent>()
    backStack.push(NavigationEvent(root, DefaultTransition()))

    val historyRewriter = HistoryRewriter { stack ->
      stack.pop()
      stack.pop()
    }

    historyRewriter.rewriteHistoryWithNavigationEvents(backStack)
  }

  private inner class RootJourney : LegacyExpedition<MagellanDummyLayoutBinding>(
    MagellanDummyLayoutBinding::inflate,
    MagellanDummyLayoutBinding::container
  )

  private inner class DummyJourney1 : Journey<MagellanDummyLayoutBinding>(
    MagellanDummyLayoutBinding::inflate,
    MagellanDummyLayoutBinding::container
  )
}
