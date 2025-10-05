plugins {
    kotlin("jvm") version "2.1.20"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.0")
    testImplementation("org.assertj:assertj-core:3.26.0")
}

kotlin {
    jvmToolchain(
        jdkVersion = 21) // Ensures Kotlin compiles to JVM 21
}

tasks.test {
    useJUnitPlatform()
}