package com.wealthfront.magellan;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

import com.wealthfront.magellan.internal.test.DummyScreen;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static com.google.common.truth.Truth.assertThat;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.spy;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class ScreenGroupTest {

  private DummyScreen screen1;
  private DummyScreen screen2;
  private DummyScreen screen3;

  @Mock BaseScreenView<DummyScreen> view1;
  @Mock BaseScreenView<DummyScreen> view2;
  @Mock BaseScreenView<DummyScreen> view3;
  @Mock BaseScreenView<ScreenGroup> screenGroupView;

  private ScreenGroup screenGroup;

  @Before
  public void setUp() {
    initMocks(this);
    screen1 = spy(new DummyScreen(view1));
    screen2 = spy(new DummyScreen(view2));
    screen3 = spy(new DummyScreen(view3));
    screenGroup = new ScreenGroup(asList(screen1, screen2)) {
      @Override
      protected ViewGroup createView(@NotNull Context context) {
        return screenGroupView;
      }
    };
  }

  @Test
  public void addScreen() {
    Context context = Robolectric.buildActivity(Activity.class).get();
    screenGroup.create(context);
    screenGroup.show(context);
    screenGroup.resume(context);

    screenGroup.addScreen(screen3);
    assertThat(screenGroup.getScreens().size()).isEqualTo(3);
  }

  @Test
  public void addScreen_emptyConstructor() {
    screenGroup = new ScreenGroup() {
      @Override
      protected ViewGroup createView(@NotNull Context context) {
        return null;
      }
    };
    screenGroup.addScreen(screen1);
  }

  @Test
  public void addScreens() {
    screenGroup = new ScreenGroup() {
      @Override
      protected ViewGroup createView(@NotNull Context context) {
        return null;
      }
    };
    screenGroup.addScreens(asList(screen1, screen2));
  }
}
