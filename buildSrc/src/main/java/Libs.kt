import Versions.androidXCoreVersion
import Versions.archVersion
import Versions.butterKnifeVersion
import Versions.constraintLayoutVersion
import Versions.coroutinesVersion
import Versions.daggerVersion
import Versions.detektVersion
import Versions.espressoVersion
import Versions.glideVersion
import Versions.jacksonVersion
import Versions.javaInjectVersion
import Versions.jodaTimeVersion
import Versions.junitTestExtVersion
import Versions.junitVersion
import Versions.kotlinVersion
import Versions.kotlinterVersion
import Versions.lifecycleVersion
import Versions.lintVersion
import Versions.materialVersion
import Versions.mockKVersion
import Versions.mockitoVersion
import Versions.okhttpVersion
import Versions.recyclerViewVersion
import Versions.retrofitVersion
import Versions.robolectricVersion
import Versions.rxAndroid2Version
import Versions.rxandroidVersion
import Versions.rxjava2Version
import Versions.rxjavaAdapterVersion
import Versions.rxjavaVersion
import Versions.supportLibVersion
import Versions.testCoreVersion
import Versions.testRunnerVersion
import Versions.truthVersion
import Versions.uiAutomatorVersion

object Plugins {
  const val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
  const val kotlinterGradle = "org.jmailen.gradle:kotlinter-gradle:$kotlinterVersion"
  const val kotlinAllOpen = "org.jetbrains.kotlin:kotlin-allopen:$kotlinVersion"
  const val detekt = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:$detektVersion"
}

object Libs {

  const val appCompat = "androidx.appcompat:appcompat:$supportLibVersion"
  const val constraintLayout = "androidx.constraintlayout:constraintlayout:$constraintLayoutVersion"
  const val lifecycle = "androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion"
  const val androidXCore = "androidx.core:core-ktx:$androidXCoreVersion"

  const val material = "com.google.android.material:material:$materialVersion"
  const val recyclerView = "androidx.recyclerview:recyclerview:$recyclerViewVersion"
  const val junit = "junit:junit:$junitVersion"
  const val truth = "com.google.truth:truth:$truthVersion"
  const val mockito = "org.mockito:mockito-core:$mockitoVersion"
  const val mockitoAndroid = "org.mockito:mockito-android:$mockitoVersion"
  const val mockK = "io.mockk:mockk:$mockKVersion"
  const val archTesting = "androidx.arch.core:core-testing:$archVersion"
  const val robolectric = "org.robolectric:robolectric:$robolectricVersion"
  const val butterknife = "com.jakewharton:butterknife:$butterKnifeVersion"
  const val butterknifeCompiler = "com.jakewharton:butterknife-compiler:$butterKnifeVersion"
  const val dagger = "com.google.dagger:dagger:$daggerVersion"
  const val daggerCompiler = "com.google.dagger:dagger-compiler:$daggerVersion"
  const val inject = "javax.inject:javax.inject:$javaInjectVersion"
  const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
  const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
  const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion"

  const val glide = "com.github.bumptech.glide:glide:$glideVersion"
  const val retrofit = "com.squareup.retrofit2:retrofit:$retrofitVersion"
  const val retrofitMock = "com.squareup.retrofit2:retrofit-mock:$retrofitVersion"
  const val rxjavaAdapter = "com.squareup.retrofit2:adapter-rxjava:$rxjavaAdapterVersion"
  const val rxJava2Adapter = "com.squareup.retrofit2:adapter-rxjava2:$rxjavaAdapterVersion"
  const val rxjava = "io.reactivex:rxjava:$rxjavaVersion"
  const val rxjava2 = "io.reactivex.rxjava2:rxjava:$rxjava2Version"
  const val rxandroid = "io.reactivex:rxandroid:$rxandroidVersion"
  const val rxAndroid2 = "io.reactivex.rxjava2:rxandroid:$rxAndroid2Version"
  const val jackson = "com.squareup.retrofit2:converter-jackson:$jacksonVersion"
  const val okhttp = "com.squareup.okhttp3:logging-interceptor:$okhttpVersion"
  const val jodaTime = "net.danlew:android.joda:$jodaTimeVersion"

  const val testCore = "androidx.test:core:$testCoreVersion"
  const val testCoreKtx = "androidx.test:core-ktx:$testCoreVersion"
  const val testRunner = "androidx.test:runner:$testRunnerVersion"
  const val testRules = "com.android.support.test:rules:$testRunnerVersion"
  const val uiAutomator ="androidx.test.uiautomator:uiautomator:$uiAutomatorVersion"
  const val extJunit = "androidx.test.ext:junit:$junitTestExtVersion"
  const val extJunitKtx = "androidx.test.ext:junit-ktx:$junitTestExtVersion"
  const val espressoCore = "androidx.test.espresso:espresso-core:$espressoVersion"
  const val espressoContrib = "androidx.test.espresso:espresso-contrib:$espressoVersion"
  const val rx2idler = "com.squareup.rx.idler:rx2-idler:0.11.0"

  const val lintApi = "com.android.tools.lint:lint-api:$lintVersion"
  const val lintChecks = "com.android.tools.lint:lint-checks:$lintVersion"
  const val lint = "com.android.tools.lint:lint:$lintVersion"
  const val lintTests = "com.android.tools.lint:lint-tests:$lintVersion"
  const val testUtils = "com.android.tools:testutils:$lintVersion"
}

