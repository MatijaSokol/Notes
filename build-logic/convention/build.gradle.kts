import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  `kotlin-dsl`
}

group = "com.matijasokol.notes.buildlogic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>().configureEach {
  compilerOptions {
    jvmTarget.set(JvmTarget.JVM_17)
  }
}

dependencies {
  compileOnly(libs.android.gradlePlugin)
  compileOnly(libs.kotlin.gradlePlugin)
  compileOnly(libs.detekt.gradlePlugin)
  compileOnly(libs.ktlint.gradlePlugin)
  compileOnly(libs.kotlin.compose.compiler.gradlePlugin)

  // used for type safe access to version catalog
  compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}


gradlePlugin {
  plugins {
    register("androidApplicationPlugin") {
      id = "notes.android.application"
      implementationClass = "AndroidApplicationConventionPlugin"
    }
    register("androidApplicationComposePlugin") {
      id = "notes.android.application.compose"
      implementationClass = "AndroidApplicationComposeConventionPlugin"
    }
    register("androidLibraryPlugin") {
      id = "notes.android.library"
      implementationClass = "AndroidLibraryConventionPlugin"
    }
    register("androidLibraryComposePlugin") {
      id = "notes.android.library.compose"
      implementationClass = "AndroidLibraryComposeConventionPlugin"
    }
    register("jvmLibraryPlugin") {
      id = "notes.jvm.library"
      implementationClass = "JvmLibraryConventionPlugin"
    }
    register("versioningPlugin") {
      id = "notes.versioning"
      implementationClass = "versioning.VersioningPlugin"
    }
    register("productFlavorsPlugin") {
      id = "notes.productflavors"
      implementationClass = "ProductFlavorsConventionPlugin"
    }
    register("buildTypesPlugin") {
      id = "notes.buildtypes"
      implementationClass = "BuildTypesConventionPlugin"
    }
    register("qualityPlugin") {
      id = "notes.quality"
      implementationClass = "QualityConventionPlugin"
    }
  }
}