plugins {
    id("com.android.application") version libs.versions.android.gradle.plugin.get() apply false
    kotlin("android") version libs.versions.kotlin.get() apply false
    kotlin("plugin.serialization") version libs.versions.kotlin.get() apply false
    kotlin("kapt") version libs.versions.kotlin.get() apply false
    id("com.google.dagger.hilt.android") version libs.versions.hilt.get() apply false
}

allprojects {
    tasks.withType(Test::class) {
        useJUnitPlatform()
    }
}