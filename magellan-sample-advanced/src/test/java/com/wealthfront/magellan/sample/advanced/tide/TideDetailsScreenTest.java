package com.wealthfront.magellan.sample.advanced.tide;

import com.wealthfront.magellan.sample.advanced.NoaaApi;
import com.wealthfront.magellan.sample.advanced.model.Observation;
import com.wealthfront.magellan.sample.advanced.model.TideInfo;
import com.wealthfront.magellan.sample.advanced.util.RxSchedulersOverrideRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import java.math.BigDecimal;
import java.util.Collections;

import rx.Observable;

import static edu.emory.mathcs.backport.java.util.Collections.singletonList;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.RuntimeEnvironment.application;

@RunWith(RobolectricTestRunner.class)
public class TideDetailsScreenTest {

  private static final int FAKE_NOAA_API_ID = 1;

  @Rule public RxSchedulersOverrideRule rule = new RxSchedulersOverrideRule();

  @Mock NoaaApi noaaApi;
  @Mock TideDetailsView view;
  TideDetailsScreen screen;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
    screen = new TideDetailsScreen(FAKE_NOAA_API_ID, "San Francisco");
    screen.noaaApi = noaaApi;
    screen.setView(view);
  }

  @Test
  public void onSubscribe_emptyTideInfo() throws Exception {
    when(noaaApi.getTideInfo(FAKE_NOAA_API_ID)).thenReturn(Observable.just(
        TideInfo.with()
            .data(Collections.<Observation>emptyList())
            .build()));
    screen.onSubscribe(application);

    verify(noaaApi).getTideInfo(FAKE_NOAA_API_ID);
    verify(view, never()).setTideHeights(any(BigDecimal.class), any(BigDecimal.class), any(BigDecimal.class));
  }

  @Test
  public void onSubscribe_withTideInfo() throws Exception {
    TideInfo tideInfo = TideInfo.with()
        .data(singletonList(
            Observation.with().verifiedWaterLevel(BigDecimal.ONE).build()))
        .build();
    when(noaaApi.getTideInfo(FAKE_NOAA_API_ID)).thenReturn(Observable.just(tideInfo));
    screen.onSubscribe(application);

    verify(noaaApi).getTideInfo(FAKE_NOAA_API_ID);
    verify(view).setTideHeights(BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE);
  }

}