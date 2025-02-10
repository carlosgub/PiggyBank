plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    androidTarget()
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":navigation"))
                api(libs.koin.android)
            }
        }
    }
}

android {
    compileSdk = libs.versions.app.compile.sdk.get().toInt()
    namespace = "com.carlosgub.myfinance.app"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    defaultConfig {
        applicationId = "com.carlosgub.myfinance.app"
        minSdk = libs.versions.app.min.sdk.get().toInt()
        targetSdk = (findProperty("android.targetSdk") as String).toInt()
        versionCode = 3
        versionName = "1.1"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildTypes{
        getByName("release") {
            // Enables code shrinking, obfuscation, and optimization for only
            // your project's release build type. Make sure to use a build
            // variant with `isDebuggable=false`.
            isMinifyEnabled = true

            // Enables resource shrinking, which is performed by the
            // Android Gradle plugin.
            isShrinkResources = true
        }
    }
    kotlin {
        jvmToolchain(libs.versions.java.jdk.get().toInt())
    }
}
