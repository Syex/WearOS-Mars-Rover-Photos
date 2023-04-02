@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("plugin.serialization")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "de.memorian.wearos.marsrover"
    compileSdk = 33

    defaultConfig {
        applicationId = "de.memorian.wearos.marsrover"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(libs.androidx.core)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.workmanager)

    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)

    implementation(libs.androidx.wear.compose.material)
    implementation(libs.androidx.wear.compose.foundation)
    implementation(libs.androidx.wear.compose.navigation)
    implementation(libs.androidx.wear.tiles)
    implementation(libs.androidx.wear.tiles.material)
    implementation(libs.androidx.compose.material.icons)

    implementation(libs.horologist.compose.tools)
    implementation(libs.horologist.tiles)

    implementation(libs.coil)
    implementation(libs.timber)

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.ktor.client)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.json)
    implementation(libs.kotlinx.datetime)

    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.hilt.navigation)
    implementation(libs.dagger.hilt.android)
    kapt(libs.androidx.hilt.compiler)
    kapt(libs.dagger.hilt.compiler)

    implementation(libs.datastore)

    debugImplementation(libs.compose.ui.tooling)
}

dependencies {
    testImplementation(libs.junit5.jupiter.api)
    testRuntimeOnly(libs.junit5.jupiter.engine)

    testImplementation(libs.mockk)

    testImplementation(libs.kotlinx.coroutines.test)

    testImplementation(libs.kotest.assertions)
}