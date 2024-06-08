plugins {
    alias(libs.plugins.kotlin.multiplatform)
    kotlin("native.cocoapods")
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    cocoapods {
        version = "1.0.0"
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                api(libs.koin.core)
                implementation(libs.koin.compose)
                api(libs.bundles.precompose)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlin.stdlib)
                implementation(libs.charts)
                api(libs.orbit.core)
                implementation(libs.delight.extension)
                implementation(libs.kotlinx.collections.immutable)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.turbine)
                implementation(libs.orbit.testing)
            }
        }
        val androidMain by getting {
            dependencies {
                api(libs.bundles.android)
                implementation(libs.delight.android)
                implementation(libs.koin.android)
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(libs.delight.ios)
            }
        }
    }
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "com.carlosgub.myfinance.app.common"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("com.carlosgub.myfinance.app")
            generateAsync.set(true)
        }
    }
}
