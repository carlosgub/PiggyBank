import org.jetbrains.compose.compose

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    kotlin("native.cocoapods")
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ktlint)
}

kotlin {
    jvmToolchain(libs.versions.java.jdk.get().toInt())

    android {
        namespace = "com.carlosgub.myfinances.navigation"
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
            baseName = "navigation"
            isStatic = true
        }
    }

    cocoapods {
        version = "1.0.0"
        summary = "This module is used for the navigation"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "navigation"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose("org.jetbrains.compose.runtime:runtime"))
            api(libs.androidx.navigation)
            implementation("org.jetbrains.compose.material3:material3:1.9.0")
            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(project(":core"))
            implementation(project(":domain"))
            implementation(project(":data"))
            implementation(project(":theme"))
            implementation(project(":presentation"))
            implementation(compose("org.jetbrains.compose.components:components-resources"))
        }
        androidMain.dependencies {
            api(libs.bundles.android)
            implementation(libs.koin.android)
        }
    }
}
