package com.wealthfront.magellan;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class ScreenTest {

  @Mock BaseScreenView<DummyScreen> view;
  @Captor ArgumentCaptor<SparseArray<Parcelable>> sparseArrayCaptor;
  private DummyScreen screen;
  private Context context = new Activity();

  @Before
  public void setUp() {
    initMocks(this);
    screen = new DummyScreen(view);
  }

  @Test
  public void recreateView() {
    View v = screen.recreateView(null);

    assertThat(screen.getView()).isEqualTo(v);
    assertThat(view).isEqualTo(v);
    assertThat(view.getScreen()).isEqualTo(screen);
  }

  @Test
  public void showScreen() {
    screen.onCreate(context);
    screen.onShow(context);
    screen.onResume(context);

    assertThat(screen.getActivity()).isEqualTo(context);
    assertThat(screen.getView()).isEqualTo(view);
  }

  @Test
  public void hideScreen() {
    screen.onCreate(context);
    screen.onShow(context);
    screen.onResume(context);
    screen.onPause(context);
    screen.onHide(context);

    assertThat(screen.getActivity()).isEqualTo(null);
    assertThat(screen.getView()).isEqualTo(null);
  }

  @Test
  public void destroyView() {
    screen.recreateView(null);
    screen.destroyView();

    verify(view).saveHierarchyState(isA(SparseArray.class));
  }

  @Test
  public void saveRestore() {
    final Bundle state42 = new Bundle();
    state42.putString("key", "value");
    doAnswer(invocation -> {
      sparseArrayCaptor.getValue().put(42, state42);
      return null;
    }).when(view).saveHierarchyState(sparseArrayCaptor.capture());

    screen.recreateView(null);
    Bundle bundle = new Bundle();
    screen.restore(bundle);
    screen.recreateView(null);

    verify(view).saveHierarchyState(isA(SparseArray.class));
    verify(view).restoreHierarchyState(sparseArrayCaptor.capture());
    assertThat(((Bundle) sparseArrayCaptor.getValue().get(42)).getString("key")).isEqualTo("value");
  }

  @Test
  public void whenTransitionFinished() {
    final Screen.TransitionFinishedListener listener = mock(Screen.TransitionFinishedListener.class);
    screen.transitionStarted();

    screen.whenTransitionFinished(listener);
    verify(listener, never()).onTransitionFinished();

    screen.transitionFinished();
    verify(listener).onTransitionFinished();

    reset(listener);
    screen.transitionStarted();
    screen.transitionFinished();
    verify(listener, never()).onTransitionFinished();
  }

  @Test
  public void whenTransitionFinished_beforeTransitionStarted() {
    final Screen.TransitionFinishedListener listener = mock(Screen.TransitionFinishedListener.class);
    screen.whenTransitionFinished(listener);
    verify(listener).onTransitionFinished();

    reset(listener);
    screen.transitionStarted();
    screen.transitionFinished();
    verify(listener, never()).onTransitionFinished();
  }

  @Test
  public void whenTransitionFinished_afterTransitionFinished() {
    final Screen.TransitionFinishedListener listener = mock(Screen.TransitionFinishedListener.class);
    screen.transitionStarted();
    screen.transitionFinished();

    screen.whenTransitionFinished(listener);
    verify(listener).onTransitionFinished();
  }
}
