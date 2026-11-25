import Versions.detektVersion
import Versions.kotlinVersion
import Versions.kotlinterVersion
import Versions.kspVersion

object Plugins {
  const val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
  const val ksp = "com.google.devtools.ksp:symbol-processing-gradle-plugin:$kspVersion"
  const val kotlinterGradle = "org.jmailen.gradle:kotlinter-gradle:$kotlinterVersion"
  const val kotlinAllOpen = "org.jetbrains.kotlin:kotlin-allopen:$kotlinVersion"
  const val detekt = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:$detektVersion"
}