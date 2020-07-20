plugins {
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.serialization") version "1.3.72"
    `maven-publish`
    id("com.jfrog.bintray") version "1.8.4"
}
repositories {
    mavenCentral()
    jcenter()
}

group = "com.batchofcode"

val ktor_version = "1.3.2"
val coroutines_verson = "1.3.5"
val serialization_version = "0.20.0"
val mockk_version = "1.10.0"

dependencies {
    compileOnly(kotlin("stdlib"))
    implementation("io.ktor:ktor-client-apache:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_verson")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serialization_version")

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
    testImplementation("io.mockk:mockk:$mockk_version")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "11"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "11"
    }
}

bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_KEY")
    publish = true
    setPublications("maven")
    pkg(closureOf<com.jfrog.bintray.gradle.BintrayExtension.PackageConfig> {
        repo = "com.batchofcode"
        name = "cfn-custom-resource-bootstrap"
        setLicenses("MIT")
        vcsUrl = "https://github.com/rdbatch02/cfn-custom-resource-bootstrap"
        setVersion(System.getenv("RELEASE_VERSION"))
    })
}

tasks.withType<Jar>() {

    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
}

//val sourcesJar by tasks.creating(Jar::class) {
//    archiveClassifier.set("sources")
//    from(sourceSets.getByName("main").allSource)
//}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = rootProject.name
            version = project.version.toString()
            from(components["java"])
        }
    }
}