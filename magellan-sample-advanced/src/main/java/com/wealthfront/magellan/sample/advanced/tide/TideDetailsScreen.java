package com.wealthfront.magellan.sample.advanced.tide;

import android.content.Context;
import android.widget.Toast;
import com.wealthfront.magellan.sample.advanced.NoaaApi;
import com.wealthfront.magellan.sample.advanced.R;
import com.wealthfront.magellan.sample.advanced.base.RefWatcherScreen;
import com.wealthfront.magellan.sample.advanced.model.Observation;
import com.wealthfront.magellan.sample.advanced.model.TideInfo;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.inject.Inject;
import rx.functions.Action1;

import static com.wealthfront.magellan.sample.advanced.SampleApplication.app;
import static rx.android.schedulers.AndroidSchedulers.mainThread;

public class TideDetailsScreen extends RefWatcherScreen<TideDetailsView> {

  public static final Comparator<Observation> OBSERVATION_COMPARATOR = new Comparator<Observation>() {
    @Override
    public int compare(Observation o1, Observation o2) {
      return o1.getVerifiedWaterLevel().compareTo(o2.getVerifiedWaterLevel());
    }
  };

  @Inject NoaaApi noaaApi;
  private final String tideLocationName;
  int noaaApiId;

  TideDetailsScreen(int noaaApiId, String tideLocationName) {
    this.noaaApiId = noaaApiId;
    this.tideLocationName = tideLocationName;
  }

  @Override
  protected TideDetailsView createView(Context context) {
    app(context).injector().inject(this);
    return new TideDetailsView(context);
  }

  @Override
  public String getTitle(Context context) {
    return tideLocationName;
  }

  @Override
  protected void onSubscribe(final Context context) {
    autoUnsubscribe(noaaApi.getTideInfo(noaaApiId)
        .observeOn(mainThread())
        .subscribe(new Action1<TideInfo>() {
          @Override
          public void call(TideInfo tideInfo) {
            if (tideInfo.getData() != null && !tideInfo.getData().isEmpty()) {
              List<Observation> observations = tideInfo.getData();
              BigDecimal highestMeasuredTideHeight =
                  Collections.max(filterOutNullMeasurements(observations), OBSERVATION_COMPARATOR)
                      .getVerifiedWaterLevel();
              BigDecimal lowestMeasuredTideHeight =
                  Collections.min(filterOutNullMeasurements(observations), OBSERVATION_COMPARATOR)
                      .getVerifiedWaterLevel();
              BigDecimal latestMeasuredTideHeight =
                  observations.get(observations.size() - 1).getVerifiedWaterLevel();
              getView().setTideHeights(latestMeasuredTideHeight, lowestMeasuredTideHeight,
                  highestMeasuredTideHeight);
            }
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Toast.makeText(context, R.string.cannot_retrieve_tide_info, Toast.LENGTH_SHORT).show();
          }
        }));
  }

  private static List<Observation> filterOutNullMeasurements(List<Observation> listWithNulls) {
    List<Observation> result = new ArrayList<>();
    for (Observation item : listWithNulls) {
      if (item.getVerifiedWaterLevel() != null) {
        result.add(item);
      }
    }
    return result;
  }
}
