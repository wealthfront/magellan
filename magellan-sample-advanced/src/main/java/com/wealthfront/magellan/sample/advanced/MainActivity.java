package com.wealthfront.magellan.sample.advanced;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wealthfront.magellan.Navigator;

import javax.inject.Inject;

import static com.wealthfront.magellan.sample.advanced.SampleApplication.app;

public class MainActivity extends AppCompatActivity {

  @Inject Navigator navigator;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    app(this).injector().inject(this);
    navigator.onCreate(this, savedInstanceState);
  }

  @Override
  protected void onPause() {
    super.onPause();
    navigator.onPause(this);
  }

  @Override
  protected void onResume() {
    super.onResume();
    navigator.onResume(this);
  }

  @Override
  public void onBackPressed() {
    if (!navigator.handleBack()) {
      super.onBackPressed();
    }
  }
}
