plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.ktlint)
}

kotlin {
    jvmToolchain(libs.versions.java.jdk.get().toInt())

    android {
        namespace = "com.carlosgub.myfinances.data"
        compileSdk = libs.versions.app.compile.sdk.get().toInt()
        minSdk = libs.versions.app.min.sdk.get().toInt()
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "data"
            isStatic = true
        }
    }

    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(libs.delight.android)
            }
        }
        val commonMain by getting{
            dependencies {
                implementation(libs.delight.extension)
                implementation(libs.kotlinx.datetime)
                implementation(project(":domain"))
                implementation(project(":core"))
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

sqldelight {
    databases {
        create("Database") {
            packageName.set("com.carlosgub.myfinance.app")
            generateAsync.set(true)
        }
    }
}
