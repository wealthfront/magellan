package com.wealthfront.magellan.sample;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.wealthfront.magellan.BaseScreenView;

class HomeView extends BaseScreenView<HomeScreen> {

  Button defaultTransitionButton;
  Button circularRevealTransitionButton;
  Button showTransitionButton;
  Button showNowTransitionButton;

  HomeView(Context context) {
    super(context);
    inflate(context, R.layout.home, this);

    defaultTransitionButton = (Button) findViewById(R.id.defaultTransitionButton);
    circularRevealTransitionButton = (Button) findViewById(R.id.circularRevealTransitionButton);
    showTransitionButton = (Button) findViewById(R.id.showTransitionButton);
    showNowTransitionButton = (Button) findViewById(R.id.showNowTransitionButton);

    defaultTransitionButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        getScreen().defaultTransitionButtonClicked();
      }
    });
    circularRevealTransitionButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        getScreen().circularRevealTransitionButtonClicked(circularRevealTransitionButton);
      }
    });
    showTransitionButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        getScreen().showTransitionButtonClicked();
      }
    });
    showNowTransitionButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        getScreen().showNowTransitionButtonClicked();
      }
    });
  }

}
