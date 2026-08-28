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
        namespace = "com.carlosgub.myfinances.domain"
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
            baseName = "domain"
            isStatic = true
        }
    }

    cocoapods {
        version = "1.0.0"
        summary = "Some description for the Domain Module"
        homepage = "Link to the Domain Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "domain"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.koin.core)
                api(libs.androidx.navigation)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlin.stdlib)
                api(libs.orbit.core)
                implementation(compose("org.jetbrains.compose.components:components-resources"))
                implementation("org.jetbrains.compose.material:material-icons-extended:1.7.3")
                implementation(project(":theme"))
                implementation(project(":core"))
            }
        }
        val androidMain by getting {
            dependencies {
                api(libs.bundles.android)
                implementation(libs.delight.android)
                implementation(libs.koin.android)
            }
        }
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(libs.delight.ios)
            }
        }
    }
}
