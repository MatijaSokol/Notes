plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.notes.quality)
}

group = "com.matijasokol.notes.server"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.addAll(
            listOf(
                "-opt-in=kotlin.io.encoding.ExperimentalEncodingApi",
                "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
                "-opt-in=kotlin.uuid.ExperimentalUuidApi",
                "-Xcontext-receivers",
            ),
        )
    }
}

dependencies {
    implementation(projects.shared)
    implementation(project.dependencies.platform(libs.ktor.bom))
    implementation(libs.bundles.ktor.server)
    implementation(libs.postgresql)
    implementation(libs.h2)
    implementation(libs.logback.classic)
    implementation(libs.firebase.admin)
    implementation(libs.koin.core)
    implementation(libs.koin.ktor)
    implementation(libs.koin.logger)
    implementation(libs.arrow.core)
    implementation(libs.arrow.coroutines)
    implementation(libs.sqldelight.driver.jdbc)
    implementation(libs.sqldelight.dialect.postgresql)
    implementation(libs.hikari)
}

sqldelight {
    // this will be the name of the generated database class
    databases.create("ServerDatabase") {
        // package name used for the database class
        packageName.set("com.matijasokol.notes.server")

        // generate suspending query methods with asynchronous drivers
        // disabled, requires R2dbc driver
        // generateAsync.set(true)

        // directory where .db schema files should be stored, relative to the project root
        // use ./gradlew data:tasks to list all available tasks for generating schema
        // available task should be run before every migration
        schemaOutputDirectory.set(file("src/main/sqldelight/databases"))

        // migration files will fail during the build process if there are any errors in them
        verifyMigrations.set(true)

        dialect(libs.sqldelight.dialect.postgresql)
    }
}
