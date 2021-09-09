package com.wealthfront.magellan;

import android.app.Activity;
import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class ScreenTest {

  @Mock BaseScreenView<DummyScreen> view;
  private DummyScreen screen;
  private final Context context = spy(new Activity());

  @Before
  public void setUp() {
    initMocks(this);
    screen = new DummyScreen(view);
  }

  @Test
  public void shown() {
    assertThat(screen.getActivity()).isEqualTo(null);
    assertThat(screen.getView()).isEqualTo(null);

    screen.create(context);
    screen.start(context);
    screen.resume(context);

    assertThat(screen.getActivity()).isEqualTo(context);
    assertThat(screen.getView()).isEqualTo(view);
  }

  @Test
  public void hidden() {
    assertThat(screen.getActivity()).isEqualTo(null);
    assertThat(screen.getView()).isEqualTo(null);

    screen.create(context);
    screen.start(context);
    screen.resume(context);
    screen.pause(context);
    screen.stop(context);

    assertThat(screen.getActivity()).isEqualTo(null);
    assertThat(screen.getView()).isEqualTo(null);
  }

  @Test
  public void destroyedActivity() {
    assertThat(screen.getActivity()).isEqualTo(null);
    assertThat(screen.getView()).isEqualTo(null);

    screen.create(context);
    screen.start(context);
    screen.resume(context);
    screen.pause(context);
    screen.stop(context);
    screen.destroy(context);

    assertThat(screen.getActivity()).isEqualTo(null);
    assertThat(screen.getView()).isEqualTo(null);
  }


  @Test
  public void configChange() {
    assertThat(screen.getActivity()).isEqualTo(null);
    assertThat(screen.getView()).isEqualTo(null);

    screen.create(context);
    screen.start(context);
    screen.resume(context);

    assertThat(screen.getActivity()).isEqualTo(context);
    assertThat(screen.getView()).isEqualTo(view);

    screen.pause(context);
    screen.stop(context);

    assertThat(screen.getActivity()).isEqualTo(null);
    assertThat(screen.getView()).isEqualTo(null);

    screen.start(context);
    screen.resume(context);

    assertThat(screen.getActivity()).isEqualTo(context);
    assertThat(screen.getView()).isEqualTo(view);
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
