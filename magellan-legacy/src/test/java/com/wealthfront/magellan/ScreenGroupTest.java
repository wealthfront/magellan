package com.wealthfront.magellan;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static com.google.common.truth.Truth.assertThat;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.spy;
import static org.mockito.MockitoAnnotations.initMocks;

public class ScreenGroupTest {

  private DummyScreen screen1;
  private DummyScreen screen2;
  private DummyScreen screen3;

  private Context context = new Activity();

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
