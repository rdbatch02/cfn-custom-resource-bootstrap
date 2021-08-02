plugins {
    kotlin("jvm") version "1.5.20"
    kotlin("plugin.serialization") version "1.5.20"
    `maven-publish`
}
repositories {
    mavenCentral()
}

group = "com.batchofcode"

val ktor_version = "1.6.0"
val coroutines_verson = "1.5.0"
val serialization_version = "1.2.2"
val mockk_version = "1.12.0"

dependencies {
    implementation(kotlin("stdlib"))
    api("com.amazonaws:aws-lambda-java-core:1.2.1")
    api("io.ktor:ktor-client-apache:$ktor_version")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_verson")
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:$serialization_version")

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

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = rootProject.name
            version = project.version.toString()
            from(components["java"])
            artifact(sourcesJar)
        }
    }
    repositories {
        maven {
            name = "GithubPackages"
            url = uri("https://maven.pkg.github.com/rdbatch02/cfn-custom-resource-bootstrap")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}