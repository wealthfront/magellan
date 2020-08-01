package com.wealthfront.magellan.view

import android.view.Menu
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.google.common.truth.Truth.assertThat
import com.wealthfront.magellan.core.DummyJourney
import com.wealthfront.magellan.core.DummyStep1
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MenuComponentTest {

  private lateinit var menuComponent: MenuComponent
  @Mock lateinit var menu: Menu
  @Mock lateinit var dummyStep1: DummyStep1
  @Mock lateinit var dummyJourney1: DummyJourney
  @Mock lateinit var dummyJourney2: DummyJourney

  @Before
  fun setUp() {
    initMocks(this)
    menuComponent = spy(MenuComponent())
    menuComponent.setMenu(menu)
    menuComponent.currentJourney = dummyJourney1
    menuComponent.currentStep = dummyStep1
  }

  @Test
  fun updateMenu() {
    assertThat(menuComponent.currentJourney).isEqualTo(dummyJourney1)
    assertThat(menuComponent.currentStep).isEqualTo(dummyStep1)

    menuComponent.onNavigableHidden(dummyJourney1)

    assertThat(menuComponent.currentJourney).isEqualTo(null)
    assertThat(menuComponent.currentStep).isEqualTo(null)

    menuComponent.onNavigableShown(dummyJourney2)

    assertThat(menuComponent.currentJourney).isEqualTo(dummyJourney2)
    assertThat(menuComponent.currentStep).isEqualTo(null)

    verify(dummyJourney2).onUpdateMenu(menu)
  }

  @Test
  fun destroy() {
    menuComponent.destroy(getApplicationContext())

    assertThat(menuComponent.menu).isEqualTo(null)
    assertThat(menuComponent.currentJourney).isEqualTo(null)
    assertThat(menuComponent.currentStep).isEqualTo(null)
  }
}
