package com.avstaim.gradle

/**
 * Helper class to make dependencies declaration more readable.
 */
class Dependency(
        private val artifactName: String,
        private val version: String,
        private val archiveType: String?
) {
    override fun toString(): String {
        var dependencyString = "$artifactName:$version"
        if (archiveType != null) {
            dependencyString += "@$archiveType"
        }
        return dependencyString
    }
}

/**
 * Creates {@link String} with full dependency name.
 * Can be used to declare dependency in .gradle files.
 *
 * @param artifactName should consists of group and name of dependency like $group:$name.
 * @param version of dependency.
 * @param archiveType optional. Can be aar, jar or whatever else.
 */
fun dependency(
        artifactName: String,
        version: String,
        archiveType: String? = null
): String {
    val dep = Dependency(artifactName, version, archiveType)
    return dep.toString()
}
