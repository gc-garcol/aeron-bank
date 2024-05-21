plugins {
    id("java-application-conventions")
    id("org.springframework.boot") version libs.versions.springBoot.get()
    id("io.spring.dependency-management") version libs.versions.springDependencyManagement.get()
}

group = "gc.garcol"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}
dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")
    implementation("org.springframework.cloud:spring-cloud-starter-consul-discovery")
    implementation(libs.micrometerOtel)
    implementation(libs.opentelemetryExporterOtlp)
    implementation("org.springframework.boot:spring-boot-starter-actuator")

}

extra["springCloudVersion"] = "2023.0.1"
dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}
