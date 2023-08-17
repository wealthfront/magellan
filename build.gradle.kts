// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
  repositories {
    mavenCentral()
    gradlePluginPortal()
    google()
  }
  dependencies {
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
    classpath("com.android.tools.build:gradle:7.4.2")
    classpath("org.jmailen.gradle:kotlinter-gradle:${libs.versions.kotlinter.get()}")
    classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${libs.versions.detekt.get()}")
  }
}

plugins {
  alias(libs.plugins.detekt)
  alias(libs.plugins.kotlinter)
}

allprojects {
  apply(plugin = "org.jmailen.kotlinter")
  apply(plugin = "io.gitlab.arturbosch.detekt")

  group = extra["GROUP"]!!
  version = extra["VERSION_NAME"]!!

  repositories {
    mavenCentral()
    google()
  }

  kotlinter {
    tasks.findByName("lint")?.dependsOn("lintKotlin")
  }

  detekt {
    config = files("$rootDir/config/detekt/detekt-config.yml")
    parallel = true
    autoCorrect = true
    reports {
      html {
        enabled = true
        destination = file("build/reports/detekt.html")
      }
    }
    tasks.findByName("check")?.dependsOn("detekt")
  }

  tasks.withType<Test> {
    reports {
      html.required.set(true)
    }
  }
}

tasks.register("clean", Delete::class) {
  delete(rootProject.buildDir)
}
