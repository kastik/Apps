plugins {
    id("com.kastik.library.compose")
    id("com.kastik.hilt")
}

android {
    namespace = "com.kastik.apps.auth"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
}