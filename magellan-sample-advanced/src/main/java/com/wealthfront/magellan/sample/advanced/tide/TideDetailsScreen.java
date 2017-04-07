package com.wealthfront.magellan.sample.advanced.tide;

import android.content.Context;

import com.wealthfront.magellan.rx.RxScreen;
import com.wealthfront.magellan.sample.advanced.NoaaApi;
import com.wealthfront.magellan.sample.advanced.model.Observation;
import com.wealthfront.magellan.sample.advanced.model.TideInfo;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.wealthfront.magellan.sample.advanced.SampleApplication.app;

public class TideDetailsScreen extends RxScreen<TideDetailsView> {

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
  protected void onSubscribe(Context context) {
    autoUnsubscribe(noaaApi.getTideInfo(noaaApiId)
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<TideInfo>() {
          @Override
          public void call(TideInfo tideInfo) {
            if (!tideInfo.getData().isEmpty()) {
              List<Observation> observations = tideInfo.getData();
              BigDecimal latestMeasuredTideHeight =
                  observations.get(observations.size() - 1).getVerifiedWaterLevel();
              getView().setLatestMeasuredTideHeight(latestMeasuredTideHeight);
            }
          }
        }));
  }
}
