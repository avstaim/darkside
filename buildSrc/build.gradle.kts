import org.gradle.kotlin.dsl.`kotlin-dsl`

val kotlinVersion = "1.6.21"

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8", kotlinVersion))
}

tasks {
    compileKotlin {
        kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        kotlinOptions.jvmTarget = "11"
    }
}
