[![Build Status](https://travis-ci.org/wealthfront/magellan.svg?branch=master)](https://travis-ci.org/wealthfront/magellan)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.wealthfront/magellan/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.wealthfront/magellan)
[![Javadocs](https://www.javadoc.io/badge/com.wealthfront/magellan.svg)](https://www.javadoc.io/doc/com.wealthfront/magellan)

# Magellan

<img src="assets/magellan_icon_web_hi_res_512.png" width="200" align="right" />

The simplest navigation library for Android.

> Note: This library is currently under development for the next major version.

## Main Features

 - Navigation is as simple as calling `goTo(screen)`
 - You get **full control** of the backstack
 - Transitions are automaticaly handled for you
 
## Download

Add the dependencies you need in your `build.gradle`:

### Core library

```groovy
implementation 'com.wealthfront:magellan:1.1.0'
```
### Optional add-ons

```groovy
def magellanVersion = '2.0.0-alpha'
implementation "com.wealthfront:magellan-library:${magellanVersion}"
implementation "com.wealthfront:magellan-support:${magellanVersion}"
implementation "com.wealthfront:magellan-rx:${magellanVersion}"
implementation "com.wealthfront:magellan-rx2:${magellanVersion}"

For support for older versions of magellan:
implementation "com.wealthfront:magellan-legacy:${magellanVersion}"
```

### Coming soon

- Better Kotlin interoperability
- Design lib add-on (for tabs), in the meantime, [here is the code to implement tabs](https://github.com/wealthfront/magellan/wiki/Implementing-Tabs-or-other-%22Screens-into-a-Screen%22-UI%2C-using-ScreenGroup).

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
    setContentView(R.layout.activity_main);
  }

}
```

`activity_main.xml`:

```xml
<com.wealthfront.magellan.ScreenContainer
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/magellan_container"
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
  public HomeView(Context context) {
    super(context);
    inflate(context, R.layout.home, this);
  }
}
``` 

## Samples

[Basic sample](https://github.com/wealthfront/magellan/tree/master/magellan-sample/src/main/java/com/wealthfront/magellan/sample)

![basic-sample-gif](https://cloud.githubusercontent.com/assets/3293136/24590417/4a39bbd8-17a1-11e7-89f9-e20398001341.gif)

[Advanced sample](https://github.com/wealthfront/magellan/tree/master/magellan-sample-advanced) using Dependency Injection, Retrofit, and Rx.

![advanced-sample-gif](https://cloud.githubusercontent.com/assets/3293136/24832801/b94ad73a-1c6c-11e7-89dd-2f561af21a04.gif)

[Kotlin sample](https://github.com/jmfayard/android-kotlin-magellan) (courtesey of @jmfayard)

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
