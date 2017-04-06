package com.wealthfront.magellan.sample;

import android.content.Context;

import com.wealthfront.magellan.BaseScreenView;

class DetailView extends BaseScreenView<DetailScreen> {

  DetailView(Context context) {
    super(context);
    inflate(context, R.layout.detail, this);
  }

}
