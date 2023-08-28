rootProject.name = "MyApplication"

include(":androidApp")
include(":shared")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        mavenCentral()
    }

    plugins {
        val kotlinVersion = extra["kotlin.version"] as String
        val agpVersion = extra["agp.version"] as String
        val composeVersion = extra["compose.version"] as String

        kotlin("jvm").version(kotlinVersion)
        kotlin("multiplatform").version(kotlinVersion)
        kotlin("android").version(kotlinVersion)

        id("com.android.application").version(agpVersion)
        id("com.android.library").version(agpVersion)
        id("org.jetbrains.compose").version(composeVersion)
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven {
            name = "GitHubPackages"
            /*  Configure path to the library hosted on GitHub Package Registry
             *  Replace UserID with package owner userID and REPOSITORY with the repository name
             *  e.g. "https://maven.pkg.github.com/enefce/AndroidLibraryForGitHubPackagesDemo"
             */
            url = uri("https://maven.pkg.github.com/carlosgub/kotlinm-charts")

            credentials {
                username = "carlosgub"
                password = "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"
            }
        }
    }
}
