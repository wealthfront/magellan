plugins {
  id("com.android.library")
  kotlin("android")
}

group = extra["GROUP"]!!
version = extra["VERSION_NAME"]!!

android {
  compileSdk = Versions.compileSdkVersion

  defaultConfig {
    minSdk = Versions.minSdkVersion
    targetSdk = Versions.targetSdkVersion

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  kotlinOptions {
    jvmTarget = "1.8"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
    }
  }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
  if (!name.contains("UnitTest")) {
    kotlinOptions.freeCompilerArgs = listOf(
      "-Xjvm-default=compatibility",
      "-Xexplicit-api=strict",
      "-Xopt-in=kotlin.RequiresOptIn"
    )
  }
  kotlinOptions.allWarningsAsErrors = true
  kotlinOptions.jvmTarget = "1.8"
}

dependencies {
  implementation(project(":magellanx-compose"))

  implementation(Dependencies.inject)

  testImplementation(Dependencies.junit)
  testImplementation(Dependencies.truth)
  testImplementation(Dependencies.mockito)
}

apply(from = rootProject.file("gradle/gradle-mvn-push.gradle"))
