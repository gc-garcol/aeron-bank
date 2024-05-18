plugins {
    id("java-library-conventions")
}

dependencies {
    implementation(libs.agrona)
    implementation(libs.aeron)
}
