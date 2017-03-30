package com.wealthfront.magellan;

import android.view.View;

import com.wealthfront.magellan.transitions.DefaultTransition;
import com.wealthfront.magellan.transitions.Transition;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;
import static com.wealthfront.magellan.Direction.BACKWARD;
import static com.wealthfront.magellan.Direction.FORWARD;
import static com.wealthfront.magellan.NavigationType.GO;
import static com.wealthfront.magellan.NavigationType.NO_ANIM;
import static com.wealthfront.magellan.NavigationType.SHOW;
import static org.robolectric.Robolectric.flushForegroundThreadScheduler;
import static org.robolectric.Robolectric.getForegroundThreadScheduler;
import static org.robolectric.RuntimeEnvironment.application;

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class DefaultTransitionTest {

  private boolean onAnimationEndCalled;

  @Before
  public void setUp() {
    onAnimationEndCalled = false;
    getForegroundThreadScheduler().pause();
  }

  @Test
  public void animateGoTo() throws Exception {
    checkAnimate(GO, FORWARD);
  }

  @Test
  public void animateGoBack() throws Exception {
    checkAnimate(GO, BACKWARD);
  }

  @Test
  public void animateShow() throws Exception {
    checkAnimate(SHOW, FORWARD);
  }

  @Test
  public void animateHide() throws Exception {
    checkAnimate(SHOW, BACKWARD);
  }

  @Test
  public void animateShowNow() throws Exception {
    checkAnimate(NO_ANIM, FORWARD);
  }

  private void checkAnimate(NavigationType navigationType, Direction direction) {
    new DefaultTransition().animate(new View(application), new View(application), navigationType, direction,
        new Transition.Callback() {
          @Override
          public void onAnimationEnd() {
            onAnimationEndCalled = true;
          }
        });
    flushForegroundThreadScheduler();
    assertThat(onAnimationEndCalled).isTrue();
  }

}