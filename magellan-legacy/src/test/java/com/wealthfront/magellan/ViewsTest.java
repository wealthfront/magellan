package com.wealthfront.magellan;

import android.app.Activity;
import android.view.View;

import com.wealthfront.magellan.Views.OnMeasured;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.LooperMode;

import static com.google.common.truth.Truth.assertThat;
import static com.wealthfront.magellan.Views.whenMeasured;
import static org.robolectric.Robolectric.setupActivity;

@RunWith(RobolectricTestRunner.class)
@LooperMode(LooperMode.Mode.LEGACY)
public class ViewsTest {

  private boolean onMeasuredCalled;
  private Activity activity;
  private View view;

  @Before
  public void setUp() {
    onMeasuredCalled = false;
    activity = setupActivity(Activity.class);
    view = new View(activity);
  }

  @Test
  public void whenMeasuredCallOnMeasured() {
    whenMeasured(view, new OnMeasured() {
      @Override
      public void onMeasured() {
        onMeasuredCalled = true;
      }
    });
    activity.setContentView(view);
    assertThat(onMeasuredCalled).isTrue();
  }

}