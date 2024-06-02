import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class BuildTypesConventionPlugin : Plugin<Project> {

  override fun apply(project: Project) {
    with(project) {
      extensions.configure<ApplicationExtension> {
        configureSigningConfigs(this)
        configureBuildTypes(this)
      }
    }
  }

  private fun Project.configureSigningConfigs(
    applicationExtension: ApplicationExtension,
  ) {
    applicationExtension.apply {
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
    }
  }

  private fun Project.configureBuildTypes(
    applicationExtension: ApplicationExtension,
  ) {
    applicationExtension.apply {
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
    }
  }
}

