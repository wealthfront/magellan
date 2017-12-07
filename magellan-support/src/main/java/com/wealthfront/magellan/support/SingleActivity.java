package com.wealthfront.magellan.support;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.wealthfront.magellan.Navigator;

public abstract class SingleActivity extends AppCompatActivity {

  private static Navigator navigator;

  protected abstract Navigator createNavigator();

  public static Navigator getNavigator() {
    return navigator;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (navigator == null) {
      navigator = createNavigator();
    }
  }

  @Override
  protected void onPostCreate(@Nullable Bundle savedInstanceState) {
    navigator.onCreate(this, savedInstanceState);
    super.onPostCreate(savedInstanceState);
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    navigator.onSaveInstanceState(outState);
  }

  @Override
  protected void onResume() {
    super.onResume();
    navigator.onResume(this);
  }

  @Override
  protected void onPause() {
    navigator.onPause(this);
    super.onPause();
  }

  @Override
  protected void onDestroy() {
    navigator.onDestroy(this);
    super.onDestroy();
  }

  @Override
  public void onBackPressed() {
    if (!navigator.handleBack()) {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    navigator.onCreateOptionsMenu(menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    navigator.onPrepareOptionsMenu(menu);
    return super.onPrepareOptionsMenu(menu);
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    navigator.onRequestPermissionsResult(requestCode, permissions, grantResults);
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    navigator.onActivityResult(requestCode, resultCode, data);
  }
}
