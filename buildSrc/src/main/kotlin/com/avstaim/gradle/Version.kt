@file:Suppress("MemberVisibilityCanBePrivate")

package com.avstaim.gradle

class Version {

    val versionMajor = 0
    val versionMinor = 2
    val versionPatch = 9

    val snapshotBase = "20221215"
    val snapshotPatch = "01"
    val snapshot = "$snapshotBase.$snapshotPatch"

    val isRelease = false

    val versionName = if (isRelease) "$versionMajor.$versionMinor.$versionPatch" else snapshot
    val versionNameInteger = versionMajor * 10000 + versionMinor * 100 + versionPatch

    val buildNumber = System.getenv("BUILD_NUMBER")?.toInt() ?: Integer.MAX_VALUE
    val versionCode = if (buildNumber == Integer.MAX_VALUE) Integer.MAX_VALUE else versionNameInteger * 10000 + buildNumber
}
