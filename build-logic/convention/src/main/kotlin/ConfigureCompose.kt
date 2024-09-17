import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Configure Compose-specific options
 */
internal fun Project.configureAndroidCompose(
  commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
  pluginManager.apply(libs.plugins.kotlin.compose.compiler)

  commonExtension.apply {
    buildFeatures {
      compose = true
    }

    dependencies {
      val bom = libs.androidx.compose.bom
      add("implementation", platform(bom))
      add("implementation", libs.bundles.compose)
      add("debugImplementation", libs.bundles.compose.debug)
      add("androidTestImplementation", platform(bom))
    }
  }

  extensions.configure<ComposeCompilerGradlePluginExtension> {
    featureFlags.set(
      setOf(
        ComposeFeatureFlag.StrongSkipping,
        ComposeFeatureFlag.IntrinsicRemember,
        ComposeFeatureFlag.OptimizeNonSkippingGroups,
      ),
    )
    includeSourceInformation.set(true)
  }

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
      // run with ./gradlew assembleRelease -Pshowcase.enableComposeCompilerReports=true
      if (findProperty("showcase.enableComposeCompilerReports") == "true") {
        // ../showcase/build/compose_metrics
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