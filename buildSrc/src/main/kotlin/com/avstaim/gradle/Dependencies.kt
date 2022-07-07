package com.avstaim.gradle

import org.gradle.api.JavaVersion

class Dependencies {
    val android = Android()
    val androidX = AndroidX()
    val dokka = Dokka()
    val java = Java()
    val junit = Junit()
    val kotlin = Kotlin()
    val okhttp = OkHttp()
    val plugins = Plugins()
    val strikt = Strikt()
}

class Android {
    val compileSdkVersion = 32
    val minSdkVersion = 21
    val targetSdkVersion = 32
    val buildToolsVersion = "32.0.0"
}

class AndroidX {
    val espresso = Espresso()
    val lifecycle = Lifecycle()
    val test = Test()
    val work = Work()

    val activity = dependency("androidx.activity:activity", "1.4.0")
    val activityKtx = dependency("androidx.activity:activity-ktx", "1.4.0")
    val annotation = dependency("androidx.annotation:annotation", "1.4.0")
    val appcompat = dependency("androidx.appcompat:appcompat", "1.4.2")
    val browser = dependency("androidx.browser:browser", "1.4.0")
    val cardview = dependency("androidx.cardview:cardview", "1.0.0")
    val collection = dependency("androidx.collection:collection", "1.2.0")
    val collectionKtx = dependency("androidx.collection:collection-ktx", "1.2.0")
    val constraintLayout = dependency("androidx.constraintlayout:constraintlayout", "2.1.4")
    val constraintLayoutSolver = dependency("androidx.constraintlayout:constraintlayout-solver", "2.0.4")
    val coordinatorLayout = dependency("androidx.coordinatorlayout:coordinatorlayout", "1.2.0")
    val core = dependency("androidx.core:core", "1.8.0")
    val coreKtx = dependency("androidx.core:core-ktx", "1.8.0")
    val dataStore = dependency("androidx.datastore:datastore", "1.0.0")
    val dataStorePreferences = dependency("androidx.datastore:datastore-preferences", "1.0.0")
    val drawerLayout = dependency("androidx.drawerlayout:drawerlayout", "1.1.1")
    val dynamicAnimation = dependency("androidx.dynamicanimation:dynamicanimation", "1.0.0")
    val exifinterface = dependency("androidx.exifinterface:exifinterface", "1.3.3")
    val fragment = dependency("androidx.fragment:fragment", "1.4.1")
    val fragmentKtx = dependency("androidx.fragment:fragment-ktx", "1.4.1")
    val gridLayout = dependency("androidx.gridlayout:gridlayout", "1.0.0")
    val legacyPreferenceV14 = dependency("androidx.legacy:legacy-preference-v14", "1.0.0")
    val legacySupportV4 = dependency("androidx.legacy:legacy-support-v4", "1.0.0")
    val legacySupportV13 = dependency("androidx.legacy:legacy-support-v13", "1.0.0")
    val localBroadcastManager = dependency("androidx.localbroadcastmanager:localbroadcastmanager", "1.1.0")
    val material = dependency("com.google.android.material:material", "1.6.1")
    val media = dependency("androidx.media:media", "1.6.0")
    val multidex = dependency("androidx.multidex:multidex", "2.0.1")
    val palette = dependency("androidx.palette:palette", "1.0.0")
    val preference = dependency("androidx.preference:preference", "1.2.0")
    val preferenceKtx = dependency("androidx.preference:preference-ktx", "1.2.0")
    val startupRuntime = dependency("androidx.startup:startup-runtime", "1.1.1")
    val emoji2 = dependency("androidx.emoji2:emoji2", "1.1.0")
    val emoji2ViewHelper = dependency("androidx.emoji2:emoji2-views-helper", "1.1.0")

    val recyclerview = dependency("androidx.recyclerview:recyclerview", "1.2.1")
    val recyclerviewSelection = dependency("androidx.recyclerview:recyclerview-selection","1.1.0")
    val swiperefreshlayout = dependency("androidx.swiperefreshlayout:swiperefreshlayout", "1.1.0")
    val testRunner = dependency("androidx.test:runner", "1.2.0")
    val transition = dependency("androidx.transition:transition", "1.4.1")
    val vectorDrawable = dependency("androidx.vectordrawable:vectordrawable", "1.1.0")
    val viewPager2 = dependency("androidx.viewpager2:viewpager2", "1.0.0")

    class Espresso {
        private val version = "3.1.0"
        val contrib = dependency("androidx.test.espresso:espresso-contrib", version)
        val core = dependency("androidx.test.espresso:espresso-core", version)
        val idlingResource = dependency("androidx.test.espresso:espresso-idling-resource", version)
        val intents = dependency("androidx.test.espresso:espresso-intents", version)
    }

    class Lifecycle {
        val version = "2.4.1"
        val common = dependency("androidx.lifecycle:lifecycle-common", version)
        val commonJava8 = dependency("androidx.lifecycle:lifecycle-common-java8", version)
        val extensions = dependency("androidx.lifecycle:lifecycle-extensions", "2.2.0")
        val compiler = dependency("androidx.lifecycle:lifecycle-compiler", version)
        val livedata = dependency("androidx.lifecycle:lifecycle-livedata", version)
        val livedataKtx = dependency("androidx.lifecycle:lifecycle-livedata-ktx", version)
        val runtime = dependency("androidx.lifecycle:lifecycle-runtime", version)
        val runtimeKtx = dependency("androidx.lifecycle:lifecycle-runtime-ktx", version)
        val service = dependency("androidx.lifecycle:lifecycle-service", version)
        val viewmodel = dependency("androidx.lifecycle:lifecycle-viewmodel", version)
        val viewmodelKtx = dependency("androidx.lifecycle:lifecycle-viewmodel-ktx", version)
    }

