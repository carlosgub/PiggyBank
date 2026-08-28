import org.jetbrains.compose.compose

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ktlint)
}

kotlin {
    jvmToolchain(libs.versions.java.jdk.get().toInt())

    android {
        namespace = "com.carlosgub.myfinances.core"
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
            baseName = "core"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose("org.jetbrains.compose.runtime:runtime"))
            implementation(compose("org.jetbrains.compose.foundation:foundation"))
            api(libs.androidx.navigation)
            api(libs.kotlinx.datetime)
        }
    }
}
