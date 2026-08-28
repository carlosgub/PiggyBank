import org.jetbrains.compose.compose

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    kotlin("native.cocoapods")
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    jvmToolchain(libs.versions.java.jdk.get().toInt())

    android {
        namespace = "com.carlosgub.myfinances.presentation"
        compileSdk = libs.versions.app.compile.sdk.get().toInt()
        minSdk = libs.versions.app.min.sdk.get().toInt()
        androidResources {
            enable = true
        }
    }

    listOf(
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
                implementation(compose("org.jetbrains.compose.material:material"))
                implementation("org.jetbrains.compose.material3:material3:1.9.0")
                implementation(compose("org.jetbrains.compose.runtime:runtime"))
                implementation(compose("org.jetbrains.compose.foundation:foundation"))
                implementation("org.jetbrains.compose.material:material-icons-extended:1.7.3")
                implementation(compose("org.jetbrains.compose.components:components-resources"))
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

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2025.01.01")
    "androidMainImplementation"(composeBom)
    // Android Studio Preview support
    "androidMainImplementation"("androidx.compose.ui:ui-tooling-preview")
    // Preview-only tooling; not shipped in consumer apps since there are no build variants
    "androidRuntimeClasspath"("androidx.compose.ui:ui-tooling")
}
