plugins {
    id("fabric-loom") version "1.14-SNAPSHOT"
}

base {
    archivesName = "render-swap"
    version = "1.0.0"
    group = "com.renderswap"
}

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/") { name = "Fabric" }
    maven("https://maven.meteordev.org/releases") { name = "Meteor Dev Releases" }
    maven("https://maven.meteordev.org/snapshots") { name = "Meteor Dev Snapshots" }
}

dependencies {
    minecraft("com.mojang:minecraft:1.21.11")
    mappings("net.fabricmc:yarn:1.21.11+build.3:v2")
    modImplementation("net.fabricmc:fabric-loader:0.18.1")
    modImplementation("meteordevelopment:meteor-client:1.21.11-SNAPSHOT")
}

tasks {
    processResources {
        val propertyMap = mapOf(
            "version" to "1.0.0",
            "mc_version" to "1.21.11",
        )
        filesMatching("fabric.mod.json") { expand(propertyMap) }
    }

    jar {
        inputs.property("archivesName", project.base.archivesName.get())
        from("LICENSE") { rename { "${it}_${inputs.properties["archivesName"]}" } }
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release = 21
    }
}