    class Test {
        private val version = "1.4.0"
        private val extJunitVersion = "1.1.2"

        val archCore = dependency("androidx.arch.core:core-testing", "2.1.0")
        val core = dependency("androidx.test:core", version)
        val coreKtx = dependency("androidx.test:core-ktx", version)
        val orchestrator = dependency("androidx.test:orchestrator", version)
        val rules = dependency("androidx.test:rules", version)
        val runner = dependency("androidx.test:runner", version)
        val extJunit = dependency("androidx.test.ext:junit", extJunitVersion)
        val uiautomator = dependency("androidx.test.uiautomator:uiautomator", "2.2.0")
    }

    class Work {
        private val version = "2.7.1"

        val runtime = dependency("androidx.work:work-runtime", version)
        val runtimeKtx = dependency("androidx.work:work-runtime-ktx", version)
        val gcm = dependency("androidx.work:work-gcm", version)
        val testing = dependency("androidx.work:work-testing", version)
        val multiprocess = dependency("androidx.work:work-multiprocess", version)
    }
}

class Dokka {
    val version = "1.7.0"

    val plugin = dependency("org.jetbrains.dokka:dokka-gradle-plugin", version)
}

class Java {
    val version = "1.8"
    val javaVersion = JavaVersion.VERSION_1_8
}

class Junit {
    val version = "5.3.1"
    val junit = dependency("junit:junit", "4.13")
    val jupiterApi = dependency("org.junit.jupiter:junit-jupiter-api", version)
    val jupiterEngine = dependency("org.junit.jupiter:junit-jupiter-engine", version)
    val jupiterParams = dependency("org.junit.jupiter:junit-jupiter-params", version)
    val vintageEngine = dependency("org.junit.vintage:junit-vintage-engine", version)
}

class Kotlin {
    val version = "1.6.21"
    val bom = dependency("org.jetbrains.kotlin:kotlin-bom", version)
    val coroutines = Coroutines()
    val gradlePlugin = dependency("org.jetbrains.kotlin:kotlin-gradle-plugin", version)
    val allopen = dependency("org.jetbrains.kotlin:kotlin-allopen", version)
    val reflect = dependency("org.jetbrains.kotlin:kotlin-reflect", version)
    val serialization = Serialization(version)
    // should be used by default
    val stdlib = dependency("org.jetbrains.kotlin:kotlin-stdlib-jdk8", version)
    val stdlibCommon = dependency("org.jetbrains.kotlin:kotlin-stdlib-common", version)
    val testJunit = dependency("org.jetbrains.kotlin:kotlin-test-junit", version)
    val testCommon = dependency("org.jetbrains.kotlin:kotlin-test-common", version)
    val testAnnotationsCommon = dependency("org.jetbrains.kotlin:kotlin-test-annotations-common", version)
    val mockito = dependency("org.mockito.kotlin:mockito-kotlin", "3.2.0")
    val mockk = dependency("io.mockk:mockk", "1.12.4")

    class Coroutines {
        val version = "1.6.1"
        val bom = dependency("org.jetbrains.kotlinx:kotlinx-coroutines-bom", version)
        val android = dependency("org.jetbrains.kotlinx:kotlinx-coroutines-android", version)
        val core = dependency("org.jetbrains.kotlinx:kotlinx-coroutines-core", version)
        val coreJvm = dependency("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm", version)
        val test = dependency("org.jetbrains.kotlinx:kotlinx-coroutines-test", version)
    }

    class Serialization(kotlinVersion: String) {
        val version = "1.3.2"
        val plugin = dependency("org.jetbrains.kotlin:kotlin-serialization", kotlinVersion)
        val runtime = dependency("org.jetbrains.kotlin:kotlin-serialization-runtime", version)
        val core = dependency("org.jetbrains.kotlinx:kotlinx-serialization-core", version)
        val json = dependency("org.jetbrains.kotlinx:kotlinx-serialization-json", version)
        val protobuf = dependency("org.jetbrains.kotlinx:kotlinx-serialization-protobuf", version)
    }
}

class OkHttp {
    val version = "4.10.0"
    val okhttp = dependency("com.squareup.okhttp3:okhttp", version)
    val loggingInterceptor = dependency("com.squareup.okhttp3:logging-interceptor", version)
    val mockWebServer = dependency("com.squareup.okhttp3:mockwebserver", version)
}

class Plugins {
    val androidGradlePlugin = dependency("com.android.tools.build:gradle", "7.2.1")
    val excludeFromJar = dependency("com.yandex.android.common:excludefromjar-plugin", "1.0.3")
    val r8 = dependency("com.android.tools:r8", "3.0.78")
}

class Strikt {
    val version = "0.34.1"

    val core = dependency("io.strikt:strikt-core", version)
}
