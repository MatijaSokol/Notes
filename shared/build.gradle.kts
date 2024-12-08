import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.notes.quality)
}

kotlin {
    jvm()

    compilerOptions {
        freeCompilerArgs.addAll(
            listOf(
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlin.uuid.ExperimentalUuidApi",
            ),
        )
    }

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project.dependencies.platform(libs.ktor.bom))
            implementation(libs.bundles.ktor.client)
            implementation(libs.arrow.core)
            implementation(libs.arrow.coroutines)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
        }

        val clientMain by creating {
            dependsOn(commonMain.get())
        }

        val serverMain by creating {
            dependsOn(commonMain.get())
        }

        androidMain {
            dependsOn(clientMain)
            dependencies {}
        }

        iosMain {
            dependsOn(clientMain)
            dependencies {}
        }

        val iosX64Main by getting {
            dependsOn(iosMain.get())
        }

        val iosArm64Main by getting {
            dependsOn(iosMain.get())
        }

        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain.get())
        }

        jvmMain {
            dependsOn(serverMain)
            dependencies {}
        }
    }
}

android {
    namespace = "com.matijasokol.notes.shared"
    compileSdk = libs.versions.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
}
