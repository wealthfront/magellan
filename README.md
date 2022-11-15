[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.wealthfront/magellan-library/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.wealthfront/magellan-library)

# Magellan

<img src="assets/magellan_icon_web_hi_res_512.png" width="200" align="right" />

A simple, flexible, and practical navigation framework for Android.

> Note: This library is currently under development for the next major version.

## Why would I use Magellan?

- **Simple**: Intuitive abstractions and encapsulation make it easy to reason through code.
- **Flexible**: The infinitely-nestable structure allows for many different styles of structuring an app and navigating between pages.
- **Practical**: We pay special attention to simplifying common patterns and removing day-to-day boilerplate.
- **Testable**: Plain objects that are easy to instantiate make testing simple.
 
## Download

Add the dependencies you need in your `build.gradle`:

### Core library

```groovy
def magellanVersion = '2.2.2-beta'
implementation "com.wealthfront:magellan-library:${magellanVersion}"
```

### Optional add-ons

```groovy
implementation "com.wealthfront:magellan-support:${magellanVersion}"
implementation "com.wealthfront:magellan-rx:${magellanVersion}"
implementation "com.wealthfront:magellan-rx2:${magellanVersion}"
testImplementation "com.wealthfront:magellan-test:${magellanVersion}"

// For support of older version:
implementation "com.wealthfront:magellan-legacy:${magellanVersion}"
```

## Learning

For an explanation of the core concepts of Magellan, see our [wiki](https://github.com/wealthfront/magellan/wiki), starting with [Thinking in Magellan](https://github.com/wealthfront/magellan/wiki/Thinking-in-Magellan).

If you're eager to start, check out our [Quickstart wiki page](https://github.com/wealthfront/magellan/wiki/Quickstart).

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
