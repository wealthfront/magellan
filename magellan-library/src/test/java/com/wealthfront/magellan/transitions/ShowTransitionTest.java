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
import static org.robolectric.Robolectric.flushForegroundThreadScheduler;
import static org.robolectric.Robolectric.getForegroundThreadScheduler;

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class ShowTransitionTest {

  private boolean onAnimationEndCalled;

  @Before
  public void setUp() {
    onAnimationEndCalled = false;
    getForegroundThreadScheduler().pause();
  }

  @Test
  public void animateGoTo() throws Exception {
    checkAnimate(FORWARD);
  }

  @Test
  public void animateGoBack() throws Exception {
    checkAnimate(BACKWARD);
  }

  private void checkAnimate(Direction direction) {
    new ShowTransition().animate(new View(getApplicationContext()), new View(getApplicationContext()), direction,
        new MagellanTransition.Callback() {
          @Override
          public void onAnimationEnd() {
            onAnimationEndCalled = true;
          }
        });
    flushForegroundThreadScheduler();
    assertThat(onAnimationEndCalled).isTrue();
  }

}