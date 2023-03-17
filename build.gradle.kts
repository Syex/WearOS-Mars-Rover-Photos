buildscript {
    dependencies {
        classpath(kotlin("gradle-plugin", version = libs.versions.kotlin.get()))
    }
}

plugins {
    id("com.android.application") version libs.versions.android.gradle.plugin.get() apply false
    kotlin("android") version libs.versions.kotlin.get() apply false
}