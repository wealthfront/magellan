[![Latest release on Jitpack](https://jitpack.io/v/com.ryanmoelter/magellanx.svg)](https://jitpack.io/#com.ryanmoelter/magellanx)
[![Test results](https://github.com/ryanmoelter/magellanx/actions/workflows/runTests.yml/badge.svg?branch=main)](https://github.com/ryanmoelter/magellanx/actions/workflows/runTests.yml)

# Magellan X

A simple, flexible, and practical navigation framework for Android using Jetpack Compose.

## Why would I use Magellan?

- **Simple**: Intuitive abstractions and encapsulation make it easy to reason through code.
- **Flexible**: The infinitely-nestable structure allows for many different styles of structuring an
  app and navigating between pages.
- **Practical**: We pay special attention to simplifying common patterns and removing day-to-day
  boilerplate.
- **Testable**: Plain objects that are easy to instantiate and control make testing simple.

## Download

Add jitpack to your `repositories` (in your root `build.gradle.kts` file):

```kotlin
allprojects {
  repositories {
    // ...
    maven("https://jitpack.io")
  }
}
```

Add the dependencies you need in your `dependencies` block:

[![Latest release on Jitpack](https://jitpack.io/v/com.ryanmoelter/magellanx.svg)](https://jitpack.io/#com.ryanmoelter/magellanx)

```kotlin
val magellanxVersion = "0.1.1"
implementation("com.ryanmoelter.magellanx:magellanx-compose:${magellanxVersion}")
testImplementation("com.ryanmoelter.magellanx:magellanx-test:${magellanxVersion}")
```

Alternatively, if you only want the core library without the Compose implementation, you can use:

```kotlin
implementation("com.github.ryanmoelter.magellanx:magellanx-core:0.1.1")
```

> Note: `magellanx-core` is included in and exposed by `magellanx-compose`, and `magellan-test` only
> applies to `magellanx-compose`.

<details>
  <summary>Version catalogs</summary>
  
  ### Version catalogs
  
  To use in [gradle's version catalogs](https://docs.gradle.org/current/userguide/platforms.html),
  add the following to your `libs.versions.toml`:
  
  ```toml
  [versions]
  magellanx = "0.1.1"
  # ...
  
  [libraries]
  magellanx-compose = { module = "com.ryanmoelter.magellanx:magellanx-compose", version.ref = "magellanx" }
  magellanx-test = { module = "com.ryanmoelter.magellanx:magellanx-test", version.ref = "magellanx" }
  # Alternatively:
  # magellanx-core = { module = "com.ryanmoelter.magellanx:magellanx-core", version.ref = "magellanx" }
  ```
</details>

## Learning

> Note: This library is a fork of [wealthfront/magellan](https://github.com/wealthfront/magellan),
> and is in the process of getting published. In the meantime, you can refer to the original's wiki
> pages below.

For an explanation of the core concepts of Magellan, see the source
repo's [wiki](https://github.com/wealthfront/magellan/wiki), starting
with [Thinking in Magellan](https://github.com/wealthfront/magellan/wiki/Thinking-in-Magellan).

If you're eager to start, check out the source
repo's [Quickstart wiki page](https://github.com/wealthfront/magellan/wiki/Quickstart).

## License

```
Copyright 2022 Ryan Moelter

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

Based on code from [wealthfront/magellan](https://github.com/wealthfront/magellan) licensed by:

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
