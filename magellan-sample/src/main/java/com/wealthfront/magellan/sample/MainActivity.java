package com.wealthfront.magellan.sample;

import android.os.Bundle;

import com.wealthfront.magellan.Navigator;
import com.wealthfront.magellan.support.SingleActivity;

public class MainActivity extends SingleActivity {

    @Override
    protected Navigator createNavigator() {
        return Navigator.withRoot(new HomeScreen()).loggingEnabled(true).build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }

}
