plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.serialization") version "1.5.31"
}

group = "dev.ikorniyenko"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}