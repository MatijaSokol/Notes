import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import versioning.Versioning
import versioning.Versioning.Version

class AndroidApplicationConventionPlugin : Plugin<Project> {

  override fun apply(project: Project) {
    with(project) {
      val version = Versioning(project.rootDir.path).readVersion()

      applyPlugins()

      extensions.configure<ApplicationExtension> {
        configureAndroid(version, this)
        configureKotlinAndroid(this)
      }
    }
  }

  private fun Project.applyPlugins() {
    with(pluginManager) {
      apply(libs.plugins.android.application)
      apply(libs.plugins.kotlin.android)
      apply(libs.plugins.ksp)
      apply(libs.plugins.notes.productflavors)
      apply(libs.plugins.notes.buildtypes)
      apply(libs.plugins.notes.versioning)
      apply(libs.plugins.notes.quality)
    }
  }

  private fun Project.configureAndroid(
    version: Version,
    applicationExtension: ApplicationExtension,
  ) = applicationExtension.apply {
    buildFeatures {
      buildConfig = true
    }

    defaultConfig {
      targetSdk = libs.versions.targetSdk.get().toInt()

      versionCode = version.versionCode
      versionName = version.versionName

      testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
  }
}


