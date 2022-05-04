plugins {
  id("com.android.library")
  kotlin("android")
}

android {
  compileSdk = 31

  defaultConfig {
    minSdk = 23
    targetSdk = 31

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  kotlinOptions {
    jvmTarget = "1.8"
  }

  testOptions {
    unitTests {
      isIncludeAndroidResources = true
    }
  }
}

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().all {
  kotlinOptions.freeCompilerArgs = listOf(
    "-Xexplicit-api=strict",
    "-Xopt-in=kotlin.RequiresOptIn"
  )
}

dependencies {
  // Kotlin
  implementation(libs.coroutines.core)
  implementation(libs.coroutines.android)
  testImplementation(libs.coroutines.test)

  // Android
  implementation(libs.material)
  implementation(libs.appcompat)
  implementation(libs.constraintlayout)
  implementation(libs.ktx.core)

  // Testing
  testImplementation(libs.kotest.assertions.core)
  testImplementation(libs.kotest.extensions.robolectric)
  testImplementation(libs.robolectric)
  testImplementation(libs.androidx.test.core)
  testImplementation(libs.androidx.test.core.ktx)
  testImplementation("org.mockito:mockito-core:4.3.1")
  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.3")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
