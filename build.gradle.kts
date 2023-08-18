import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    id("com.github.johnrengelman.shadow") version "6.0.0"
}

group = "borg.locutus"
version = "0.0.1"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")

    implementation("com.google.inject:guice:4.0")
    implementation("org.hibernate:hibernate-core:5.0.12.Final")
    implementation("com.google.code.gson:gson:2.2.4")

    compileOnly("org.projectlombok:lombok:+")
    annotationProcessor("org.projectlombok:lombok:+")
}

tasks {
    named<ShadowJar>("shadowJar") {
        fun relocate(pattern: String) {
            relocate(pattern, "guicehibernateplugin.$pattern")
        }

        relocate("antlr")
        relocate("com.google.common")
        relocate("com.google.gson")
        relocate("com.google.inject")
        relocate("com.google.thirdparty.publicsuffix")
        relocate("javassist")
        relocate("javax.inject")
        relocate("javax.persistence")
        relocate("javax.transaction")
        relocate("javax.xml.parsers")
        relocate("javax.xml.transform")
        relocate("license")
        relocate("org.aopalliance")
        relocate("org.apache.xmlcommons")
        relocate("org.dom4j")
        relocate("org.hibernate")
        relocate("org.jboss.jandex")
        relocate("org.jboss.logging")
        relocate("org.w3c.dom")
        relocate("org.xml.sax")

        dependencies {
            exclude(dependency("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT"))
            exclude(dependency("org.projectlombok:lombok"))
        }

        archiveFileName.set("GuiceHibernatePlugin.jar")
    }

    processResources {
        expand("version" to project.version)
    }

    build { dependsOn(shadowJar) }
}

tasks.register<Copy>("copy") {
    from("${buildDir}/libs/GuiceHibernatePlugin.jar")
    into("C:/Daten/Java/server/plugins")
}
