import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.1.20"
}

kotlin {
    jvmToolchain( jdkVersion = 21)
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.0")
    testImplementation("org.assertj:assertj-core:3.26.0")
}



tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

tasks.test {
    useJUnitPlatform()
}