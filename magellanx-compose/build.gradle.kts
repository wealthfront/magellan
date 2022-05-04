plugins {
  id("com.android.library")
  kotlin("android")
  alias(libs.plugins.kotest)
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

  buildFeatures {
    compose = true
  }

  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.compose.get()
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

tasks.withType<Test> {
  useJUnitPlatform()
}

dependencies {
  api(project(":magellanx-core"))

  // Kotlin
  implementation(libs.coroutines.core)
  implementation(libs.coroutines.android)
  testImplementation(libs.coroutines.test)

  // Android
  implementation(libs.material)
  implementation(libs.appcompat)
  implementation(libs.constraintlayout)
  implementation(libs.ktx.core)

  // Compose
  implementation(libs.compose.ui)
  implementation(libs.compose.ui.tooling)
  implementation(libs.compose.foundation)
  implementation(libs.compose.material3)
  implementation(libs.compose.material.icons.core)
  implementation(libs.compose.material.icons.extended)
  implementation(libs.activity.compose)
  androidTestImplementation(libs.compose.ui.test)
  implementation(libs.accompanist.systemuicontroller)
  implementation(libs.accompanist.drawablepainter)

  // Testing
  testImplementation(libs.kotest.runner.junit5)
  testImplementation(libs.kotest.assertions.core)
  testImplementation(libs.kotest.extensions.robolectric)
  testImplementation(libs.robolectric)
  testImplementation(libs.androidx.test.core)
  testImplementation(libs.androidx.test.core.ktx)
  androidTestImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.3")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
