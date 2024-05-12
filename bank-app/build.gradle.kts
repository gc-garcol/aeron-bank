plugins {
    id("java-application-conventions")
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "gc.garcol"
//version = "0.0.1"

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

@Suppress("DEPRECATION")
val generatedDir = file("${buildDir}/generated/src/main/java")
val codecGeneration = configurations.create("codecGeneration")

dependencies {
    implementation(libs.agrona)
    implementation(libs.aeron)
    implementation(libs.slf4j)
    implementation(libs.logback)
    implementation(project(":common"))
    implementation(project(":cluster-protocol"))
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
}

tasks {
    task("uberJar", Jar::class) {
        group = "uber"
        manifest {
            attributes["Main-Class"] = "gc.garcol.cluster.ClusterApplication"
            attributes["Add-Opens"] = "java.base/sun.nio.ch"
        }
        archiveClassifier.set("uber")
        from(sourceSets.main.get().output)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        dependsOn(configurations.runtimeClasspath)
        from({
            configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
        })
    }

    task("generateCodecs", JavaExec::class) {
        group = "sbe"
        val codecsFile = "src/main/resources/protocol/protocol-codecs.xml"
        val sbeFile = "src/main/resources/protocol/fpl/sbe.xsd"
        inputs.files(codecsFile, sbeFile)
        outputs.dir(generatedDir)
        classpath = codecGeneration
        mainClass.set("uk.co.real_logic.sbe.SbeTool")
        args = listOf(codecsFile)
        systemProperties["sbe.output.dir"] = generatedDir
        systemProperties["sbe.target.language"] = "Java"
        systemProperties["sbe.validation.xsd"] = sbeFile
        systemProperties["sbe.validation.stop.on.error"] = "true"
        outputs.dir(generatedDir)
    }

    compileJava {
        dependsOn("generateCodecs")
    }
}
