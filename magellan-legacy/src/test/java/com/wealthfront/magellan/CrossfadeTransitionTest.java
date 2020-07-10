package com.wealthfront.magellan;

import android.view.View;

import com.wealthfront.magellan.transitions.CrossfadeTransition;
import com.wealthfront.magellan.transitions.Transition;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.google.common.truth.Truth.assertThat;
import static com.wealthfront.magellan.Direction.BACKWARD;
import static com.wealthfront.magellan.Direction.FORWARD;
import static com.wealthfront.magellan.NavigationType.GO;
import static com.wealthfront.magellan.NavigationType.NO_ANIM;
import static com.wealthfront.magellan.NavigationType.SHOW;
import static org.robolectric.Robolectric.flushForegroundThreadScheduler;
import static org.robolectric.Robolectric.getForegroundThreadScheduler;
import static org.robolectric.RuntimeEnvironment.application;

@RunWith(RobolectricTestRunner.class)
public class CrossfadeTransitionTest {

  boolean onAnimationEndCalled;
  View from = new View(application);
  View to = new View(application);

  @Before
  public void setUp() {
    onAnimationEndCalled = false;
    from.setVisibility(VISIBLE);
    to.setVisibility(GONE);
    getForegroundThreadScheduler().pause();
  }

  @Test
  public void animateGoTo() {
    checkAnimate(GO, FORWARD);
  }

  @Test
  public void animateGoBack() {
    checkAnimate(GO, BACKWARD);
  }

  @Test
  public void animateShow() {
    checkAnimate(SHOW, FORWARD);
  }

  @Test
  public void animateHide() {
    checkAnimate(SHOW, BACKWARD);
  }

  @Test
  public void animateShowNow() {
    checkAnimate(NO_ANIM, FORWARD);
  }

  private void checkAnimate(NavigationType navType, Direction direction) {
    new CrossfadeTransition().animate(from, to, navType, direction, new Transition.Callback() {
      @Override
      public void onAnimationEnd() {
        onAnimationEndCalled = true;
      }
    });
    flushForegroundThreadScheduler();
    assertThat(onAnimationEndCalled).isTrue();
    assertThat(from.getVisibility()).isEqualTo(GONE);
    assertThat(to.getVisibility()).isEqualTo(VISIBLE);
  }

}