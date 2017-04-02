package com.wealthfront.magellan.sample;

import android.content.Context;
import android.view.View;

import com.wealthfront.magellan.Screen;
import com.wealthfront.magellan.transitions.CircularRevealTransition;

class HomeScreen extends Screen<HomeView> {

    @Override
    protected HomeView createView(Context context) {
        return new HomeView(context);
    }

    @Override
    public String getTitle(Context context) {
        return "Home Screen";
    }

    public void defaultTransitionButtonClicked() {
        getNavigator().goTo(new DetailScreen());
    }

    public void circularRevealTransitionButtonClicked(View clickedView) {
        getNavigator().overrideTransition(new CircularRevealTransition(clickedView)).goTo(new DetailScreen());
    }

    public void showTransitionButtonClicked() {
        getNavigator().show(new DetailScreen());
    }

    public void showNowTransitionButtonClicked() {
        getNavigator().showNow(new DetailScreen());
    }

}
