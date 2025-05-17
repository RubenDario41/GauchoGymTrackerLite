plugins {
    alias(libs.plugins.android.application)

}

android {
    namespace = "com.example.gauchogymtrackerlite"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.gauchogymtrackerlite"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
val roomVersion = "2.6.1"
dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.core.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

        // Room components
    // implementation "androidx.room:room-runtime:$room_version"
    // annotationProcessor "androidx.room:room-compiler:$room_version"
    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor(libs.androidx.room.compiler)
    // Opcional - Soporte para Coroutines/Flow (si lo usaras más adelante)
    // implementation "androidx.room:room-ktx:$room_version"

    // Opcional - Soporte para Paging 3 (si necesitaras paginación)
    // implementation "androidx.room:room-paging:$room_version"
}