plugins {
  id("com.android.library")
  kotlin("android")
  `maven-publish`
}

android {
  namespace = "com.ryanmoelter.magellanx.core"
  compileSdk = 32

  defaultConfig {
    minSdk = 23
    targetSdk = 32

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
    aarMetadata {
      minCompileSdk = 32
    }
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

  publishing {
    multipleVariants {
      allVariants()
      withJavadocJar()
      withSourcesJar()
    }
  }
}

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().all {
  kotlinOptions.freeCompilerArgs = listOf(
    "-Xexplicit-api=strict",
    "-opt-in=kotlin.RequiresOptIn"
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
  testImplementation(libs.androidx.test.core)
  testImplementation(libs.androidx.test.core.ktx)
  testImplementation("org.mockito:mockito-core:4.3.1")
  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.3")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}

publishing {
  publications {
    register<MavenPublication>("default") {
      groupId = extra["GROUP"] as String
      artifactId = extra["POM_ARTIFACT_ID"] as String
      version = extra["VERSION_NAME"] as String

      pom {
        name.set(extra["POM_NAME"] as String)
        packaging = extra["POM_PACKAGING"] as String
        description.set(extra["POM_DESCRIPTION"] as String)
        url.set(extra["POM_URL"] as String)

        scm {
          url.set(extra["POM_SCM_URL"] as String)
          connection.set(extra["POM_SCM_CONNECTION"] as String)
          developerConnection.set(extra["POM_SCM_DEV_CONNECTION"] as String)
        }

        licenses {
          license {
            name.set(extra["POM_LICENCE_NAME"] as String)
            url.set(extra["POM_LICENCE_URL"] as String)
            distribution.set(extra["POM_LICENCE_DIST"] as String)
          }
        }

        developers {
          developer {
            id.set(extra["POM_DEVELOPER_ID"] as String)
            name.set(extra["POM_DEVELOPER_NAME"] as String)
          }
        }
      }

      afterEvaluate {
        from(components["release"])
      }
    }
  }
}
