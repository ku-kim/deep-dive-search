plugins {
    kotlin("jvm") version "2.0.10"
}

group = "kunhee.kim"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")

    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.8.0")
    testImplementation("io.kotest:kotest-assertions-core-jvm:5.8.0")
    testImplementation("io.kotest:kotest-property-jvm:5.8.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
