plugins {
    id("java-library-conventions")
}

dependencies {
    implementation(libs.micrometerOtel)
    implementation(libs.opentelemetryExporterOtlp)
}
