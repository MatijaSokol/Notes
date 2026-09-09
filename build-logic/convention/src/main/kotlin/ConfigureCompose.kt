import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Configure Compose-specific options
 */
internal fun Project.configureAndroidCompose() {
  pluginManager.apply(libs.plugins.kotlin.compose.compiler)

  tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
      freeCompilerArgs.addAll(
        listOf(
          "-opt-in=androidx.compose.ExperimentalComposeApi",
          "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
          "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
          "-opt-in=androidx.compose.runtime.ExperimentalComposeApi",
          "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
          "-opt-in=coil.annotation.ExperimentalCoilApi",
        ),
      )

      // generates compose metrics files
      // run with ./gradlew :composeApp:assembleRelease -Pnotes.enableComposeCompilerReports=true
      if (findProperty("notes.enableComposeCompilerReports") == "true") {
        // ../composeApp/build/compose_metrics
        val dir = "${layout.buildDirectory.get()}/compose_metrics"

        freeCompilerArgs.addAll(
          listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=$dir",
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=$dir",
          ),
        )
      }
    }
  }
}
