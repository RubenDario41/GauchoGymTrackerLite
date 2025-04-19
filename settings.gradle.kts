pluginManagement {
    plugins {
        // Define las versiones de los plugins aquí
        kotlin("jvm") version "1.9.0"
        kotlin("kapt") version "1.9.0"
        id("com.android.application") version "8.0.0"
        id("org.jetbrains.kotlin.android") version "1.9.0"
    }
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

// Incluye los módulos del proyecto
include(":app")
// include(":otherModule") // Si tienes más módulos, descomenta esta línea
rootProject.name = "GauchoGymTrackerLite"
include(":app")
