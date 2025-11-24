import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.notes.quality)
    alias(libs.plugins.sqldelight)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            listOf(
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlin.uuid.ExperimentalUuidApi",
                "-opt-in=kotlin.time.ExperimentalTime",
            ),
        )
    }

    jvm()

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
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

            implementation(libs.firebase)
        }

        val clientMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                implementation(libs.sqldelight.coroutines)
            }
        }

        val serverMain by creating {
            dependsOn(commonMain.get())
        }

        androidMain {
            dependsOn(clientMain)
            dependencies {
                implementation(libs.ktor.client.okhttp)
                implementation(libs.sqldelight.driver.android)
            }
        }

        iosMain {
            dependsOn(clientMain)
            dependencies {
                implementation(libs.ktor.client.darwin)
                implementation(libs.sqldelight.driver.native)
            }
        }

        iosX64Main { dependsOn(iosMain.get()) }
        iosArm64Main { dependsOn(iosMain.get()) }
        iosSimulatorArm64Main { dependsOn(iosMain.get()) }

        jvmMain { dependsOn(serverMain) }
    }
}

android {
    namespace = "com.matijasokol.notes.shared"
    compileSdk = libs.versions.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
}

sqldelight {
    // this will be the name of the generated database class
    databases.create("ClientDatabase") {
        // package name used for the database class
        packageName.set("com.matijasokol.notes.client")

        // directory where .db schema files should be stored, relative to the project root
        // use ./gradlew data:tasks to list all available tasks for generating schema
        // available task should be run before every migration
        schemaOutputDirectory.set(file("src/main/sqldelight/databases"))

        // migration files will fail during the build process if there are any errors in them
        verifyMigrations.set(true)
    }
}
