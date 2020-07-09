package com.wealthfront.magellan.sample.advanced.tide;

import android.content.Context;
import android.view.LayoutInflater;

import com.wealthfront.magellan.sample.advanced.NoaaApi;
import com.wealthfront.magellan.sample.advanced.databinding.TideDetailBinding;
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

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.google.common.truth.Truth.assertThat;
import static java.util.Arrays.asList;
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
  @Mock Context context;
  TideDetailsScreen screen;
  TideDetailBinding binding;

  @Before
  public void setUp() {
    initMocks(this);
    screen = new TideDetailsScreen(FAKE_NOAA_API_ID);

    binding = TideDetailBinding.inflate(LayoutInflater.from(application));
    screen.noaaApi = noaaApi;
  }

  @Test
  public void onSubscribe_nullTideInfo() {
    when(noaaApi.getTideInfo(FAKE_NOAA_API_ID)).thenReturn(Observable.just(
        TideInfo.with()
            .build()));
    screen.onShow(context, binding);

    verify(noaaApi).getTideInfo(FAKE_NOAA_API_ID);
    assertThat(binding.tideDetailsContent.getVisibility()).isEqualTo(GONE);
    assertThat(binding.loading.getVisibility()).isEqualTo(VISIBLE);
  }

  @Test
  public void onSubscribe_emptyTideInfo() {
    when(noaaApi.getTideInfo(FAKE_NOAA_API_ID)).thenReturn(Observable.just(
        TideInfo.with()
            .data(Collections.<Observation>emptyList())
            .build()));
    screen.onShow(context, binding);

    verify(noaaApi).getTideInfo(FAKE_NOAA_API_ID);
    assertThat(binding.tideDetailsContent.getVisibility()).isEqualTo(GONE);
    assertThat(binding.loading.getVisibility()).isEqualTo(VISIBLE);
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
    screen.onShow(context, binding);

    verify(noaaApi).getTideInfo(FAKE_NOAA_API_ID);
    assertThat(binding.tideDetailsContent.getVisibility()).isEqualTo(VISIBLE);
    assertThat(binding.loading.getVisibility()).isEqualTo(GONE);
    assertThat(binding.currentWaterLevel.getText()).isEqualTo("Current Water Level: 1.00 ft");
    assertThat(binding.highestWaterLevel.getText()).isEqualTo("Today's Highest Water Level: 10.00 ft");
    assertThat(binding.lowestWaterLevel.getText()).isEqualTo("Today's Lowest Water Level: 0.00 ft");
  }

}