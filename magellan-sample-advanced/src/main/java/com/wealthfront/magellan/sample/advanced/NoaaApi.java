package com.wealthfront.magellan.sample.advanced;

import com.wealthfront.magellan.sample.advanced.model.TideInfo;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface NoaaApi {

  @GET("/api/datagetter?format=json&application=wealthfront&product=water_level&date=today&datum=mllw&time_zone=GMT&units=english")
  Observable<TideInfo> getTideInfo(@Query("station") int stationId);

}
