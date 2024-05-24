pluginManagement {
    // Include 'plugins build' to define convention plugins.
    includeBuild("bank-libs/build-logic")
}

plugins {
    // Apply plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

include("api-gateway")
include(
    "bank-libs:common",
    "bank-libs:app-util",
    "bank-libs:cluster-protocol",
    "bank-cluster",
    "bank-app"
)

rootProject.name = "aeron-bank"
