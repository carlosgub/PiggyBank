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
        namespace = "com.carlosgub.myfinances.components"
        compileSdk = libs.versions.app.compile.sdk.get().toInt()
        minSdk = libs.versions.app.min.sdk.get().toInt()
        androidResources {
            enable = true
        }
    }

    listOf(
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
            implementation(compose("org.jetbrains.compose.material:material"))
            implementation("org.jetbrains.compose.material3:material3:1.9.0")
            implementation(compose("org.jetbrains.compose.foundation:foundation"))
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.kotlinx.datetime)
            implementation(libs.charts)
            implementation(compose("org.jetbrains.compose.components:components-resources"))
            implementation("org.jetbrains.compose.material:material-icons-extended:1.7.3")
            implementation(project(":theme"))
            implementation(project(":core"))
        }
    }
}
