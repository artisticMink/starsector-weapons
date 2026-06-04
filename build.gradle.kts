import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

group = "maver.weapons"

val starsectorModFolder: String = providers.gradleProperty("starsectorModFolder").get()

plugins {
    kotlin("jvm") version "2.3.21"
}

repositories {
    mavenCentral()
}

// Target Java 17
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    // LazyLib provides the kotlin runtime at game runtime
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
}

tasks.named<Jar>("jar") {
    archiveFileName.set("MaverWeapons.jar")
}

/**
 * Compile, build the jar and assemble the mod folder
 */
tasks.register<Copy>("packageMod") {
    val jarTask = tasks.named<Jar>("jar")
    dependsOn(jarTask)

    val modFolder = "$starsectorModFolder/MaverWeapons"

    doFirst {
        val destinationDir = file(modFolder)
        if (destinationDir.exists()) {
            destinationDir.deleteRecursively()
        }
    }

    destinationDir = file(starsectorModFolder)
    duplicatesStrategy = DuplicatesStrategy.FAIL

    from(jarTask.flatMap { it.archiveFile }) {
        into("MaverWeapons/jars")
    }

    from("src/main/modfiles/") {
        into("MaverWeapons/")
    }
}
