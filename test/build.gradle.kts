plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    jvmToolchain(libs.versions.java.jdk.get().toInt())

    android {
        namespace = "com.carlosgub.myfinances.test"
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
            baseName = "test"
            isStatic = true
        }
    }

    sourceSets {
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.turbine)
                implementation(libs.orbit.testing)
                implementation(libs.kotlinx.collections.immutable)
                implementation(project(":core"))
                implementation(project(":data"))
                implementation(project(":domain"))
                implementation(project(":presentation"))
            }
        }
    }
}
