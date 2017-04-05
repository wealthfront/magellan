package com.wealthfront.magellan.sample.advanced;

import android.content.Context;

import com.wealthfront.magellan.Screen;

class HomeScreen extends Screen<HomeView> {

    @Override
    protected HomeView createView(Context context) {
        return new HomeView(context);
    }

    @Override
    public String getTitle(Context context) {
        return "Home Screen";
    }
}
