[![Build Status](https://travis-ci.org/wealthfront/magellan.svg?branch=master)](https://travis-ci.org/wealthfront/magellan)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.wealthfront/magellan-library/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.wealthfront/magellan-library)
[![Javadocs](https://www.javadoc.io/badge/com.wealthfront/magellan-library.svg)](https://www.javadoc.io/doc/com.wealthfront/magellan-library)

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
implementation 'com.wealthfront:magellan-library:2.0.2-beta'
```
### Optional add-ons

```groovy
def magellanVersion = '2.0.2-beta'
implementation "com.wealthfront:magellan-library:${magellanVersion}"
implementation "com.wealthfront:magellan-support:${magellanVersion}"
implementation "com.wealthfront:magellan-rx:${magellanVersion}"
implementation "com.wealthfront:magellan-rx2:${magellanVersion}"

For support of older version:
implementation "com.wealthfront:magellan-legacy:${magellanVersion}"
```

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
