val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

plugins {
    java
}

java {
//    toolchain {
//        languageVersion.set(JavaLanguageVersion.of(21))
//        vendor.set(JvmVendorSpec.AZUL)
//    }
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    //maven local if using locally built Aeron
    mavenLocal()
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}