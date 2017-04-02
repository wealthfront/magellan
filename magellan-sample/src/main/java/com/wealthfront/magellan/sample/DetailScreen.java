package com.wealthfront.magellan.sample;

import android.content.Context;

import com.wealthfront.magellan.Screen;

class DetailScreen extends Screen<DetailView> {

    @Override
    protected DetailView createView(Context context) {
        return new DetailView(context);
    }

    @Override
    public String getTitle(Context context) {
        return "Detail Screen";
    }

}
