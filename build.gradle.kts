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
    implementation("org.xerial:sqlite-jdbc:3.41.2.2")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("com.googlecode.lanterna:lanterna:3.1.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}