plugins {
    alias(libs.plugins.kotlin.multiplatform)
    kotlin("native.cocoapods")
    alias(libs.plugins.android.library)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "presentation"
            isStatic = true
        }
    }

    cocoapods {
        version = "1.0.0"
        summary = "This module is used for the presentation"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "presentation"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.material)
                implementation(compose.material3)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.materialIconsExtended)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                implementation(libs.charts)
                implementation(libs.delight.extension)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.collections.immutable)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.androidx.lifecycle.runtime.compose)
                implementation(project(":domain"))
                implementation(project(":core"))
                implementation(project(":components"))
                implementation(project(":theme"))
            }
        }
    }
}

android {
    namespace = "com.carlosgub.myfinances.presentation"
    compileSdk = libs.versions.app.compile.sdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.app.min.sdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlin {
        jvmToolchain(libs.versions.java.jdk.get().toInt())
    }
    buildFeatures {
        compose = true
    }
}



dependencies {
    // Compose
    val composeBom = platform("androidx.compose:compose-bom:2025.01.01")
    implementation(composeBom)
    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
}
