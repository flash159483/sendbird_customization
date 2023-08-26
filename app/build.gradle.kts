import org.jetbrains.kotlin.konan.properties.Properties

val sendbirdFiles = rootProject.file("sendbird.properties")
val properties = Properties()
properties.load(sendbirdFiles.inputStream())

plugins {
    kotlin("android")
    id("com.android.application")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.lighthouse.sendbird_demo"

    defaultConfig {
        applicationId = "com.lighthouse.sendbird_demo"
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.appVersion.get()

        buildConfigField(
            "String",
            "SENDBIRD_APPLICATION",
            properties.getProperty("application_id")
        )

        buildConfigField(
            "String",
            "SENDBIRD_USER_ID",
            properties.getProperty("user_id")
        )

        buildConfigField(
            "String",
            "SENDBIRD_USER_NAME",
            properties.getProperty("user_name")
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = true // APK or AAB
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        dataBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.sendbird.uikit)
    implementation(libs.hilt)
    implementation(libs.bundles.androidx.ui.foundation)
    implementation(libs.bundles.android.basic.ui)
    implementation(libs.bundles.image)
    kapt(libs.hilt.kapt)
    implementation(libs.bundles.basic.test)
    implementation(libs.bundles.retrofit)
    implementation(libs.bundles.gson)
    implementation(libs.bundles.navigation)
}