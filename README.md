<img src="assets/magellan_icon_web_hi_res_512.png" width="200" align="right" hspace="20" />

[![Build Status](https://travis-ci.org/wealthfront/magellan.svg?branch=master)](https://travis-ci.org/wealthfront/magellan)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.wealthfront/magellan/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.wealthfront/magellan)
[![Javadocs](https://www.javadoc.io/badge/com.wealthfront/magellan.svg)](https://www.javadoc.io/doc/com.wealthfront/magellan)

# Magellan

The simplest navigation library for Android.

## Main Features

 - Navigation is as simple as calling `goTo(screen)`
 - You get **full control** of the backstack
 - Transitions are automaticaly handled for you
 
## Download

Add the dependencies you need in your `build.gradle`:

### Core library

```gradle
compile 'com.wealthfront:magellan:1.0.0'
```
### Optional add-ons

```gradle
def magellanVersion = '1.0.0'
compile 'com.wealthfront:magellan:' + magellanVersion
compile 'com.wealthfront:magellan-support:' + magellanVersion
compile 'com.wealthfront:magellan-rx:' + magellanVersion
```

### Add-on coming soon

- Rx 2: already merged, will be in the next release (thanks to @FabianTerhorst).
- Design lib (for tabs).

## Getting started

### Single Activity

`MainActivity.java`:

```java
public class MainActivity extends SingleActivity {

  @Override
  protected Navigator createNavigator() {
    return Navigator.withRoot(new HomeScreen()).build();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_activity);
  }

}
```

`main_activity.xml`:

```xml
<com.wealthfront.magellan.ScreenContainer
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@id/magellan_container"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    /> 
```

### Minimal Screen implementation

Screen example `HomeScreen.java`:

```java
public class HomeScreen extends Screen<HomeView> {
  @Override
  protected HomeView createView(Context context) {
    return new HomeView(context);
  }
}
```

Associated View `HomeView.java`:

```java
public class HomeView extends BaseScreenView<HomeScreen> {
  public InviteView(Context context) {
    super(context);
    inflate(getContext(), R.layout.home, this);
  }
}
``` 

## Samples

[Basic sample](https://github.com/wealthfront/magellan/tree/master/magellan-sample/src/main/java/com/wealthfront/magellan/sample)

![basic-sample-gif](https://cloud.githubusercontent.com/assets/3293136/24590417/4a39bbd8-17a1-11e7-89f9-e20398001341.gif)

[Advanced sample](https://github.com/wealthfront/magellan/pull/14) (work in progress)

![advanced-sample-gif](https://cloud.githubusercontent.com/assets/3293136/24832801/b94ad73a-1c6c-11e7-89dd-2f561af21a04.gif)

## Learn More

For more, see the [wiki](https://github.com/wealthfront/magellan/wiki).

## License

```
Copyright 2017 Wealthfront, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
