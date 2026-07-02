plugins {
    java
    id("com.gradleup.shadow") version "9.3.2"
    id("io.github.intisy.github-gradle") version "1.8.2.1"
}

group = "me.kaiyan.missilewarfare"

fun latestGitTagVersion(): String? = try {
    val out = providers.exec { workingDir = rootDir; commandLine("git","describe","--tags","--abbrev=0"); isIgnoreExitValue = true }
    if (out.result.get().exitValue == 0) out.standardOutput.asText.get().trim().removePrefix("gh-").removePrefix("v").takeIf { it.isNotBlank() } else null
} catch (e: Exception) { null }

version = (project.findProperty("artifact_version") as String?)?.removePrefix("v")?.takeIf { it.isNotBlank() } ?: latestGitTagVersion() ?: "1.6.13"
val versionSuffix: String = when {
    !(project.findProperty("artifact_version") as String?).isNullOrBlank() -> ""
    System.getenv("GITHUB_ACTIONS") == "true" -> "-EXPERIMENTAL"
    else -> "-UNOFFICIAL"
}
val displayVersion = "${project.version}$versionSuffix"
description = "MissileWarfare is a Slimefun addon adding missiles and warfare items."

github {
    accessToken = System.getenv("GITHUB_TOKEN") ?: ""
    publish {
        tag = System.getenv("GITHUB_REF_NAME")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo.glaremasters.me/repository/towny/")
    maven("https://jitpack.io")
}

dependencies {
<<<<<<< HEAD
    implementation("com.github.Slimefun5:SlimefunMetrics:master-SNAPSHOT")
<<<<<<< HEAD
    compileOnly("io.papermc.paper:paper-api:${property("paperApiVersion")}")
=======
=======
    githubImplementation("Slimefun5:SlimefunMetrics:v1.0.0")
>>>>>>> origin/experimental
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
>>>>>>> origin/experimental
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
    githubCompileOnly("Slimefun5:Slimefun5:gh-v5.2.3.2")

    // External softdepend compileOnly
    // TODO: WorldGuard/WorldEdit/Towny are softdepends â€” commented out due to
    // strict version constraints in worldedit-bukkit conflicting with Paper API.
    // The integration classes are stubbed; re-enable when compatible versions are available.
    // compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.7")
    // compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.3.9")
    // compileOnly("com.palmergames.bukkit.towny:towny:0.99.5.0")

    // Shaded
    
    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.mockito:mockito-core:5.15.2")
    testImplementation("org.slf4j:slf4j-simple:2.0.16")
    testImplementation("org.mockbukkit.mockbukkit:mockbukkit-v1.21:4.107.0") {
        exclude(group = "org.jetbrains", module = "annotations")
    }
}

configurations.testImplementation {
    extendsFrom(configurations.compileOnly.get())
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
    processResources {
        filesMatching("plugin.yml") {
            expand("version" to displayVersion)
        }
    }
    jar {
        enabled = false
    }
    shadowJar {
<<<<<<< HEAD
        archiveFileName.set("MissileWarfare v${project.version}.jar")
=======
        relocate("org.bstats", "missilewarfare.libs.bstats")
<<<<<<< HEAD
        archiveFileName.set("MissileWarfare-1.6.13-UNOFFICIAL.jar")
>>>>>>> origin/experimental
=======
        archiveFileName.set("MissileWarfare-$displayVersion.jar")
>>>>>>> origin/experimental
                exclude("META-INF/**")
    }
    build {
        dependsOn(shadowJar)
    }
    compileTestJava {
        enabled = false
    }
    test {
        enabled = false
    }
}
