package com.avstaim.gradle

import kotlin.properties.ReadOnlyProperty

class Publish {

    val group by localProperty(key = "publish.group", default = "com.avstaim.darkside")
    val repoName by localProperty(key = "publish.repo.name", default = "repo")
    val releaseRepo by localProperty(key = "publish.repo.release")
    val snapshotRepo by localProperty(key = "publish.repo.snapshot")
    val username by localProperty(key = "publish.username")
    val password by localProperty(key = "publish.password")

    private fun localProperty(key: String, default: String = "") = ReadOnlyProperty<Any, String> { _, _ ->
        getLocalProperty(key) ?: default
    }

    private fun getLocalProperty(key: String, file: String = "local.properties"): String? {
        val properties = java.util.Properties()
        val localProperties = java.io.File(file)

        return if (localProperties.isFile) {
            java.io.InputStreamReader(java.io.FileInputStream(localProperties), Charsets.UTF_8).use { reader ->
                properties.load(reader)
            }
            properties.getProperty(key)
        } else null
    }
}
