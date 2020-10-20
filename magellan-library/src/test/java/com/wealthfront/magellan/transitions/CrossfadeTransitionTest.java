package com.wealthfront.magellan.transitions;

import android.view.View;

import com.wealthfront.magellan.Direction;
import com.wealthfront.magellan.transitions.MagellanTransition.Callback;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static com.google.common.truth.Truth.assertThat;
import static com.wealthfront.magellan.Direction.BACKWARD;
import static com.wealthfront.magellan.Direction.FORWARD;
import static org.robolectric.Robolectric.flushForegroundThreadScheduler;
import static org.robolectric.Robolectric.getForegroundThreadScheduler;

@RunWith(RobolectricTestRunner.class)
public class CrossfadeTransitionTest {

  boolean onAnimationEndCalled;
  View from = new View(getApplicationContext());
  View to = new View(getApplicationContext());

  @Before
  public void setUp() {
    onAnimationEndCalled = false;
    from.setVisibility(VISIBLE);
    to.setVisibility(GONE);
    getForegroundThreadScheduler().pause();
  }

  @Test
  public void animateShow() {
    checkAnimate(FORWARD);
  }

  @Test
  public void animateHide() {
    checkAnimate(BACKWARD);
  }

  private void checkAnimate(Direction direction) {
    new CrossfadeTransition().animate(from, to, direction, new Callback() {
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