package com.wealthfront.magellan

import android.app.Activity
import android.content.Context
import android.view.Menu
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class LazyTabsScreenGroupTest {

  lateinit var screen1: Screen<*>
  lateinit var screen2: Screen<*>
  lateinit var screen3: Screen<*>
  lateinit var lazy1: LazyScreen<Screen<*>>
  lateinit var lazy2: LazyScreen<Screen<*>>
  lateinit var lazy3: LazyScreen<Screen<*>>

  @Mock lateinit var screenView: BaseScreenView<*>
  @Mock lateinit var navigator: LegacyNavigator
  @Mock lateinit var view1: BaseScreenView<*>
  @Mock lateinit var view2: BaseScreenView<*>
  @Mock lateinit var view3: BaseScreenView<*>
  @Mock lateinit var menu: Menu

  private val context: Context = spy(Activity())

  lateinit var screenGroup: LazyTabsScreenGroup<Screen<*>, BaseScreenView<*>>

  open class MockScreen(val mockView: BaseScreenView<*>) : Screen<BaseScreenView<*>>() {
    override fun createView(context: Context): BaseScreenView<*> {
      return mockView
    }
  }

  open class MockLazyTabsScreenGroup(val mockView: BaseScreenView<*>) : LazyTabsScreenGroup<Screen<*>, BaseScreenView<*>>() {

    override fun onScreenDisplayed(screen: Screen<*>?) {}

    override fun createView(context: Context): BaseScreenView<*> {
      return mockView
    }
  }

  @Before
  fun setUp() {
    initMocks(this)
    screenGroup = spy(MockLazyTabsScreenGroup(screenView))
    screen1 = spy(MockScreen(view1))
    screen2 = spy(MockScreen(view2))
    screen3 = spy(MockScreen(view3))
    screenGroup.addScreens(listOf(screen1, screen2, screen3))
    lazy1 = spy(screenGroup.lazyScreens[0])
    lazy2 = spy(screenGroup.lazyScreens[1])
    lazy3 = spy(screenGroup.lazyScreens[2])
    screenGroup.lazyScreens = listOf(lazy1, lazy2, lazy3)
    screenGroup.selectedLazyScreen = lazy1
    screenGroup.navigator = navigator
    screenGroup.setActivity(context as Activity)
  }

  @Test(expected = IllegalArgumentException::class)
  fun setSelectedScreen_ScreenNotFound() {
    val screen4 = MockScreen(view1)
    screenGroup.selectedScreen = screen4

    verify(screenGroup).selectedScreen = screen2
  }

  @Test
  fun onUpdateMenu() {
    screenGroup.onUpdateMenu(menu)

    assertThat(screenGroup.selectedScreen).isEqualTo(screen1)
    verify(screen1).onUpdateMenu(menu)
  }

  @Test
  fun onShow() {
    screenGroup.lazyScreens[0].isLoaded = true
    screenGroup.lazyScreens[1].isLoaded = true
    screenGroup.lazyScreens[2].isLoaded = true
    lazy1 = spy(screenGroup.lazyScreens[0])
    lazy2 = spy(screenGroup.lazyScreens[1])
    lazy3 = spy(screenGroup.lazyScreens[2])
    screenGroup.lazyScreens = listOf(lazy1, lazy2, lazy3)
    screenGroup.selectedLazyScreen = lazy1
    screenGroup.onShow(context)

    verify(lazy1).isLoaded = false
    verify(lazy1).isLoaded = true
    verify(lazy2).isLoaded = false
    verify(lazy3).isLoaded = false
    verify(screenGroup).showSelectedScreen()
  }

  @Test
  fun showChildScreen() {
    screenGroup.showSelectedScreen()
  }

  @Test
  fun getTitle() {
    screenGroup.getTitle(context)

    assertThat(screenGroup.selectedScreen).isEqualTo(screen1)
    verify(screen1).getTitle(context)
  }
}
