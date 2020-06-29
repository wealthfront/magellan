package com.wealthfront.magellan.sample;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4ClassRunner.class)
public class ExampleInstrumentedTest {
  @Test
  public void useAppContext() {
    // Context of the app under test.
    Context appContext = ApplicationProvider.getApplicationContext();

    assertEquals("com.wealthfront.magellan.sample", appContext.getPackageName());
  }
}