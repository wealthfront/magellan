package com.wealthfront.magellan.sample.advanced.tide;

import android.view.View;

import com.wealthfront.magellan.sample.advanced.NoaaApi;
import com.wealthfront.magellan.sample.advanced.model.Observation;
import com.wealthfront.magellan.sample.advanced.model.TideInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.math.BigDecimal;
import java.util.Collections;

import rx.Observable;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.RuntimeEnvironment.application;
import static org.robolectric.annotation.Config.NONE;

@Config(manifest = NONE)
@RunWith(RobolectricTestRunner.class)
public class TideDetailsScreenTest {

  private static final int FAKE_NOAA_API_ID = 1;

  @Mock NoaaApi noaaApi;
  @Mock View view;
  TideDetailsScreen screen;

  @Before
  public void setUp() {
    initMocks(this);
    screen = spy(new TideDetailsScreen(FAKE_NOAA_API_ID));
    screen.noaaApi = noaaApi;
  }

  @Test
  public void onSubscribe_nullTideInfo() {
    when(noaaApi.getTideInfo(FAKE_NOAA_API_ID)).thenReturn(Observable.just(
        TideInfo.with()
            .build()));
    screen.onShow(application, view);

    verify(noaaApi).getTideInfo(FAKE_NOAA_API_ID);
    verify(screen, never()).setTideHeights(any(View.class), any(BigDecimal.class), any(BigDecimal.class), any(BigDecimal.class));
  }

  @Test
  public void onSubscribe_emptyTideInfo() {
    when(noaaApi.getTideInfo(FAKE_NOAA_API_ID)).thenReturn(Observable.just(
        TideInfo.with()
            .data(Collections.<Observation>emptyList())
            .build()));
    screen.onShow(application, view);

    verify(noaaApi).getTideInfo(FAKE_NOAA_API_ID);
    verify(screen, never()).setTideHeights(any(View.class), any(BigDecimal.class), any(BigDecimal.class), any(BigDecimal.class));
  }

  @Test
  public void onSubscribe_withTideInfo() {
    TideInfo tideInfo = TideInfo.with()
        .data(asList(
            Observation.with().verifiedWaterLevel(BigDecimal.TEN).build(),
            Observation.with().verifiedWaterLevel(BigDecimal.ZERO).build(),
            Observation.with().verifiedWaterLevel(BigDecimal.ONE).build()
        ))
        .build();
    when(noaaApi.getTideInfo(FAKE_NOAA_API_ID)).thenReturn(Observable.just(tideInfo));
    screen.onShow(application, view);

    verify(noaaApi).getTideInfo(FAKE_NOAA_API_ID);
    verify(screen).setTideHeights(view, BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.TEN);
  }

}