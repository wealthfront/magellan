[![Run tests](https://github.com/ryanmoelter/magellan-x/actions/workflows/runTests.yml/badge.svg?branch=main)](https://github.com/ryanmoelter/magellan-x/actions/workflows/runTests.yml)

# Magellan X

A simple, flexible, and practical navigation framework for Android using Jetpack Compose.

## Why would I use Magellan?

- **Simple**: Intuitive abstractions and encapsulation make it easy to reason through code.
- **Flexible**: The infinitely-nestable structure allows for many different styles of structuring an app and navigating between pages.
- **Practical**: We pay special attention to simplifying common patterns and removing day-to-day boilerplate.
- **Testable**: Plain objects that are easy to instantiate and control make testing simple.
 
## Download

Add the dependency you need in your `build.gradle`:

```kotlin
implementation("com.github.ryanmoelter:magellan-x:0.1.0")
```

> Note: JitPack seems to package all modules together, so the test library is bundled with the main library for now

## Learning

> Note: This library is a fork of [wealthfront/magellan](https://github.com/wealthfront/magellan), and is in the process of getting published. In the meantime, you can refer to the original's wiki pages below.

For an explanation of the core concepts of Magellan, see the source repo's [wiki](https://github.com/wealthfront/magellan/wiki), starting with [Thinking in Magellan](https://github.com/wealthfront/magellan/wiki/Thinking-in-Magellan).

If you're eager to start, check out the source repo's [Quickstart wiki page](https://github.com/wealthfront/magellan/wiki/Quickstart).

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
