package com.wealthfront.magellan.sample.advanced;

import android.os.Bundle;

import com.wealthfront.magellan.Navigator;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;

import static com.wealthfront.magellan.sample.advanced.SampleApplication.injector;

public class MainActivity extends AppCompatActivity {

  @Inject Navigator navigator;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    injector().inject(this);
    navigator.onCreate(this, savedInstanceState);
    getLifecycle().addObserver(navigator);
  }

  @Override
  public void onBackPressed() {
    if (!navigator.handleBack()) {
      super.onBackPressed();
    }
  }

}
