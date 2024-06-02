import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class ProductFlavorsConventionPlugin : Plugin<Project> {

  override fun apply(project: Project) {
    project.extensions.configure<ApplicationExtension> {
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
    }
  }
}