package com.wealthfront.magellan.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wealthfront.magellan.Navigator;

public class MainActivity extends AppCompatActivity {

  Navigator navigator;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_activity);
    navigator = Navigator.withRoot(new HomeScreen()).loggingEnabled(true).build();
    navigator.onCreate(this, savedInstanceState);
  }

  @Override
  public void onBackPressed() {
    if (!navigator.handleBack()) {
      super.onBackPressed();
    }
  }

}
