import com.avstaim.gradle.Darkside

apply(plugin = "maven-publish")
apply(plugin = "com.android.library")

group=Darkside.publish.group

configure<PublishingExtension> {
    publications {
        create<MavenPublication>("maven") {
            groupId = Darkside.publish.group
            artifactId = "darkside"
            version = Darkside.version.versionName

            afterEvaluate {
                from(components["release"])
            }
        }
    }
    repositories {
        maven {
            name = Darkside.publish.repoName
            credentials {
                username = Darkside.publish.username
                password = Darkside.publish.password
            }

            val releasesRepoUrl = uri(Darkside.publish.releaseRepo)
            val snapshotsRepoUrl = uri(Darkside.publish.snapshotRepo)
            url = if (Darkside.version.isRelease) releasesRepoUrl else snapshotsRepoUrl
        }
    }
}
