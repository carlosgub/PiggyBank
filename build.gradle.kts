plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.ktlint)
    alias(libs.plugins.compose.compiler) apply false
}

buildscript {
    dependencies {
        classpath(libs.ktlint)
    }
}
