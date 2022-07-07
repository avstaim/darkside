buildscript {

    repositories {
        google()
        mavenCentral()
    }

    val darkside = com.avstaim.gradle.Darkside.initWith(project)

    dependencies {
        classpath(darkside.deps.plugins.androidGradlePlugin)
        classpath(darkside.deps.kotlin.gradlePlugin)
        classpath(darkside.deps.kotlin.allopen)
        classpath(darkside.deps.okhttp.okhttp)
        classpath(darkside.deps.dokka.plugin)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register<Delete>("clean").configure {
    delete(rootProject.buildDir)
}
