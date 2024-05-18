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

dependencies {
    implementation(libs.agrona)
    implementation(libs.aeron)
    implementation(libs.slf4j)
    implementation(libs.logback)
    implementation(project(":bank-libs:common"))
    implementation(project(":bank-libs:cluster-protocol"))
    implementation("org.springframework.boot:spring-boot-starter")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
}

tasks {
    task("runSingleNodeCluster", JavaExec::class) {
        group = "run"
        classpath = sourceSets.main.get().runtimeClasspath
        mainClass.set("gc.garcol.cluster.ClusterApplication")
        jvmArgs("--add-opens=java.base/sun.nio.ch=ALL-UNNAMED")
    }

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
}
