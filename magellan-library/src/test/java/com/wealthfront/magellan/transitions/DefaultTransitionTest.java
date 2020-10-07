package com.wealthfront.magellan.transitions;

import android.view.View;

import com.wealthfront.magellan.Direction;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static com.google.common.truth.Truth.assertThat;
import static com.wealthfront.magellan.Direction.BACKWARD;
import static com.wealthfront.magellan.Direction.FORWARD;
import static com.wealthfront.magellan.Transition.GO;
import static com.wealthfront.magellan.Transition.NO_ANIM;
import static com.wealthfront.magellan.Transition.SHOW;
import static org.robolectric.Robolectric.flushForegroundThreadScheduler;
import static org.robolectric.Robolectric.getForegroundThreadScheduler;

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

  private void checkAnimate(Transition navigationType, Direction direction) {
    new DefaultTransition().animate(new View(getApplicationContext()), new View(getApplicationContext()), navigationType, direction,
        new com.wealthfront.magellan.transitions.Transition.Callback() {
          @Override
          public void onAnimationEnd() {
            onAnimationEndCalled = true;
          }
        });
    flushForegroundThreadScheduler();
    assertThat(onAnimationEndCalled).isTrue();
  }

}