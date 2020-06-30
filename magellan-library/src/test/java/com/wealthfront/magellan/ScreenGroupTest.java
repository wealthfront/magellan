package com.wealthfront.magellan;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static com.google.common.truth.Truth.assertThat;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class ScreenGroupTest {

  private DummyScreen screen1;
  private DummyScreen screen2;
  private DummyScreen screen3;

  @Mock BaseScreenView<DummyScreen> view1;
  @Mock BaseScreenView<DummyScreen> view2;
  @Mock BaseScreenView<DummyScreen> view3;
  @Mock BaseScreenView<ScreenGroup> screenGroupView;

  @Mock Bundle bundle;
  @Mock Context context;
  @Mock Navigator navigator;

  private ScreenGroup screenGroup;

  @Before
  public void setUp() {
    initMocks(this);
    screen1 = spy(new DummyScreen(view1));
    screen2 = spy(new DummyScreen(view2));
    screen3 = spy(new DummyScreen(view3));
    screenGroup = new ScreenGroup(asList(screen1, screen2)) {
      @Override
      protected ViewGroup createView(Context context) {
        return screenGroupView;
      }
    };
  }

  @Test
  public void addScreen() {
    screenGroup.addScreen(screen3);
    assertThat(screenGroup.getScreens().size()).isEqualTo(3);
  }

  @Test
  public void addScreen_emptyConstructor() {
    screenGroup = new ScreenGroup() {
      @Override
      protected ViewGroup createView(Context context) {
        return null;
      }
    };
    screenGroup.addScreen(screen1);
  }

  @Test(expected = IllegalStateException.class)
  public void addScreen_onCreateCalled() {
    screen3.recreateView(new Activity());
    screenGroup.addScreen(screen3);
  }

  @Test(expected = IllegalStateException.class)
  public void addScreen_thisOnCreateCalled() {
    screenGroup.recreateView(new Activity());
    screenGroup.addScreen(screen3);
  }

  @Test
  public void addScreens() {
    screenGroup = new ScreenGroup() {
      @Override
      protected ViewGroup createView(Context context) {
        return null;
      }
    };
    screenGroup.addScreens(asList(screen1, screen2));
  }

  @Test(expected = IllegalStateException.class)
  public void addScreens_thisOnCreateCalled() {
    screenGroup.recreateView(new Activity());
    screenGroup.addScreens(asList(screen3));
  }

  @Test(expected = IllegalStateException.class)
  public void addScreens_onCreateCalled() {
    screen3.recreateView(new Activity());
    screenGroup.addScreens(asList(screen3));
  }

  @Test
  public void onShow() {
    screenGroup.onShow(context);
    verify(screen1).onShow(context);
    verify(screen2).onShow(context);
    assertThat(screen1.getView()).isNotNull();
    assertThat(screen2.getView()).isNotNull();
  }

  @Test
  public void onRestore() {
    screenGroup.onRestore(bundle);
    verify(screen1).onRestore(bundle);
    verify(screen2).onRestore(bundle);
  }

  @Test
  public void onResume() {
    screenGroup.onResume(context);
    verify(screen1).onResume(context);
    verify(screen2).onResume(context);
  }

  @Test
  public void onPause() {
    screenGroup.onPause(context);
    verify(screen1).onPause(context);
    verify(screen2).onPause(context);
  }

  @Test
  public void onSave() {
    screenGroup.onSave(bundle);
    verify(screen1).onSave(bundle);
    verify(screen2).onSave(bundle);
  }

  @Test
  public void onHide() {
    screenGroup.onHide(context);
    verify(screen1).onHide(context);
    verify(screen2).onHide(context);
    assertThat(screen1.getDialog()).isNull();
    assertThat(screen1.getView()).isNull();
    assertThat(screen2.getDialog()).isNull();
    assertThat(screen2.getView()).isNull();
  }
}
