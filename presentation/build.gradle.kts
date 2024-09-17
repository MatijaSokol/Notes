plugins {
  alias(libs.plugins.notes.library)
  alias(libs.plugins.notes.library.compose)
  alias(libs.plugins.kotlin.android)
}

android {
  namespace = "com.matijasokol.presentation"
}

dependencies {
  implementation(projects.domain)

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
