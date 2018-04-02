package com.wealthfront.magellan.sample.advanced;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wealthfront.magellan.ActionBarConfig;
import com.wealthfront.magellan.Navigator;
import com.wealthfront.magellan.ScreenAwareActivity;

import javax.inject.Inject;

import static com.wealthfront.magellan.sample.advanced.SampleApplication.app;

public class MainActivity extends AppCompatActivity implements ScreenAwareActivity {

  @Inject Navigator navigator;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    app(this).injector().inject(this);
    navigator.onCreate(this, savedInstanceState);
  }

  @Override
  public void onBackPressed() {
    if (!navigator.handleBack()) {
      super.onBackPressed();
    }
  }

  @Override
  public Context getContext() {
    return this;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

}
