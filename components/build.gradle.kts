plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ktlint)
}

kotlin {
    androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "components"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.foundation)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.kotlinx.datetime)
            implementation(libs.charts)
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
            implementation(compose.materialIconsExtended)
            implementation(project(":theme"))
            implementation(project(":core"))
        }
    }
}

android {
    namespace = "com.carlosgub.myfinances.components"
    compileSdk = libs.versions.app.compile.sdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.app.min.sdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(libs.versions.java.jdk.get().toInt())
    }
}
