import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.1.20"
}

kotlin {
    jvmToolchain(jdkVersion = 21)
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.0")
    testImplementation("org.assertj:assertj-core:3.26.0")
    implementation("org.graphstream:gs-core:2.0")
    implementation("org.openjfx:javafx-swing:26-ea+10")
    implementation("org.graphstream:gs-ui-swing:2.0")
    implementation("com.github.graphstream:gs-ui-javafx:2.0")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

tasks.test {
    useJUnitPlatform()
}
