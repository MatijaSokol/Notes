# Notes Development Guide

## Project Structure

Notes is a Kotlin Multiplatform project targeting Android, iOS, and a JVM server. Gradle includes the
`composeApp`, `shared`, and `server` modules; `iosApp` is the Xcode host project.

- `composeApp`: Compose Multiplatform UI and presentation code. Shared UI lives in `src/commonMain`; Android and
  iOS entry points and platform implementations live in their respective source sets. This module also packages the
  Android application with `dev` and `prod` product flavors.
- `shared`: Code shared by the clients and server, including models, API contracts, networking, authentication, and
  SQLDelight client persistence. `clientMain` is shared by Android and iOS, while `serverMain` feeds the JVM target.
- `server`: Ktor/Netty JVM backend. It depends on `shared` and contains server routes, services, Firebase
  authentication, PostgreSQL/H2 access, and the server SQLDelight database.
- `iosApp`: SwiftUI application shell. Open this directory in Xcode to run iOS; it initializes Firebase and embeds the
  framework produced by `composeApp`.
- `build-logic`: Gradle convention plugins for Java/Kotlin configuration, Android flavors and build types, quality
  checks, and versioning.

Keep platform-independent code in the narrowest applicable shared source set. Use `androidMain` or `iosMain` only
when an implementation genuinely depends on that platform.

## Toolchain

- Use JDK 21. CI uses Temurin 21, and project JVM targets and Android compile options target Java 21.
- Use the checked-in Gradle wrapper (`./gradlew`) rather than a system Gradle installation.
- Android builds require an installed Android SDK. iOS builds require macOS and Xcode.

## Build And Validation

Run the checks relevant to a change. Before submitting broad changes, mirror the complete CI validation:

```sh
./gradlew ktlintCheck detekt --stacktrace
./gradlew assembleProdRelease --stacktrace
./gradlew :server:build --stacktrace
./gradlew composeApp:test --stacktrace
./gradlew shared:test --stacktrace
./gradlew server:test --stacktrace
```

- `assembleProdRelease` builds the signed production Android release and therefore needs the release signing
  environment variables described below.
- `:server:build` includes server compilation and tests; the explicit module test commands match the CI workflow and
  are useful for targeted validation.
- Run `./gradlew ktlintFormat` only when intentionally formatting Kotlin sources, then inspect its changes before
  keeping them.

## Configuration And Secrets

- `NOTES_STORE_PASSWORD` and `NOTES_KEY_PASSWORD` provide Android release-keystore passwords for release builds.
- `NOTES_FIREBASE_CREDENTIALS` provides the Base64-encoded Firebase service-account credentials required to run the
  server.
- The client server URL is currently configured by `BASE_URL` in
  `shared/src/clientMain/kotlin/com/matijasokol/notes/data/client/HttpClient.kt`; it is a source constant, not an
  environment variable.
- Local server database defaults are in `server/src/main/resources/application.yaml`.

Never commit credential values, populated local configuration, signing material, or machine-specific paths. Refer to
secret names only and obtain values through the project's established secure channel. Do not print secrets in test or
build output.

## Style And Generated Code

- Follow `.editorconfig`: spaces, four-space indentation, a 120-character line limit, final newlines, and no trailing
  whitespace. YAML uses two-space indentation.
- Kotlin permits trailing commas in declarations and call sites. Preserve the repository's ktlint exceptions rather
  than overriding them locally.
- Keep changes focused and place code according to existing package and source-set boundaries.
- Do not edit Gradle output, generated Compose resources, or SQLDelight-generated sources. Change the source Kotlin,
  resources, or `.sq`/`.sqm` files instead.
- When changing a SQLDelight schema, add or update migrations as required; migration verification is enabled in both
  database configurations.
