package com.wealthfront.magellan;

import android.content.Context;
import android.view.View;

public interface ScreenAwareActivity {

    Context getContext();

    void onDestroy();

    void setTitle(int titleResId);

    void setTitle(CharSequence title);

    View findViewById(int id);
}
