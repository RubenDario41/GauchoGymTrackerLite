// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

        // Declara los plugins aquí con apply false si son necesarios en submódulos
        kotlin("jvm") version "1.9.0" apply false




}