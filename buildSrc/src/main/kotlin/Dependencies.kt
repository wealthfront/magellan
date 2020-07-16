import Versions.archVersion
import Versions.butterKnifeVersion
import Versions.daggerVersion
import Versions.jacksonVersion
import Versions.junitVersion
import Versions.kotlinVersion
import Versions.kotlinterVersion
import Versions.lifecycleVersion
import Versions.mockitoVersion
import Versions.okhttpVersion
import Versions.retrofitVersion
import Versions.robolectricVersion
import Versions.rxandroidVersion
import Versions.rxjava2Version
import Versions.rxjavaAdapterVersion
import Versions.rxjavaVersion
import Versions.supportLibVersion
import Versions.truthVersion

object Dependencies {

  const val kotlinJdk = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
  const val appCompat = "androidx.appcompat:appcompat:$supportLibVersion"
  const val lifecycle = "androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion"

  val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
  val kotlinterGradle = "org.jmailen.gradle:kotlinter-gradle:$kotlinterVersion"

  const val junit = "junit:junit:$junitVersion"
  const val truth = "com.google.truth:truth:$truthVersion"
  const val mockito = "org.mockito:mockito-core:$mockitoVersion"
  const val archTesting = "androidx.arch.core:core-testing:$archVersion"
  const val robolectric = "org.robolectric:robolectric:$robolectricVersion"
  const val butterknife = "com.jakewharton:butterknife:$butterKnifeVersion"
  const val butterknifeCompiler = "com.jakewharton:butterknife-compiler:$butterKnifeVersion"
  const val dagger = "com.google.dagger:dagger:$daggerVersion"
  const val daggerCompiler = "com.google.dagger:dagger-compiler:$daggerVersion"

  const val retrofit = "com.squareup.retrofit2:retrofit:$retrofitVersion"
  const val rxjava = "io.reactivex:rxjava:$rxjavaVersion"
  const val rxjava2 = "io.reactivex.rxjava2:rxjava:$rxjava2Version"
  const val rxjavaAdapter = "com.squareup.retrofit2:adapter-rxjava:$rxjavaAdapterVersion"
  const val rxandroid = "io.reactivex:rxandroid:$rxandroidVersion"
  const val jackson = "com.squareup.retrofit2:converter-jackson:$jacksonVersion"
  const val okhttp = "com.squareup.okhttp3:logging-interceptor:$okhttpVersion"
  
  const val extJunit = "androidx.test.ext:junit:1.1.1"
  const val espressoCore = "com.android.support.test.espresso:espresso-core:3.0.2"
}

