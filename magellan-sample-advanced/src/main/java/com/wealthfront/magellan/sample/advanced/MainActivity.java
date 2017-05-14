package com.wealthfront.magellan.sample.advanced;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.wealthfront.magellan.ActionBarConfig;
import com.wealthfront.magellan.NavigationListener;
import com.wealthfront.magellan.Navigator;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.wealthfront.magellan.sample.advanced.SampleApplication.app;
import static rx.schedulers.Schedulers.start;

public class MainActivity extends AppCompatActivity implements NavigationListener {

  @Inject Navigator navigator;
  Toolbar toolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    app(this).injector().inject(this);
    toolbar = ButterKnife.findById(this, R.id.toolbar);
    setSupportActionBar(toolbar);
    navigator.onCreate(this, savedInstanceState);
  }

  @Override
  public void onBackPressed() {
    if (!navigator.handleBack()) {
      super.onBackPressed();
    }
  }

  @Override
  public void onNavigate(ActionBarConfig actionBarConfig) {
    // Update action bar config based on new Screen settings
    setActionBarColor(actionBarConfig.colorRes());
    if (actionBarConfig.visible()) {
      showActionBar(actionBarConfig.animated());
    } else {
      hideActionBar(actionBarConfig.animated());
    }
    if (navigator.atRoot()) {
      toolbar.setNavigationIcon(R.drawable.action_bar_icon);
    } else {
      toolbar.setNavigationIcon(R.drawable.back_arrow);
    }
  }

  private void hideActionBar(boolean animated) {
    if (!animated) {
      // Normally you'd probably want the action bar to be GONE.
      // Set to INVISIBLE here to avoid jumping after navigation completes.
      toolbar.setVisibility(View.INVISIBLE);
      return;
    }

    // Note, this simply moves the action bar off the top of the screen.
    // This may not be sufficient for other applications when hiding the
    // toolbar, as the space on the screen for the toolbar will still be
    // shown, but it will be blank. It's more complicated to do a real
    // collapse animation that animates the view height, and we didn't
    // want to complicate the sample too much
    int actionBarHeight = getActionBarHeight();
    toolbar.animate().y(-actionBarHeight).start();
  }

  private int getActionBarHeight() {
    TypedValue tv = new TypedValue();
    if (getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.actionBarSize, tv, true)) {
      return TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
    }
    return 0;
  }

  private void showActionBar(boolean animated) {
    if (!animated) {
      toolbar.setVisibility(VISIBLE);
      return;
    }

    // Toolbar is visible by default, so we just need to undo the hide
    // animation. This would be different if you used a real collapse
    // animation that changed the height of the toolbar instead of just
    // moving the toolbar off the top of the screen.
    toolbar.setVisibility(VISIBLE);
    toolbar.animate().y(0).start();
  }

  private void setActionBarColor(int colorRes) {
    toolbar.setBackgroundColor(getResources().getColor(colorRes));
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    navigator.onCreateOptionsMenu(menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        return navigator.handleBack();
      case R.id.animateActionBar:
        navigator.goTo(new ActionBarHiddenScreen());
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

}
