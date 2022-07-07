import com.avstaim.gradle.Darkside

plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = Darkside.deps.android.compileSdkVersion

    defaultConfig {
        applicationId = "com.yandex.darkside"
        minSdk = Darkside.deps.android.minSdkVersion
        targetSdk = Darkside.deps.android.targetSdkVersion
        versionCode = Darkside.version.versionCode
        versionName = Darkside.version.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        named("release"){
            isMinifyEnabled = false
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"))
        }
    }

    kotlinOptions {
        jvmTarget = Darkside.deps.java.version
    }
}

dependencies {
    implementation(project(":darkside"))

    implementation(kotlin("stdlib", Darkside.deps.kotlin.version))

    implementation (Darkside.deps.androidX.coreKtx)
    implementation (Darkside.deps.androidX.appcompat)
    implementation (Darkside.deps.androidX.recyclerview)

    implementation (Darkside.deps.kotlin.coroutines.core)
    implementation (Darkside.deps.kotlin.coroutines.android)

    testImplementation(Darkside.deps.junit.junit)
}
