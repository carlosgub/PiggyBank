plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    kotlin("multiplatform").apply(false)
    id("com.android.application").apply(false)
    id("com.android.library").apply(false)
    id("org.jetbrains.compose").version("1.5.3").apply(false)
    id("com.google.gms.google-services").version("4.3.14").apply(false)
    id("org.jlleitschuh.gradle.ktlint").version("11.5.1")
}
