@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose.compiler)
}

private val appId = "com.matijasokol.notes"

android {
  namespace = "com.matijasokol.notes"
  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    applicationId = appId
    minSdk = libs.versions.minSdk.get().toInt()
    targetSdk = libs.versions.targetSdk.get().toInt()
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
  }

  signingConfigs {
    getByName("debug") {
      storeFile = file("../signing/debug.keystore")
      storePassword = "TuV9Bf8jcm"
      keyAlias = "notesdebug"
      keyPassword = "DEl7CotoUU"
    }
    create("release") {
      storeFile = file("../signing/release.keystore")
      storePassword = System.getenv("NOTES_STORE_PASSWORD")
      keyAlias = "notes"
      keyPassword = System.getenv("NOTES_KEY_PASSWORD")
    }
  }

  buildTypes {
    debug {
      isDebuggable = true
      signingConfig = signingConfigs.getByName("debug")
    }
    release {
      isDebuggable = false
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro",
      )
      signingConfig = signingConfigs.getByName("release")
    }
  }

  flavorDimensions += "mode"

  productFlavors {
    create("dev") {
      dimension = "mode"
      resValue("string", "app_name", "Notes Dev")
      applicationIdSuffix = ".dev"
      versionNameSuffix = "-dev"
    }

    create("prod") {
      dimension = "mode"
      resValue("string", "app_name", "Notes")
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions {
    jvmTarget = "17"
  }
  buildFeatures {
    compose = true
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

dependencies {
  implementation(projects.presentation)

  implementation(libs.androidx.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.bundles.compose)
  implementation(libs.androidx.compose.navigation)

  implementation(platform(libs.koin.bom))
  implementation(libs.bundles.koin)

  testImplementation(libs.junit)

  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.test.junit)
  androidTestImplementation(libs.bundles.compose.test)

  debugImplementation(libs.bundles.compose.debug)
}
