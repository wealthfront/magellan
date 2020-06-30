package com.wealthfront.magellan.sample.advanced;

import androidx.appcompat.app.AppCompatActivity;
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
  public void onBackPressed() {
    if (!navigator.handleBack()) {
      super.onBackPressed();
    }
  }

}
