package com.avstaim.gradle

import org.gradle.api.Project

/**
 * Singleton to keep global project things:
 * - build params
 * - dependency and repository constants
 */
object Darkside {
    lateinit var deps: Dependencies
    lateinit var version: Version
    lateinit var publish: Publish

    fun initWith(target: Project) = also {
        require(target === target.rootProject) {
            "Passport must be initialized only with rootProject"
        }

        deps = Dependencies()
        version = Version()
        publish = Publish()
    }
}
