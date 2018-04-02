package com.wealthfront.magellan.support;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.wealthfront.magellan.ActionBarConfig;
import com.wealthfront.magellan.Navigator;
import com.wealthfront.magellan.ScreenAwareActivity;

public abstract class SingleActivity extends AppCompatActivity implements ScreenAwareActivity {

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
    public void onDestroy() {
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

    @Override
    public Context getContext() {
        return this;
    }
}
