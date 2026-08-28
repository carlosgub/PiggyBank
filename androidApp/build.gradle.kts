plugins {
    alias(libs.plugins.android.application)
}

android {
    compileSdk = libs.versions.app.compile.sdk.get().toInt()
    namespace = "com.carlosgub.myfinance.app"

    defaultConfig {
        applicationId = "com.carlosgub.myfinance.app"
        minSdk = libs.versions.app.min.sdk.get().toInt()
        targetSdk = (findProperty("android.targetSdk") as String).toInt()
        versionCode = 4
        versionName = "1.2"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
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

dependencies {
    implementation(project(":navigation"))
}
