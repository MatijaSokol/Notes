plugins {
  alias(libs.plugins.notes.application)
  alias(libs.plugins.notes.application.compose)
}

private val appId = "com.matijasokol.notes"

android {
  namespace = "com.matijasokol.notes"

  defaultConfig {
    applicationId = appId
    testApplicationId = "$appId.test"
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

  implementation(libs.kotlinx.serialization)

  implementation(platform(libs.koin.bom))
  implementation(libs.bundles.koin)

  testImplementation(libs.junit)

  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.test.junit)
  androidTestImplementation(libs.bundles.compose.test)

  debugImplementation(libs.bundles.compose.debug)
}
