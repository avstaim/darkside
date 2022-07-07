import com.avstaim.gradle.Darkside

plugins {
    id("com.android.library")
    id("kotlin-android")
}

apply(from = rootProject.file("publish.gradle.kts"))

android {
    compileSdk = Darkside.deps.android.compileSdkVersion

    defaultConfig {
        minSdk = Darkside.deps.android.minSdkVersion
        minSdk = Darkside.deps.android.targetSdkVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        named("release"){
            isMinifyEnabled = false
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"))
        }
    }

    kotlinOptions {
        freeCompilerArgs += "-Xopt-in=com.avstaim.darkside.dsl.views.InternalApi"
        freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        freeCompilerArgs += "-Xopt-in=kotlinx.coroutines.DelicateCoroutinesApi"
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            //withJavadocJar()
        }
    }
}

dependencies {

    implementation(kotlin("stdlib", Darkside.deps.kotlin.version))

    implementation(Darkside.deps.androidX.coreKtx)
    implementation(Darkside.deps.androidX.appcompat)
    implementation(Darkside.deps.androidX.constraintLayout)
    implementation(Darkside.deps.androidX.coordinatorLayout)
    implementation(Darkside.deps.androidX.recyclerview)

    implementation(Darkside.deps.androidX.lifecycle.commonJava8)
    implementation(Darkside.deps.androidX.lifecycle.runtimeKtx)
    implementation(Darkside.deps.androidX.preferenceKtx)
    implementation(Darkside.deps.androidX.transition)
    implementation(Darkside.deps.androidX.material)

    implementation(Darkside.deps.kotlin.coroutines.core)
    implementation(Darkside.deps.kotlin.coroutines.android)

    testImplementation(Darkside.deps.junit.junit)
    testImplementation(Darkside.deps.kotlin.mockk)
    testImplementation(Darkside.deps.strikt.core)
    testImplementation(Darkside.deps.androidX.test.core)
    testImplementation(Darkside.deps.androidX.test.coreKtx)
}
